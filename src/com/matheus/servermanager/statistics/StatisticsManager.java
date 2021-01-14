package com.matheus.servermanager.statistics;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.matheus.servermanager.Main;
import com.matheus.servermanager.statistics.statistic.PlayersStatistics;
import com.matheus.servermanager.statistics.statistic.ServerStatistics;
import com.matheus.servermanager.utils.TimeUtils;

public class StatisticsManager {

	private YamlConfiguration statistic_file = new YamlConfiguration();
	private File file;
	private BukkitTask task;
	private HashMap<StatisticType, Statistic> statistics = new HashMap<StatisticType, Statistic>();

	public StatisticsManager(File f) {
		try {
			if (!f.exists()) {
				f.createNewFile();
			}
			this.file = f;
			statistic_file.load(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}

		statistics.put(StatisticType.PLAYERS, new PlayersStatistics());
		statistics.put(StatisticType.SERVER, new ServerStatistics());

		for (Statistic s : statistics.values()) {
			s.loadStatistics(statistic_file);
		}

		task = new BukkitRunnable() {
			int lastHour = TimeUtils.getCurrentHour();

			@Override
			public void run() {
				for (Statistic s : statistics.values()) {
					if (lastHour != TimeUtils.getCurrentHour()) {
						Main.getStatisticsManager().saveAll();
					}
					s.processStatistic();
				}
			}

		}.runTaskTimerAsynchronously(Main.getPlugin(), 20l, 60 * 20l);

	}

	private void saveAll() {
		for (Statistic s : statistics.values()) {
			s.saveStatistic(statistic_file);
		}
		try {
			statistic_file.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void stop() {

		saveAll();
		task.cancel();

	}

	public Statistic getStatistic(StatisticType type) {
		return statistics.get(type);
	}

}
