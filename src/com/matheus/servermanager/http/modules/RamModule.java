package com.matheus.servermanager.http.modules;

import com.google.gson.JsonObject;
import com.matheus.servermanager.http.HttpModule;
import com.matheus.servermanager.http.reqres.Request;
import com.matheus.servermanager.http.reqres.Response;
import com.matheus.servermanager.http.reqres.ResponseCode;

public class RamModule extends HttpModule {

	private Runtime r = Runtime.getRuntime();
	private final long MB_DIV = (1024 * 1024);

	public RamModule() {
		super("/ram");
	}

	public void disable() {

	}

	@Override
	public void post(Request req, Response res) {

		long memUsed = (r.totalMemory() - r.freeMemory()) / MB_DIV;
		long freeMem = r.freeMemory() / MB_DIV;
		long totalMem = r.totalMemory() / MB_DIV;

		JsonObject jsonResponse = new JsonObject();
		jsonResponse.addProperty("free_ram", freeMem);
		jsonResponse.addProperty("used_ram", memUsed);
		jsonResponse.addProperty("total_ram", totalMem);

		String response = jsonResponse.toString();
		res.sendText(response, ResponseCode.SUCCESS);

	}

}