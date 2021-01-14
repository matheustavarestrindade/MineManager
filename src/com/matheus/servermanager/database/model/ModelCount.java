package com.matheus.servermanager.database.model;

import com.matheus.servermanager.database.database.DatabaseException;
import com.matheus.servermanager.database.query.Count;
import com.matheus.servermanager.database.query.DatabaseFilter;
import com.matheus.servermanager.database.query.Filterable;
import com.matheus.servermanager.database.query.Operator;

public class ModelCount<M extends DatabaseModel> implements Filterable {

	@SuppressWarnings("unused")
	private final Table<M> table;
	private final Count count;

	public ModelCount(Table<M> table) {
		this.table = table;
		this.count = new Count(table.getDatabase(), table.getName());
	}

	@Override
	public ModelCount<M> where(String field, Object value) {
		return this.where(field, value, Operator.EQUALS);
	}

	@Override
	public ModelCount<M> where(String field, Object value, Operator operator) {
		return this.where(new DatabaseFilter(field, value, operator));
	}

	@Override
	public ModelCount<M> where(DatabaseFilter filter) {
		this.count.where(filter);
		return this;
	}

	public int execute() throws DatabaseException {
		return this.count.execute();
	}
}
