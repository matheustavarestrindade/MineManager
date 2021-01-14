package com.matheus.servermanager.http.pages;

import com.matheus.servermanager.PluginConfiguration;
import com.matheus.servermanager.http.HttpModule;
import com.matheus.servermanager.http.HttpPageRender;
import com.matheus.servermanager.http.reqres.Request;
import com.matheus.servermanager.http.reqres.Response;

public class LoginPage extends HttpModule {

	public LoginPage() {
		super("/login");
	}

	@Override
	public void get(Request req, Response res) {
		HttpPageRender render = new HttpPageRender("login.html");
		render.setParameter("server_name", PluginConfiguration.SERVER_NAME);
		render.setParameter("error_message", "");
		res.renderPage(render);
	}

	@Override
	public void post(Request req, Response res) {
		if (!req.existQueryParameter("password")) {
			HttpPageRender render = new HttpPageRender("login.html");
			render.setParameter("server_name", PluginConfiguration.SERVER_NAME);
			render.setParameter("error_message", "Invalid Password!");
			res.renderPage(render);
			return;
		}

		if (!req.getQueryParameter("password").equals(PluginConfiguration.SERVER_PASSWORD)) {
			HttpPageRender render = new HttpPageRender("login.html");
			render.setParameter("server_name", PluginConfiguration.SERVER_NAME);
			render.setParameter("error_message", "Invalid Password!");
			res.renderPage(render);
			return;
		}

		req.getSession().setAttribute("logged", true);
		res.redirect("/dashboard");
		return;
	}

}