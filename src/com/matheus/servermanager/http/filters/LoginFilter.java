package com.matheus.servermanager.http.filters;

import com.matheus.servermanager.http.reqres.Request;
import com.matheus.servermanager.http.reqres.Response;

public class LoginFilter extends HttpFilter {

	@Override
	public boolean doFilter(Request req, Response res) {
		if (req.getRequestURI().equalsIgnoreCase("/login") && req.getSession().getAttribute("logged") == null) {
			return true;
		} else if (req.getRequestURI().equalsIgnoreCase("/login") && req.getSession().getAttribute("logged") != null) {
			res.redirect("/dashboard");
			return false;
		} else if (!req.getRequestURI().equalsIgnoreCase("/login") && req.getSession().getAttribute("logged") != null) {
			return true;
		} else {
			res.redirect("/login");
			return false;
		}
	}

}
