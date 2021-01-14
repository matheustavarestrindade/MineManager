package com.matheus.servermanager.http;

import java.util.HashMap;

public class HttpSession {

	private HashMap<String, Object> sessionContent = new HashMap<String, Object>();
	private HttpRestServer server;
	private String sessionToken;

	public HttpSession(String sessionToken, HttpRestServer server) {
		this.server = server;
		this.sessionToken = sessionToken;
	}

	public void setAttribute(String name, Object value) {
		sessionContent.put(name.toLowerCase(), value);
	}

	public Object getAttribute(String name) {
		if (!sessionContent.containsKey(name.toLowerCase())) {
			return null;
		}
		return sessionContent.get(name.toLowerCase());
	}

	public void clearSession() {
		sessionContent.clear();
	}

	public String getToken() {
		return sessionToken;
	}

	public void destroy() {
		server.destroySession(this);
	}

}
