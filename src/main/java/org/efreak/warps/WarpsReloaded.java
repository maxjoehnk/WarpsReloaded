package org.efreak.warps;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;
import org.efreak.warps.commands.*;
import org.efreak.warps.databases.*;
import org.efreak.warps.help.HelpManager;

import com.turt2live.metrics.EMetrics;

public class WarpsReloaded extends JavaPlugin {

	private static IOManager io;
	private static Configuration config;
	private static Database db;
	private static JavaPlugin instance;
	private static EMetrics metrics;
	private static HashMap<String, Warp> warps;
	
	@Override
	public void onLoad() {
		instance = this;
	}
	
	@Override
	public void onEnable() {
		new File(getDataFolder(), "lang").mkdirs();
		config = new Configuration();
		io = new IOManager();
		config.init();
		io.init();
		if (config.getDebug()) io.sendConsoleWarning(io.translate("Plugin.Debug"));
		try {
			metrics = new EMetrics(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
		new Permissions().init();
		Database.registerDatabaseSystem("MySQL", new MySQL());
		Database.registerDatabaseSystem("SQLite", new SQLite());
		db = Database.getDatabaseBySystem(config.getString("Database.System"));
		if (db == null) {
			io.sendConsoleWarning("Unknown Database System. Falling back to SQLite");
			db = Database.getDatabaseBySystem("SQLite");
		}
		db.init();
		new HelpManager().init();
		getServer().getPluginCommand("warp").setExecutor(new WarpCommand());
		getServer().getPluginCommand("warps").setExecutor(new WarpsCommand());
		//getServer().getPluginManager().registerEvents(new BukkitListener(), this);
		if (config.getBoolean("General.Statistics")) metrics.startMetrics();
	}
	
	@Override
	public void onDisable() {
    	db.shutdown();
    	getServer().getScheduler().cancelTasks(this);
    	io.sendConsole(io.translate("Plugin.Done"));
	}
	
	public static HashMap<String, Warp> getWarps() {
		return warps;
	}
	
	public static List<Warp> getWarpList() {
		return new ArrayList<Warp>(warps.values());
	}
	
	public static JavaPlugin getInstance() {
		return instance;
	}
	
	public static IOManager getIOManager() {
		return io;
	}
	
	public static Configuration getConfiguration() {
		return config;
	}
	
	public static Database getDb() {
		return db;
	}
}
