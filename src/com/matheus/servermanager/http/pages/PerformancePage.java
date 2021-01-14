package com.matheus.servermanager.http.pages;

import com.matheus.servermanager.PluginConfiguration;
import com.matheus.servermanager.http.HttpModule;
import com.matheus.servermanager.http.HttpPageRender;
import com.matheus.servermanager.http.reqres.Request;
import com.matheus.servermanager.http.reqres.Response;

public class PerformancePage extends HttpModule {

	public PerformancePage() {
		super("/performance");
	}

	@Override
	public void get(Request req, Response res) {

		HttpPageRender dashboard = new HttpPageRender("performance.html");
		dashboard.addSubPageRender("sidebar", "sidebar.html");
		dashboard.setParameter("server_name", PluginConfiguration.SERVER_NAME);
		res.renderPage(dashboard);

	}
}