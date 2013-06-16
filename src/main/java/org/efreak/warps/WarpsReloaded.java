package org.efreak.warps;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
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
	private static HashMap<String, WarpGroup> warpGroups;
	private static Economy economy;
	
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
		if (config.getBoolean("Warps.UseDatabase")) db.init();
		warps = new HashMap<String, Warp>();
		for (String warp : WarpConfiguration.getWarpNames()) addWarp(new Warp(warp));
		warpGroups = new HashMap<String, WarpGroup>();
		for (String warpGroup : WarpConfiguration.getWarpGroupNames()) addWarpGroup(new WarpGroup(warpGroup));
		new HelpManager().init();
		getServer().getPluginCommand("warp").setExecutor(new WarpCommand());
		getServer().getPluginCommand("warps").setExecutor(new WarpsCommand());
		getServer().getPluginCommand("setwarp").setExecutor(new SetwarpCommand());
		//getServer().getPluginManager().registerEvents(new BukkitListener(), this);
		if (config.getBoolean("General.Statistics")) metrics.startMetrics();
		RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
		if (economyProvider != null) economy = economyProvider.getProvider();
		io.sendConsole(getDescription().getFullName() + " has been enabled");
	}
	
	@Override
	public void onDisable() {
		if (config.getBoolean("Warps.UseDatabase")) db.shutdown();
    	getServer().getScheduler().cancelTasks(this);
		io.sendConsole(getDescription().getFullName() + " has been disabled");
	}
	
	public static void addWarp(Warp warp) {
		warps.put(warp.getName(), warp);
		if (config.getDebug()) io.debug("Added Warp: " + warp.getName());
	}
	
	public static HashMap<String, Warp> getWarps() {
		return warps;
	}
	
	public static List<Warp> getWarpList() {
		return new ArrayList<Warp>(warps.values());
	}
	
	public static void addWarpGroup(WarpGroup group) {
		warpGroups.put(group.getName(), group);
		if (config.getDebug()) io.debug("Added Warp Group: " + group.getName() + " containing " + group.getWarps().size() + " Warps");
	}
	
	public static HashMap<String, WarpGroup> getWarpGroups() {
		return warpGroups;
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
	
	public static Economy getEconomy() {
		return economy;
	}
}
