package com.matheus.servermanager.http;

import com.matheus.servermanager.http.reqres.Request;
import com.matheus.servermanager.http.reqres.Response;
import com.matheus.servermanager.http.reqres.ResponseCode;

public abstract class HttpModule {

	private String context;

	public HttpModule(String context) {
		this.context = context;
	}

	public void get(Request req, Response res) {
		res.sendCode(ResponseCode.NOT_FOUND);
	}

	public void post(Request req, Response res) {
		res.sendCode(ResponseCode.NOT_FOUND);
	}

	public void put(Request req, Response res) {
		res.sendCode(ResponseCode.NOT_FOUND);
	}

	public void update(Request req, Response res) {
		res.sendCode(ResponseCode.NOT_FOUND);
	}

	public void disable() {

	}

	public String getContext() {
		return context;
	}

}
