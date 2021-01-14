package com.matheus.servermanager.http.pages;

import com.matheus.servermanager.http.HttpModule;
import com.matheus.servermanager.http.reqres.Request;
import com.matheus.servermanager.http.reqres.Response;

public class LogoutPage extends HttpModule {

	public LogoutPage() {
		super("/logout");
	}

	@Override
	public void get(Request req, Response res) {
		req.getSession().destroy();
		res.redirect("/login");
	}

}