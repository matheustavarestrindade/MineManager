package com.matheus.servermanager.database.query;

public enum Operator {
	EQUALS("="), NOT_EQUAL("!="), LESS("<"), LESS_OR_EQUAL("<="), GREATER(">"), GREATER_OR_EQUAL(">="), IN("IN"),
	LIKE("LIKE"), IS("IS");

	private final String value;

	Operator(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
