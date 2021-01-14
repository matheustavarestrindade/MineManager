package com.matheus.servermanager.http.modules;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.matheus.servermanager.PluginConfiguration;
import com.matheus.servermanager.http.HttpModule;
import com.matheus.servermanager.http.reqres.Request;
import com.matheus.servermanager.http.reqres.Response;
import com.matheus.servermanager.http.reqres.ResponseCode;
import com.matheus.servermanager.utils.Matematica;

public class PlayersModule extends HttpModule {

	public PlayersModule() {
		super("/players");
	}

	public void disable() {

	}

	@Override
	public void post(Request req, Response res) {
		JsonObject jsonResponse = new JsonObject();
		jsonResponse.addProperty("player_count", Bukkit.getOnlinePlayers().size());
		JsonArray online_players = new JsonArray();
		for (Player player : Bukkit.getOnlinePlayers()) {
			JsonObject online_player = new JsonObject();
			online_player.addProperty("name", player.getName());
			online_player.addProperty("level", player.getLevel());
			online_player.addProperty("gamemode", player.getGameMode().name());
			online_player.addProperty("food", Matematica.round(player.getFoodLevel(), 2));
			online_player.addProperty("health", Matematica.round(player.getHealth(), 2));
			online_player.addProperty("world", player.getWorld().getName());
			online_player.addProperty("uuid", player.getUniqueId().toString());
			online_player.addProperty("allowfly", player.getAllowFlight());
			online_players.add(online_player);
		}
		jsonResponse.add("online_players", online_players);
		jsonResponse.add("custom_player_commands", PluginConfiguration.CUSTOM_COMMANDS_ARRAY);

		String response = jsonResponse.toString();
		res.sendText(response, ResponseCode.SUCCESS);
	}

}
