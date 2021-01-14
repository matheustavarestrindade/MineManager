package com.matheus.servermanager.http.modules;

import com.google.gson.JsonObject;
import com.matheus.servermanager.Main;
import com.matheus.servermanager.http.HttpModule;
import com.matheus.servermanager.http.reqres.Request;
import com.matheus.servermanager.http.reqres.Response;
import com.matheus.servermanager.http.reqres.ResponseCode;
import com.matheus.servermanager.statistics.StatisticType;

public class StatisticsModule extends HttpModule {

	public StatisticsModule() {
		super("/statistics");
	}

	public void disable() {

	}

	@Override
	public void post(Request req, Response res) {
		JsonObject jsonResponse = new JsonObject();
		jsonResponse.add("server", Main.getStatisticsManager().getStatistic(StatisticType.SERVER).getWebJSON());
		jsonResponse.add("players", Main.getStatisticsManager().getStatistic(StatisticType.PLAYERS).getWebJSON());
		String response = jsonResponse.toString();
		res.sendText(response, ResponseCode.SUCCESS);
	}

}