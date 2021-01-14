package com.matheus.servermanager.http.modules;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.gson.JsonObject;
import com.matheus.servermanager.http.HttpModule;
import com.matheus.servermanager.http.reqres.Request;
import com.matheus.servermanager.http.reqres.Response;
import com.matheus.servermanager.http.reqres.ResponseCode;

public class LogModule extends HttpModule {

	private JsonObject error;
	private File log;

	public LogModule() {
		super("/log");
		error = new JsonObject();
		error.addProperty("error", "Error Loading the Log");

		File server_root = new File("");

		log = new File(server_root.getAbsolutePath() + "/logs/latest.log");
	}

	private String getLogContent() {
		try {
			return new String(Files.readAllBytes(Paths.get(log.getAbsolutePath())));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void disable() {

	}

	@Override
	public void post(Request req, Response res) {

		String response = getLogContent();
		if (response == null) {
			response = error.toString();
		} else {
			JsonObject jsonResponse = new JsonObject();
			jsonResponse.addProperty("log_txt", response);
			response = jsonResponse.toString();
		}
		res.sendText(response, ResponseCode.SUCCESS);
	}

}
