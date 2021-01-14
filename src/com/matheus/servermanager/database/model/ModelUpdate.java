package com.matheus.servermanager.database.model;

import com.matheus.servermanager.database.database.DatabaseException;
import com.matheus.servermanager.database.query.DatabaseFilter;
import com.matheus.servermanager.database.query.Filterable;
import com.matheus.servermanager.database.query.Operator;
import com.matheus.servermanager.database.query.Update;

public class ModelUpdate<M extends DatabaseModel> implements Filterable {

	@SuppressWarnings("unused")
	private final Table<M> table;
	private final Update update;

	public ModelUpdate(Table<M> table) {
		this.table = table;
		this.update = new Update(table.getDatabase(), table.getName());
	}

	public ModelUpdate<M> set(String field, Object value) {
		this.update.set(field, value);
		return this;
	}

	@Override
	public ModelUpdate<M> where(String field, Object value) {
		return this.where(field, value, Operator.EQUALS);
	}

	@Override
	public ModelUpdate<M> where(String field, Object value, Operator operator) {
		return this.where(new DatabaseFilter(field, value, operator));
	}

	@Override
	public ModelUpdate<M> where(DatabaseFilter filter) {
		this.update.where(filter);
		return this;
	}

	public void execute() throws DatabaseException {
		this.update.execute();
	}
}
