package com.matheus.servermanager.http.pages;

import com.matheus.servermanager.PluginConfiguration;
import com.matheus.servermanager.http.HttpModule;
import com.matheus.servermanager.http.HttpPageRender;
import com.matheus.servermanager.http.reqres.Request;
import com.matheus.servermanager.http.reqres.Response;

public class DashBoardPage extends HttpModule {

	public DashBoardPage() {
		super("/dashboard");
	}

	@Override
	public void get(Request req, Response res) {

		HttpPageRender dashboard = new HttpPageRender("dashboard.html");
		dashboard.addSubPageRender("sidebar", "sidebar.html");
		dashboard.setParameter("server_name", PluginConfiguration.SERVER_NAME);
		res.renderPage(dashboard);

	}

}
