package com.matheus.servermanager.http.modules;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.gson.JsonObject;
import com.matheus.servermanager.Main;
import com.matheus.servermanager.PluginConfiguration;
import com.matheus.servermanager.http.HttpModule;
import com.matheus.servermanager.http.reqres.Request;
import com.matheus.servermanager.http.reqres.Response;
import com.matheus.servermanager.http.reqres.ResponseCode;

public class ConsoleModule extends HttpModule {

	private JsonObject command__null_error = new JsonObject();

	public ConsoleModule() {
		super("/console");
		command__null_error.addProperty("error", "Command is Null");
	}

	@Override
	public void post(Request req, Response res) {
		if (!PluginConfiguration.SERVER_ACCEPT_COMMANDS) {
			res.sendText("{\"error\":\"Commands has been disabled on the MineManager!\"}", ResponseCode.BAD_REQUEST);
			return;
		}
		JsonObject jsonResponse = new JsonObject();
		String cmd = req.existQueryParameter("command") ? req.getQueryParameter("command") : null;
		String response = "teste";
		if (cmd == null) {
			response = command__null_error.toString();
		} else {
			new BukkitRunnable() {
				public void run() {
					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd);
				}
			}.runTask(Main.getPlugin());
			jsonResponse.addProperty("success", "Command sent!");
			response = jsonResponse.toString();
		}
		res.sendText(response, ResponseCode.SUCCESS);
	}

}
