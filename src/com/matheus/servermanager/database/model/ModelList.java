package com.matheus.servermanager.database.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.matheus.servermanager.database.database.DatabaseException;
import com.matheus.servermanager.database.query.Row;

public class ModelList<M extends DatabaseModel> extends ArrayList<M> {

	private static final long serialVersionUID = 1L;
	private final Table<M> table;

	public Table<M> getTable() {
		return table;
	}

	public ModelList(Table<M> table) {
		this.table = table;
	}

	public M first() {
		if (size() == 0) {
			return null;
		}
		return get(0);
	}

	public M last() {
		if (size() == 0) {
			return null;
		}
		return get(size() - 1);
	}

	void addRows(ResultSet resultSet) {
		try {
			while (resultSet.next()) {
				add(this.table.newInstance(new Row(resultSet)));
			}
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	@Override
	public String toString() {
		return super.toString();
	}
}
