package com.matheus.servermanager.database.query;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.matheus.servermanager.database.QuestUtils;
import com.matheus.servermanager.database.database.Database;
import com.matheus.servermanager.database.database.DatabaseException;

public class Count implements Filterable {

	private final Database database;

	public Database getDatabase() {
		return database;
	}

	public String getTable() {
		return table;
	}

	private final String table;
	private List<String> columns;
	private Optional<DatabaseFilter> filter;
	private Optional<String> order;
	private Optional<String> limit;

	public Count(Database database, String table) {
		this.database = database;
		this.table = table;
		this.columns = new ArrayList<>();
		this.filter = Optional.empty();
		this.order = Optional.empty();
		this.limit = Optional.empty();
	}

	public Count(Count count) {
		this(count.database, count.table);
		this.columns = new ArrayList<>(count.columns);
		this.filter = Optional.empty();
		if (count.filter.isPresent()) {
			this.filter = Optional.of(new DatabaseFilter(count.filter.get()));
		}
		this.order = count.order;
		this.limit = count.limit;
	}

	@Override
	public Count where(String field, Object value) {
		return this.where(field, value, Operator.EQUALS);
	}

	@Override
	public Count where(String field, Object value, Operator operator) {
		return this.where(new DatabaseFilter(field, value, operator));
	}

	@Override
	public Count where(DatabaseFilter filter) {
		if (this.filter.isPresent()) {
			this.filter.get().and(filter);
		} else {
			this.filter = Optional.of(filter);
		}
		return this;
	}

	public Count columns(String... columns) {
		return columns(Arrays.asList(columns));
	}

	public Count columns(List<String> columns) {
		this.columns.clear();
		this.columns.addAll(columns);
		return this;
	}

	public Count limit(int count) {
		this.limit = Optional.of(count + "");
		return this;
	}

	public Count limit(int from, int to) {
		this.limit = Optional.of(from + "," + to);
		return this;
	}

	public Count order(String field) {
		return this.order(field, "ASC");
	}

	public Count order(String field, String direction) {
		this.order = Optional.of(QuestUtils.getField(field) + " " + direction);
		return this;
	}

	public String build() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(");
		sql.append(this.buildColumnString());
		sql.append(") 'Count' FROM ");
		sql.append("`");
		sql.append(this.table);
		sql.append("`");
		if (this.filter.isPresent()) {
			Optional<String> where = this.filter.get().build();
			if (where.isPresent()) {
				sql.append(" WHERE ");
				sql.append(where.get());
			}
		}
		if (this.order.isPresent()) {
			sql.append(" ORDER BY ");
			sql.append(this.order.get());
		}
		if (this.limit.isPresent()) {
			sql.append(" LIMIT ");
			sql.append(this.limit.get());
		}
		sql.append(";");
		return sql.toString();
	}

	public int execute() throws DatabaseException {
		String sql = this.build();
		try (PreparedStatement statement = this.database.createQueryStatement(sql, true)) {
			try (ResultSet set = statement.executeQuery()) {
				if (set.next()) return set.getInt("Count");
			}
		} catch (SQLException e) {
			throw new DatabaseException(String.format("Failed statement: %s", sql), e);
		}
		return 0;
	}

	public RowIterator executeIterator() throws DatabaseException {
		String sql = this.build();
		PreparedStatement statement = this.database.createQueryStatement(sql, false);
		try {
			return new RowIterator(statement.executeQuery());
		} catch (SQLException e) {
			throw new DatabaseException(String.format("Failed statement: %s", sql), e);
		}
	}

	private String buildColumnString() {
		if (this.columns.isEmpty()) {
			return "*";
		} else {
			List<String> columns = new ArrayList<>(this.columns);
			for (int i = 0; i < columns.size(); i++) {
				columns.set(i, QuestUtils.getField(columns.get(i)));
			}
			return String.join(",", columns);
		}
	}
}