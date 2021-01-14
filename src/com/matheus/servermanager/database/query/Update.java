package com.matheus.servermanager.database.query;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.matheus.servermanager.database.QuestUtils;
import com.matheus.servermanager.database.database.Database;
import com.matheus.servermanager.database.database.DatabaseException;

public class Update implements Filterable {

	private final Database database;
	private final String table;
	private Map<String, Object> values;
	private Optional<DatabaseFilter> filter;

	public Update(Database database, String table) {
		this.database = database;
		this.table = table;
		this.values = new LinkedHashMap<>();
		this.filter = Optional.empty();
	}

	public Update(Update update) {
		this(update.database, update.table);
		this.values = new HashMap<>(update.values);
		this.filter = Optional.empty();
		if (update.filter.isPresent()) {
			this.filter = Optional.of(new DatabaseFilter(update.filter.get()));
		}
	}

	public Update set(String field, Object value) {
		values.put(field, value);
		return this;
	}

	@Override
	public Update where(String field, Object value) {
		return this.where(field, value, Operator.EQUALS);
	}

	@Override
	public Update where(String field, Object value, Operator operator) {
		return this.where(new DatabaseFilter(field, value, operator));
	}

	@Override
	public Update where(DatabaseFilter filter) {
		if (this.filter.isPresent()) {
			this.filter.get().and(filter);
		} else {
			this.filter = Optional.of(filter);
		}
		return this;
	}

	public String build() throws DatabaseException {
		List<String> set = new ArrayList<>(this.values.size());
		for (Map.Entry<String, Object> entry : this.values.entrySet()) {
			StringBuilder builder = new StringBuilder();
			builder.append(QuestUtils.getField(entry.getKey()));
			builder.append(" = ");
			builder.append(QuestUtils.getValue(entry.getValue()));
			set.add(builder.toString());
		}

		if (set.size() == 0) {
			throw new DatabaseException("No updates to be made.");
		}

		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE ");
		sql.append(QuestUtils.getField(this.table));
		sql.append(" SET ");
		sql.append(String.join(", ", set));

		if (this.filter.isPresent()) {
			Optional<String> where = this.filter.get().build();
			if (where.isPresent()) {
				sql.append(" WHERE ");
				sql.append(where.get());
			}
		}

		sql.append(";");
		return sql.toString();
	}

	public void execute() throws DatabaseException {
		String sql = this.build();
		try (PreparedStatement statement = this.database.createUpdateStatement(sql)) {
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new DatabaseException(String.format("Failed statement: %s", sql), e);
		}
	}
}
