package com.matheus.servermanager.database.query;

public interface Filterable {

	Filterable where(String field, Object value);

	Filterable where(String field, Object value, Operator operator);

	Filterable where(DatabaseFilter filter);
}
