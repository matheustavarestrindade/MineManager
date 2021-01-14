package com.matheus.servermanager.http.modules;

import org.bukkit.Bukkit;

import com.google.gson.JsonObject;
import com.matheus.servermanager.Main;
import com.matheus.servermanager.http.HttpModule;
import com.matheus.servermanager.http.reqres.Request;
import com.matheus.servermanager.http.reqres.Response;
import com.matheus.servermanager.http.reqres.ResponseCode;
import com.matheus.servermanager.utils.Matematica;
import com.matheus.servermanager.utils.TPSUtils;

public class TPSModule extends HttpModule {
	private int tps_task;

	public TPSModule() {
		super("/tps");
		tps_task = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new TPSUtils(), 100L, 1L);
	}

	public void disable() {
		Bukkit.getScheduler().cancelTask(tps_task);
	}

	@Override
	public void post(Request req, Response res) {

		JsonObject json = new JsonObject();
		json.addProperty("tps", Matematica.round(TPSUtils.getTPS(), 2));
		String response = json.toString();
		res.sendText(response, ResponseCode.SUCCESS);
	}

}
