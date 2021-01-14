package com.matheus.servermanager.statistics.statistic;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.configuration.file.YamlConfiguration;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.matheus.servermanager.statistics.Statistic;
import com.matheus.servermanager.utils.Matematica;
import com.matheus.servermanager.utils.TPSUtils;
import com.matheus.servermanager.utils.TimeUtils;

public class ServerStatistics implements Statistic {

	private HashMap<Integer, Double> MaxTpsByHour = new HashMap<Integer, Double>();
	private HashMap<Integer, Double> MinTpsByHour = new HashMap<Integer, Double>();
	private int last_hour = 0;

	@Override
	public void processStatistic() {

		double tps = Matematica.round(TPSUtils.getTPS(), 2);
		int hour = TimeUtils.getCurrentHour();

		if (last_hour == hour && MaxTpsByHour.containsKey(hour) && MinTpsByHour.containsKey(hour)) {
			double max_tps = MaxTpsByHour.get(hour);
			if (tps > max_tps) {
				MaxTpsByHour.put(hour, tps);
			}
			double min_tps = MinTpsByHour.get(hour);
			if (tps < min_tps) {
				MinTpsByHour.put(hour, tps);
			}
		} else {
			last_hour = hour;
			MinTpsByHour.put(hour, tps);
			MaxTpsByHour.put(hour, tps);
		}

	}

	@Override
	public void saveStatistic(YamlConfiguration config) {
		JsonArray max = new JsonArray();

		for (Entry<Integer, Double> entry : MaxTpsByHour.entrySet()) {
			JsonObject hour = new JsonObject();
			hour.addProperty("hour", entry.getKey());
			hour.addProperty("tps", entry.getValue());
			max.add(hour);
		}

		config.set("max_tps_statistics", max.toString());
		JsonArray min = new JsonArray();

		for (Entry<Integer, Double> entry : MinTpsByHour.entrySet()) {
			JsonObject hour = new JsonObject();
			hour.addProperty("hour", entry.getKey());
			hour.addProperty("tps", entry.getValue());
			min.add(hour);
		}

		config.set("min_tps_statistics", min.toString());
	}

	@Override
	public void loadStatistics(YamlConfiguration config) {
		if (config.isSet("max_tps_statistics")) {
			JsonParser parser = new JsonParser();
			JsonElement element = parser.parse(config.getString("max_tps_statistics"));
			JsonArray arr = element.getAsJsonArray();
			arr.forEach(obj -> {
				JsonObject json = (JsonObject) obj;
				int hour = json.get("hour").getAsInt();
				double tps = json.get("tps").getAsDouble();
				MaxTpsByHour.put(hour, tps);
			});
		}
		if (config.isSet("min_tps_statistics")) {
			JsonParser parser = new JsonParser();
			JsonElement element = parser.parse(config.getString("min_tps_statistics"));
			JsonArray arr = element.getAsJsonArray();
			arr.forEach(obj -> {
				JsonObject json = (JsonObject) obj;
				int hour = json.get("hour").getAsInt();
				double tps = json.get("tps").getAsDouble();
				MinTpsByHour.put(hour, tps);
			});
		}

		int hour = TimeUtils.getCurrentHour();
		last_hour = hour;
	}

	@Override
	public JsonObject getWebJSON() {
		String formated_web_max = "[";
		String formated_web_min = "[";
		for (int i = 0; i < 24; i++) {
			double max_tps = 0;
			double min_tps = 0;
			if (MaxTpsByHour.containsKey(i)) {
				max_tps = MaxTpsByHour.get(i);
			}
			if (MinTpsByHour.containsKey(i)) {
				min_tps = MinTpsByHour.get(i);
			}

			formated_web_max += String.valueOf(max_tps);
			formated_web_min += String.valueOf(min_tps);

			if (i == 23) {
				formated_web_max += "]";
				formated_web_min += "]";
			} else {
				formated_web_max += ",";
				formated_web_min += ",";
			}
		}
		JsonObject response = new JsonObject();
		response.addProperty("max_tps_statistics", formated_web_max);
		response.addProperty("min_tps_statistics", formated_web_min);
		return response;
	}

}
