package com.matheus.servermanager.database.query;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

import com.matheus.servermanager.database.QuestUtils;
import com.matheus.servermanager.database.database.Database;
import com.matheus.servermanager.database.database.DatabaseException;

public class Delete implements Filterable {

	private final Database database;
	private final String table;
	private Optional<DatabaseFilter> filter;

	public Delete(Database database, String table) {
		this.database = database;
		this.table = table;
		this.filter = Optional.empty();
	}

	public Delete(Delete delete) {
		this(delete.database, delete.table);
		this.filter = Optional.empty();
		if (delete.filter.isPresent()) {
			this.filter = Optional.of(new DatabaseFilter(delete.filter.get()));
		}
	}

	@Override
	public Delete where(String field, Object value) {
		return this.where(field, value);
	}

	@Override
	public Delete where(String field, Object value, Operator operator) {
		return this.where(new DatabaseFilter(field, value, operator));
	}

	@Override
	public Delete where(DatabaseFilter filter) {
		if (this.filter.isPresent()) {
			this.filter.get().and(filter);
		} else {
			this.filter = Optional.of(filter);
		}
		return this;
	}

	public String build() throws DatabaseException {
		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM ");
		sql.append(QuestUtils.getField(this.table));

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
		try (PreparedStatement statement = this.database.createQueryStatement(sql, false)) {
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new DatabaseException(String.format("Failed statement: %s", sql), e);
		}
	}
}
