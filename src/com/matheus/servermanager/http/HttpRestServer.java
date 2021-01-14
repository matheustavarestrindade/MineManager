package com.matheus.servermanager.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;

import com.matheus.servermanager.http.filters.HttpFilter;
import com.matheus.servermanager.http.reqres.Request;
import com.matheus.servermanager.http.reqres.Response;
import com.matheus.servermanager.utils.TokenUtils;
import com.sun.net.httpserver.HttpServer;

public class HttpRestServer {

	private HttpServer server;
	private ArrayList<HttpModule> modules = new ArrayList<HttpModule>();
	private ArrayList<HttpFilter> filters = new ArrayList<HttpFilter>();
	private HashMap<String, HttpSession> sessions = new HashMap<String, HttpSession>();
	private File basePath;

	public HttpRestServer(int port) {
		try {
			System.out.println("Starting Server on port: " + port);
			server = HttpServer.create(new InetSocketAddress(port), 0);
			server.setExecutor(null);
			server.start();
			System.out.println("Started Server on port: " + port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addFilter(HttpFilter filter) {
		filters.add(filter);
	}

	public void addModule(HttpModule module) {
		System.out.println("Registering module on context: " + module.getContext());
		HashMap<String, Integer> params = new HashMap<String, Integer>();
		if (module.getContext().contains(":")) {
			String currentParam = "";
			boolean start = false;
			int index = 0;
			for (char c : module.getContext().toCharArray()) {
				if (c == ':') {
					start = true;
				} else if (c == '/') {
					start = false;
					params.put(currentParam, index);
					currentParam = "";
					index++;
					continue;
				} else if (start) {
					currentParam += c;
				}
			}
		}

		server.createContext(module.getContext(), (exchange -> {

			HashMap<String, String> cookies = new HashMap<String, String>();
			if (exchange.getRequestHeaders().containsKey("Cookie")) {
				for (String cookie : exchange.getRequestHeaders().get("Cookie")) {
					if (cookie.split("=").length > 1) {
						String cookie_name = cookie.split("=", 2)[0];
						String cookie_value = cookie.split("=", 2)[1];
						cookies.put(cookie_name, cookie_value);
					}
				}
			}

			String token = cookies.containsKey("HTTP_SERVER_SESSION_TOKEN") ? cookies.get("HTTP_SERVER_SESSION_TOKEN") : null;

			HttpSession session;
			if (token == null || !sessions.containsKey(token)) {
				token = TokenUtils.generateToken();
				session = new HttpSession(token, this);
				sessions.put(token, session);
				exchange.getResponseHeaders().add("Set-Cookie", "HTTP_SERVER_SESSION_TOKEN=" + token);
				System.out.println("[MineManager] New WebSession started on ip: " + exchange.getRemoteAddress().getAddress().getHostAddress());
			} else {
				exchange.getResponseHeaders().add("Set-Cookie", "HTTP_SERVER_SESSION_TOKEN=" + token);
				session = sessions.get(token);
			}

			Request req = new Request(exchange, session, cookies, params);
			Response res = new Response(exchange, basePath);

			for (HttpFilter filter : filters) {
				if (!filter.doFilter(req, res)) {
					return;
				}
			}

			if (exchange.getRequestMethod().equalsIgnoreCase("GET")) {
				module.get(req, res);
			} else if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {
				module.post(req, res);
			} else if (exchange.getRequestMethod().equalsIgnoreCase("PUT")) {
				module.put(req, res);
			} else if (exchange.getRequestMethod().equalsIgnoreCase("UPDATE")) {
				module.update(req, res);
			}
		}));
		modules.add(module);
	}

	protected void destroySession(HttpSession session) {
		if (sessions.containsKey(session.getToken())) {
			sessions.remove(session.getToken());
		}
	}

	// Will be registered to /public
	public void registerPublicFileDirectory(File folderPath) {
		basePath = folderPath;
		server.createContext("/public", (exchange -> {
			String fileId = exchange.getRequestURI().getPath().replace("/public", "");
			File file = new File(folderPath.getAbsolutePath() + fileId);
			if (!file.exists()) {
				String response = "Error 404 File not found.";
				exchange.sendResponseHeaders(404, response.length());
				OutputStream output = exchange.getResponseBody();
				output.write(response.getBytes());
				output.flush();
				output.close();
			} else {
				exchange.sendResponseHeaders(200, 0);
				OutputStream output = exchange.getResponseBody();
				FileInputStream fs = new FileInputStream(file);
				final byte[] buffer = new byte[0x10000];
				int count = 0;
				while ((count = fs.read(buffer)) >= 0) {
					output.write(buffer, 0, count);
				}
				output.flush();
				output.close();
				fs.close();
			}
		}));
	}

	public void stopServer() {
		server.stop(0);
		for (HttpModule m : modules) {
			m.disable();
		}
	}

}
