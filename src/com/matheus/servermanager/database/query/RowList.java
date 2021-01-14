package com.matheus.servermanager.database.query;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.matheus.servermanager.database.database.DatabaseException;

public class RowList extends ArrayList<Row> {

	private static final long serialVersionUID = 1L;

	public Row first() {
		return get(0);
	}

	public Row last() {
		return get(size() - 1);
	}

	void addRows(ResultSet resultSet) {
		try {
			while (resultSet.next()) {
				add(new Row(resultSet));
			}
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}
}
