package com.matheus.servermanager;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.matheus.servermanager.http.HttpRestServer;
import com.matheus.servermanager.http.filters.LoginFilter;
import com.matheus.servermanager.http.modules.CPUModule;
import com.matheus.servermanager.http.modules.InternalCommandsModule;
import com.matheus.servermanager.http.modules.LogModule;
import com.matheus.servermanager.http.modules.PlayersModule;
import com.matheus.servermanager.http.modules.RamModule;
import com.matheus.servermanager.http.modules.StatisticsModule;
import com.matheus.servermanager.http.modules.TPSModule;
import com.matheus.servermanager.http.pages.ConsolePage;
import com.matheus.servermanager.http.pages.DashBoardPage;
import com.matheus.servermanager.http.pages.LoginPage;
import com.matheus.servermanager.http.pages.LogoutPage;
import com.matheus.servermanager.http.pages.PerformancePage;
import com.matheus.servermanager.http.pages.PluginsPage;
import com.matheus.servermanager.http.pages.UsersPage;
import com.matheus.servermanager.statistics.StatisticType;
import com.matheus.servermanager.statistics.StatisticsManager;
import com.matheus.servermanager.statistics.statistic.PlayersStatistics;

public class Main extends JavaPlugin {

	private static HttpRestServer server;
	private static Plugin pl;
	private static StatisticsManager stManager;

	public void onEnable() {
		pl = this;
		new PluginConfiguration(new File(pl.getDataFolder() + File.separator + "configuration.yml"));

		server = new HttpRestServer(PluginConfiguration.SERVER_PORT);

		server.addFilter(new LoginFilter());

		server.registerPublicFileDirectory(new File(pl.getDataFolder() + File.separator + "html" + File.separator));

		server.addModule(new LoginPage());
		server.addModule(new DashBoardPage());
		server.addModule(new UsersPage());
		server.addModule(new LogoutPage());
		server.addModule(new ConsolePage());
		server.addModule(new PluginsPage());
		server.addModule(new PerformancePage());

		server.addModule(new PlayersModule());
		server.addModule(new InternalCommandsModule());
		server.addModule(new LoginPage());
		server.addModule(new StatisticsModule());
		server.addModule(new RamModule());
		server.addModule(new TPSModule());
		server.addModule(new CPUModule());
		server.addModule(new LogModule());

		stManager = new StatisticsManager(new File(pl.getDataFolder() + File.separator + "statistics.yml"));

		Bukkit.getPluginManager().registerEvents((PlayersStatistics) stManager.getStatistic(StatisticType.PLAYERS), this);

	}

	public void onDisable() {

		server.stopServer();
		stManager.stop();
		PluginConfiguration.unload();
	}

	public static StatisticsManager getStatisticsManager() {
		return stManager;
	}

	public static Plugin getPlugin() {
		return pl;
	}

}
