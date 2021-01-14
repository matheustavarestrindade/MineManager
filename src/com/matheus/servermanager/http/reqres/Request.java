package com.matheus.servermanager.http.reqres;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.matheus.servermanager.http.HttpSession;
import com.sun.net.httpserver.HttpExchange;

public class Request {

	private HttpSession session;
	private HashMap<String, String> cookies;
	private Map<String, List<String>> queryParameters = new HashMap<String, List<String>>();
	private HttpExchange exchange;
	private HashMap<String, Integer> params;

	public Request(HttpExchange exchange, HttpSession session, HashMap<String, String> cookies,
																HashMap<String, Integer> params) {
		String query = null;
		this.exchange = exchange;
		this.params = params;
		if (exchange.getRequestMethod().equalsIgnoreCase("GET")) {
			if (exchange.getRequestURI().getRawQuery() != null) query = exchange.getRequestURI().getRawQuery();
		} else if (exchange.getRequestBody() != null) {
			StringBuilder sb = new StringBuilder();
			InputStream ios = exchange.getRequestBody();
			int i;
			try {
				while ((i = ios.read()) != -1) {
					sb.append((char) i);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			query = sb.toString();
		}
		Map<String, List<String>> splited = splitQuery(query);
		if (splited != null) {
			queryParameters.putAll(splited);
		}
		this.session = session;
		this.cookies = cookies;
	}

	public HttpSession getSession() {
		return session;
	}

	public String getCookie(String key) {
		if (!cookies.containsKey(key)) {
			return null;
		}
		return cookies.get(key);
	}

	public void enableCORS() {
		exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
		exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST");
		exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Origin, Content-Type, Accept");
	}

	public boolean existURLParameter(String key) {
		if (params.containsKey(key)) {
			int index = params.get(key);
			if (exchange.getRequestURI().toString().split("/").length > index) {
				return true;
			}
		}
		return false;
	}

	public String getRequestURI() {
		return exchange.getRequestURI().toString();
	}

	public String getURLParameter(String key) {
		if (params.containsKey(key)) {
			int index = params.get(key);
			if (exchange.getRequestURI().toString().split("/").length > index) {
				return exchange.getRequestURI().toString().split("/")[index];
			}
		}
		return "";
	}

	public String getQueryParameter(String key) {
		return queryParameters.get(key).get(0);
	}

	public boolean existQueryParameter(String key) {
		return queryParameters.containsKey(key) && queryParameters.get(key).size() > 0;
	}

	private static Map<String, List<String>> splitQuery(String query) {
		try {
			if (query == null) {
				return new HashMap<String, List<String>>();
			}
			final Map<String, List<String>> query_pairs = new LinkedHashMap<String, List<String>>();
			final String[] pairs = query.split("&");
			for (String pair : pairs) {
				final int idx = pair.indexOf("=");
				final String key = idx > 0 ? URLDecoder.decode(pair.substring(0, idx), "UTF-8") : pair;
				if (!query_pairs.containsKey(key)) {
					query_pairs.put(key, new LinkedList<String>());
				}
				final String value = idx > 0 && pair.length() > idx + 1 ? URLDecoder.decode(pair.substring(idx + 1), "UTF-8") : null;
				query_pairs.get(key).add(value);
			}
			return query_pairs;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

}
