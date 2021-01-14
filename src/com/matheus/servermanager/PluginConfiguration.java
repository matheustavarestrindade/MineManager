package com.matheus.servermanager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.matheus.servermanager.utils.CustomCommand;

public class PluginConfiguration {

	public static String MYSQL_USER;
	public static String MYSQL_PASSWORD;
	public static String MYSQL_ADDRESS;
	public static String MYSQL_PORT;
	public static String MYSQL_DATABASE;

	public static int SERVER_PORT;
	public static String SERVER_PASSWORD;
	public static String SERVER_NAME;
	public static boolean SERVER_ACCEPT_COMMANDS;

	public static JsonArray CUSTOM_COMMANDS_ARRAY;
	private static HashMap<String, CustomCommand> CUSTOM_PLAYER_COMMANDS = new HashMap<String, CustomCommand>();

	private YamlConfiguration statistic_file = new YamlConfiguration();
	private File file;

	public PluginConfiguration(File f) {

		try {
			if (!f.exists()) {
				System.out.println("[MineManager] Creating New Configuration!");
				f.createNewFile();
				this.file = f;
				statistic_file.load(f);
				statistic_file.set("config.enable_commands_from_web_server", true);
				if (!statistic_file.isSet("custom_player_commands")) {
					statistic_file.set("custom_player_commands.kickplayer.name", "Kick Player");
					statistic_file.set("custom_player_commands.kickplayer.cmd", "kick {player} You has been kicked by MineManager");
					statistic_file.set("custom_player_commands.kickplayer.server_msg", "The Player {player} Has beed kicked successfuly");

					statistic_file.set("custom_player_commands.xp_add.name", "Give XP Level");
					statistic_file.set("custom_player_commands.xp_add.cmd", "xp add {player} 1 levels");
					statistic_file.set("custom_player_commands.xp_add.server_msg", "{player} Has been banned given 1 level");

					statistic_file.set("custom_player_commands.fill_health.name", "Fill Health");
					statistic_file.set("custom_player_commands.fill_health.cmd", "effect give {player} minecraft:instant_health 1 100");
					statistic_file.set("custom_player_commands.fill_health.server_msg", "{player} Health has been set to max");
				}

				statistic_file.options().header("The port on this configuration file needs to be open on your server so the plugin can work\n" + " - enable_commands_from_web_server - This configuration let you disable the commands sent from the WEB Server so your online panel will only be able to send requests to data display" + "\n MySQL Will soon be implemented to better data storage");

				statistic_file.set("mysql.user", "root");
				statistic_file.set("mysql.password", "");
				statistic_file.set("mysql.address", "localhost");
				statistic_file.set("mysql.port", "3306");
				statistic_file.set("mysql.database", "MineManager");

				statistic_file.set("server.port", 3000);
				statistic_file.set("server.password", UUID.randomUUID().toString());
				statistic_file.set("server.displayname", "A MineManager Server");

				statistic_file.options().copyHeader(true);
				try {
					statistic_file.save(file);
				} catch (IOException e) {
					e.printStackTrace();
				}

			} else {
				this.file = f;
				statistic_file.load(f);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}

		if (statistic_file.isSet("custom_player_commands")) {
			CUSTOM_COMMANDS_ARRAY = new JsonArray();
			for (String s : statistic_file.getConfigurationSection("custom_player_commands").getKeys(false)) {
				String cmd = statistic_file.getString("custom_player_commands." + s + ".cmd");
				String server_msg = statistic_file.getString("custom_player_commands." + s + ".server_msg");
				CustomCommand c = new CustomCommand(cmd, server_msg);

				JsonObject commandJson = new JsonObject();
				commandJson.addProperty("name", s);
				commandJson.addProperty("displayname", statistic_file.getString("custom_player_commands." + s + ".name"));

				CUSTOM_COMMANDS_ARRAY.add(commandJson);
				CUSTOM_PLAYER_COMMANDS.put(s, c);
			}
		}

		MYSQL_USER = statistic_file.getString("mysql.user");
		MYSQL_PASSWORD = statistic_file.getString("mysql.password");
		MYSQL_ADDRESS = statistic_file.getString("mysql.address");
		MYSQL_PORT = statistic_file.getString("mysql.port");
		MYSQL_DATABASE = statistic_file.getString("mysql.database");

		SERVER_PORT = statistic_file.getInt("server.port");
		SERVER_PASSWORD = statistic_file.getString("server.password");
		SERVER_NAME = statistic_file.getString("server.displayname");
		SERVER_ACCEPT_COMMANDS = statistic_file.getBoolean("config.enable_commands_from_web_server");

	}

	public static CustomCommand getCustomPlayerCommand(String command) {
		if (CUSTOM_PLAYER_COMMANDS.containsKey(command)) {
			return CUSTOM_PLAYER_COMMANDS.get(command);
		}
		return null;
	}

	public static void unload() {
		MYSQL_USER = null;
		MYSQL_PASSWORD = null;
		MYSQL_ADDRESS = null;
		MYSQL_PORT = null;
		MYSQL_DATABASE = null;

		SERVER_PORT = 0;
		SERVER_PASSWORD = null;
		SERVER_NAME = null;

		CUSTOM_COMMANDS_ARRAY = null;
		CUSTOM_PLAYER_COMMANDS = null;

	}

}