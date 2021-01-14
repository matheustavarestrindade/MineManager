package com.matheus.servermanager.statistics.statistic;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.matheus.servermanager.statistics.Statistic;
import com.matheus.servermanager.utils.TimeUtils;

public class PlayersStatistics implements Statistic, Listener {

	private HashMap<Integer, Integer> OnlinePlayersByHour = new HashMap<Integer, Integer>();
	private HashMap<Integer, Integer> LoginsByHour = new HashMap<Integer, Integer>();

	private int last_hour = 0;

	@EventHandler
	public void onLogin(PlayerLoginEvent e) {
		int hour = TimeUtils.getCurrentHour();
		if (last_hour == hour && LoginsByHour.containsKey(hour)) {
			int players = LoginsByHour.get(hour) + 1;
			LoginsByHour.put(hour, players);
		} else {
			last_hour = hour;
			LoginsByHour.put(hour, 1);
		}
	}

	@Override
	public void processStatistic() {

		int online_players = Bukkit.getOnlinePlayers().size();
		int hour = TimeUtils.getCurrentHour();

		if (last_hour == hour && OnlinePlayersByHour.containsKey(hour)) {
			int max_online = OnlinePlayersByHour.get(hour);
			if (online_players > max_online) {
				OnlinePlayersByHour.put(hour, online_players);
			}
		} else {
			last_hour = hour;
			OnlinePlayersByHour.put(hour, online_players);
		}

	}

	@Override
	public void saveStatistic(YamlConfiguration config) {
		JsonArray arr = new JsonArray();

		for (Entry<Integer, Integer> entry : OnlinePlayersByHour.entrySet()) {
			JsonObject hour = new JsonObject();
			hour.addProperty("hour", entry.getKey());
			hour.addProperty("players", entry.getValue());
			arr.add(hour);
		}

		config.set("player_statistics", arr.toString());

		JsonArray arr2 = new JsonArray();

		for (Entry<Integer, Integer> entry : LoginsByHour.entrySet()) {
			JsonObject hour = new JsonObject();
			hour.addProperty("hour", entry.getKey());
			hour.addProperty("players", entry.getValue());
			arr2.add(hour);
		}

		config.set("player_statistics_login", arr2.toString());
	}

	@Override
	public void loadStatistics(YamlConfiguration config) {
		if (config.isSet("player_statistics")) {
			JsonParser parser = new JsonParser();
			JsonElement element = parser.parse(config.getString("player_statistics"));
			JsonArray arr = element.getAsJsonArray();
			arr.forEach(obj -> {
				JsonObject json = (JsonObject) obj;
				int hour = json.get("hour").getAsInt();
				int players = json.get("players").getAsInt();
				OnlinePlayersByHour.put(hour, players);
			});
		}
		if (config.isSet("player_statistics_login")) {
			JsonParser parser = new JsonParser();
			JsonElement element = parser.parse(config.getString("player_statistics_login"));
			JsonArray arr = element.getAsJsonArray();
			arr.forEach(obj -> {
				JsonObject json = (JsonObject) obj;
				int hour = json.get("hour").getAsInt();
				int players = json.get("players").getAsInt();
				LoginsByHour.put(hour, players);
			});
		}
		int hour = TimeUtils.getCurrentHour();
		last_hour = hour;
	}

	@Override
	public JsonObject getWebJSON() {
		String formated_web = "[";
		String formated_web_logins = "[";

		for (int i = 0; i < 24; i++) {
			int online_players = 0;
			if (OnlinePlayersByHour.containsKey(i)) {
				online_players = OnlinePlayersByHour.get(i);
			}
			int logins = 0;
			if (LoginsByHour.containsKey(i)) {
				logins = LoginsByHour.get(i);
			}
			formated_web_logins += String.valueOf(logins);
			formated_web += String.valueOf(online_players);
			if (i == 23) {
				formated_web_logins += "]";
				formated_web += "]";
			} else {
				formated_web_logins += ",";
				formated_web += ",";
			}
		}
		JsonObject response = new JsonObject();
		response.addProperty("max_players_statistic", formated_web);
		response.addProperty("players_login_statistic", formated_web_logins);
		return response;
	}

}
