package com.matheus.servermanager.database.model;

import java.sql.ResultSet;
import java.util.Iterator;

import com.matheus.servermanager.database.database.DatabaseException;
import com.matheus.servermanager.database.query.RowIterator;

public class ModelIterator<M extends DatabaseModel> implements Iterator<M> {

	private final Table<M> table;
	private final RowIterator rowIterator;

	public ModelIterator(Table<M> table, ResultSet resultSet) {
		this.table = table;
		this.rowIterator = new RowIterator(resultSet);
	}

	@Override
	public boolean hasNext() {
		return this.rowIterator.hasNext();
	}

	@Override
	public M next() throws DatabaseException {
		return this.table.newInstance(this.rowIterator.next());
	}
}
