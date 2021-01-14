package com.matheus.servermanager.database.model;

import com.matheus.servermanager.database.database.DatabaseException;
import com.matheus.servermanager.database.query.DatabaseFilter;
import com.matheus.servermanager.database.query.Delete;
import com.matheus.servermanager.database.query.Filterable;
import com.matheus.servermanager.database.query.Operator;

public class ModelDelete<M extends DatabaseModel> implements Filterable {

	@SuppressWarnings("unused")
	private final Table<M> table;
	private final Delete delete;

	public ModelDelete(Table<M> table) {
		this.table = table;
		this.delete = new Delete(table.getDatabase(), table.getName());
	}

	@Override
	public ModelDelete<M> where(String field, Object value) {
		return this.where(field, value, Operator.EQUALS);
	}

	@Override
	public ModelDelete<M> where(String field, Object value, Operator operator) {
		return this.where(new DatabaseFilter(field, value, operator));
	}

	@Override
	public ModelDelete<M> where(DatabaseFilter filter) {
		this.delete.where(filter);
		return this;
	}

	public void execute() throws DatabaseException {
		this.delete.execute();
	}
}
