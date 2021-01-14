package com.matheus.servermanager.http.modules;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.gson.JsonObject;
import com.matheus.servermanager.Main;
import com.matheus.servermanager.PluginConfiguration;
import com.matheus.servermanager.http.HttpModule;
import com.matheus.servermanager.http.reqres.Request;
import com.matheus.servermanager.http.reqres.Response;
import com.matheus.servermanager.http.reqres.ResponseCode;
import com.matheus.servermanager.utils.CustomCommand;

public class InternalCommandsModule extends HttpModule {

	JsonObject error = new JsonObject();

	public InternalCommandsModule() {
		super("/internal");
		error.addProperty("error", "Internal command not found");
	}

	@Override
	public void post(Request request, Response res) {

		JsonObject jsonResponse = new JsonObject();
		String response = error.toString();

		String cmd = request.existQueryParameter("command") ? request.getQueryParameter("command") : null;
		if (cmd != null) {
			if (cmd.equalsIgnoreCase("clearmobs")) {
				int number = ClearMobs();
				jsonResponse.addProperty("success", true);
				jsonResponse.addProperty("msg", "The server has cleared " + number + " Mobs!");
				response = jsonResponse.toString();
			} else if (cmd.equalsIgnoreCase("clearanimals")) {
				int number = ClearAnimals();
				jsonResponse.addProperty("success", true);
				jsonResponse.addProperty("msg", "The server has cleared " + number + " animals!");
				response = jsonResponse.toString();
			} else if (cmd.equalsIgnoreCase("clearitems")) {
				int number = ClearItems();
				jsonResponse.addProperty("success", true);
				jsonResponse.addProperty("msg", "The server has cleared " + number + " items from the ground!");
				response = jsonResponse.toString();
			} else if (cmd.equalsIgnoreCase("kickPlayer")) {
				String player_name = request.existQueryParameter("playername") ? request.getQueryParameter("playername") : null;
				if (player_name != null) {
					kickPlayer(player_name);
					jsonResponse.addProperty("success", true);
					jsonResponse.addProperty("msg", "The player: " + player_name + " has been kicked from the server!");
					response = jsonResponse.toString();
				}
			} else if (cmd.equalsIgnoreCase("fillHealth")) {
				String player_name = request.existQueryParameter("playername") ? request.getQueryParameter("playername") : null;
				if (player_name != null) {
					fillHealth(player_name);
					jsonResponse.addProperty("success", true);
					jsonResponse.addProperty("msg", "The player: " + player_name + " had their helth set to max!");
					response = jsonResponse.toString();
				}
			} else if (cmd.equalsIgnoreCase("fillFood")) {
				String player_name = request.existQueryParameter("playername") ? request.getQueryParameter("playername") : null;
				if (player_name != null) {
					fillFood(player_name);
					jsonResponse.addProperty("success", true);
					jsonResponse.addProperty("msg", "The player: " + player_name + " had their food set to max!");
					response = jsonResponse.toString();
				}
			} else if (cmd.equalsIgnoreCase("giveLevel")) {
				String player_name = request.existQueryParameter("playername") ? request.getQueryParameter("playername") : null;
				if (player_name != null) {
					giveLevel(player_name);
					jsonResponse.addProperty("success", true);
					jsonResponse.addProperty("msg", "The player: " + player_name + " has gainned 1 level!");
					response = jsonResponse.toString();
				}
			} else if (cmd.equalsIgnoreCase("togglefly")) {
				String player_name = request.existQueryParameter("playername") ? request.getQueryParameter("playername") : null;
				if (player_name != null) {
					toggleFly(player_name);
					jsonResponse.addProperty("success", true);
					jsonResponse.addProperty("msg", "The player: " + player_name + " has gainned 1 level!");
					response = jsonResponse.toString();
				}
			} else if (cmd.equalsIgnoreCase("customplayercmd")) {
				String player_name = request.existQueryParameter("playername") ? request.getQueryParameter("playername") : null;
				String custom_cmd = request.existQueryParameter("customcmd") ? request.getQueryParameter("customcmd") : null;
				if (player_name != null && custom_cmd != null && PluginConfiguration.getCustomPlayerCommand(custom_cmd) != null) {
					CustomCommand c = PluginConfiguration.getCustomPlayerCommand(custom_cmd);
					runCustomCommand(c.getCmd(player_name));
					jsonResponse.addProperty("success", true);
					jsonResponse.addProperty("msg", c.getServer_msg(player_name));
					response = jsonResponse.toString();
				}
			} else if (cmd.equalsIgnoreCase("disableplugin")) {
				String plugin_name = request.existQueryParameter("pluginname") ? request.getQueryParameter("pluginname") : null;
				if (plugin_name != null) {
					disablePlugin(plugin_name);
					jsonResponse.addProperty("success", true);
					jsonResponse.addProperty("msg", "The plugin: " + plugin_name + " has been disabled!");
					response = jsonResponse.toString();
				}

			} else if (cmd.equalsIgnoreCase("enableplugin")) {
				String plugin_name = request.existQueryParameter("pluginname") ? request.getQueryParameter("pluginname") : null;
				if (plugin_name != null) {
					enablePlugin(plugin_name);
					jsonResponse.addProperty("success", true);
					jsonResponse.addProperty("msg", "The plugin: " + plugin_name + " has been enabled!");
					response = jsonResponse.toString();
				}
			} else if (cmd.equalsIgnoreCase("reloadplugin")) {
				String plugin_name = request.existQueryParameter("pluginname") ? request.getQueryParameter("pluginname") : null;
				if (plugin_name != null) {
					reloadPlugin(plugin_name);
					jsonResponse.addProperty("success", true);
					jsonResponse.addProperty("msg", "The plugin: " + plugin_name + " has been reloaded!");
					response = jsonResponse.toString();
				}
			}
		}
		res.sendText(response, ResponseCode.SUCCESS);
	}

	public void disable() {

	}

	private void runCustomCommand(String command) {
		new BukkitRunnable() {
			public void run() {
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
			}
		}.runTask(Main.getPlugin());
	}

	private void disablePlugin(String plugin_name) {
		new BukkitRunnable() {
			public void run() {
				Bukkit.getPluginManager().disablePlugin(Bukkit.getPluginManager().getPlugin(plugin_name));
			}
		}.runTask(Main.getPlugin());
	}

	private void enablePlugin(String plugin_name) {
		new BukkitRunnable() {
			public void run() {
				if (!Bukkit.getPluginManager().getPlugin(plugin_name).isEnabled()) {
					Bukkit.getPluginManager().enablePlugin(Bukkit.getPluginManager().getPlugin(plugin_name));
				}
			}
		}.runTask(Main.getPlugin());
	}

	private void reloadPlugin(String plugin_name) {
		new BukkitRunnable() {
			public void run() {
				if (Bukkit.getPluginManager().getPlugin(plugin_name).isEnabled()) {
					Bukkit.getPluginManager().disablePlugin(Bukkit.getPluginManager().getPlugin(plugin_name));
					Bukkit.getPluginManager().enablePlugin(Bukkit.getPluginManager().getPlugin(plugin_name));
				} else {
					Bukkit.getPluginManager().enablePlugin(Bukkit.getPluginManager().getPlugin(plugin_name));
				}
			}
		}.runTask(Main.getPlugin());
	}

	private void toggleFly(String player_name) {
		new BukkitRunnable() {
			public void run() {
				Player player = Bukkit.getPlayer(player_name);
				if (player != null && player.isOnline()) {
					player.setAllowFlight(!player.getAllowFlight());
				}
			}
		}.runTask(Main.getPlugin());
	}

	private void giveLevel(String player_name) {
		new BukkitRunnable() {
			public void run() {
				Player player = Bukkit.getPlayer(player_name);
				if (player != null && player.isOnline()) {
					player.setLevel(player.getLevel() + 1);
				}
			}
		}.runTask(Main.getPlugin());
	}

	private void fillFood(String player_name) {
		new BukkitRunnable() {
			public void run() {
				Player player = Bukkit.getPlayer(player_name);
				if (player != null && player.isOnline()) {
					player.setFoodLevel(20);
				}
			}
		}.runTask(Main.getPlugin());
	}

	private void fillHealth(String player_name) {
		new BukkitRunnable() {
			@SuppressWarnings("deprecation")
			public void run() {
				Player player = Bukkit.getPlayer(player_name);
				if (player != null && player.isOnline()) {
					player.setHealth(player.getMaxHealth());
				}
			}
		}.runTask(Main.getPlugin());
	}

	private void kickPlayer(String player_name) {
		new BukkitRunnable() {
			public void run() {
				Player player = Bukkit.getPlayer(player_name);
				if (player != null && player.isOnline()) {
					player.kickPlayer("You has ben kicked by console!");
				}
			}
		}.runTask(Main.getPlugin());
	}

	private int ClearAnimals() {
		int qtd = 0;
		for (World w : Bukkit.getServer().getWorlds()) {
			for (Entity e : w.getEntities()) {
				if (e instanceof Animals) {
					e.remove();
					qtd++;
				}
			}
		}
		return qtd;
	}

	private int ClearItems() {
		int qtd = 0;
		for (World w : Bukkit.getServer().getWorlds()) {
			for (Entity e : w.getEntities()) {
				if (e instanceof Item) {
					e.remove();
					qtd++;
				}
			}
		}
		return qtd;
	}

	private int ClearMobs() {
		int qtd = 0;
		for (World w : Bukkit.getServer().getWorlds()) {
			for (Entity e : w.getEntities()) {
				if (e instanceof Monster) {
					e.remove();
					qtd++;
				}
			}
		}
		return qtd;
	}

}
