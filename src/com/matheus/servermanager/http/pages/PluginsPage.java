package com.matheus.servermanager.http.pages;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.matheus.servermanager.PluginConfiguration;
import com.matheus.servermanager.http.HttpModule;
import com.matheus.servermanager.http.HttpPageRender;
import com.matheus.servermanager.http.reqres.Request;
import com.matheus.servermanager.http.reqres.Response;
import com.matheus.servermanager.http.reqres.ResponseCode;

public class PluginsPage extends HttpModule {

	public PluginsPage() {
		super("/plugins");
	}

	private ArrayList<Plugin> getPlugins() {

		ArrayList<Plugin> plugins = new ArrayList<Plugin>();

		for (Plugin pl : Bukkit.getPluginManager().getPlugins()) {
			plugins.add(pl);
		}

		return plugins;
	}

	@Override
	public void post(Request req, Response res) {
		// TODO Auto-generated method stub
		JsonObject jsonResponse = new JsonObject();

		JsonArray plugins = new JsonArray();
		for (Plugin pl : getPlugins()) {
			JsonObject plugin_info = new JsonObject();
			plugin_info.addProperty("name", pl.getName());
			plugin_info.addProperty("active", pl.isEnabled());
			plugins.add(plugin_info);
		}
		jsonResponse.add("plugins", plugins);
		String response = jsonResponse.toString();

		res.sendText(response, ResponseCode.SUCCESS);
	}

	@Override
	public void get(Request req, Response res) {
		HttpPageRender dashboard = new HttpPageRender("plugins.html");
		dashboard.addSubPageRender("sidebar", "sidebar.html");
		dashboard.setParameter("server_name", PluginConfiguration.SERVER_NAME);
		res.renderPage(dashboard);
	}
}