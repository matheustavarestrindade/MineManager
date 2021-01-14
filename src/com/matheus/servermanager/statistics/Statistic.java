package com.matheus.servermanager.statistics;

import org.bukkit.configuration.file.YamlConfiguration;

import com.google.gson.JsonObject;

public interface Statistic {

	public void processStatistic();

	public void loadStatistics(YamlConfiguration config);

	public void saveStatistic(YamlConfiguration config);

	public JsonObject getWebJSON();

}
