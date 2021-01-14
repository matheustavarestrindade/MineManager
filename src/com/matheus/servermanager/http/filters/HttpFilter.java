package com.matheus.servermanager.http.filters;

import com.matheus.servermanager.http.reqres.Request;
import com.matheus.servermanager.http.reqres.Response;

public abstract class HttpFilter {

	private String context;

	public void Filter(String context) {
		this.context = context;
	}

	public abstract boolean doFilter(Request req, Response res);

	public String getContext() {
		return context;
	}
}
