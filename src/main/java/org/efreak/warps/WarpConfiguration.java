package org.efreak.warps;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

public class WarpConfiguration{
	 
	private static Configuration config;
	private static YamlConfiguration warps;
	private static File warpsFile;
	
	static {
		config = WarpsReloaded.getConfiguration();
		warpsFile = new File(WarpsReloaded.getInstance().getDataFolder(), config.getString("Warps.File"));
		warps = new YamlConfiguration();
		try {
			if (!warpsFile.exists()) warpsFile.createNewFile();
	        warps.load(warpsFile);
		} catch (IOException e) {
			if (config.getDebug()) e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			if (config.getDebug()) e.printStackTrace();
		}
	}

	public static void save() {
		try {
			warps.save(warpsFile);
		} catch (IOException e) {
			if (config.getDebug()) e.printStackTrace();
		}
	}
	
	public static ConfigurationSection getWarps() {
		return warps.getConfigurationSection("Warps");
	}
	
	public static List<String> getWarpNames() {
		return new ArrayList<String>(warps.getConfigurationSection("Warps").getKeys(false));
	}

	public static boolean containsWarp(String name) {
		return warps.contains("Warps." + name);
	}
	
	public static ConfigurationSection getWarp(String name) {
		return warps.getConfigurationSection("Warps." + name);
	}
	
	public static void createWarp(String name, Location location, String perm, double cost) {
		warps.set("Warps." + name + ".Location.World", location.getWorld().getName());
		warps.set("Warps." + name + ".Location.X", location.getBlockX());
		warps.set("Warps." + name + ".Location.Y", location.getBlockY());
		warps.set("Warps." + name + ".Location.Z", location.getBlockZ());
		if (perm != null) warps.set("Warps." + name + ".Permission", perm);
		if (cost > 0) warps.set("Warps." + name + ".Cost", cost);
		try {
			warps.save(warpsFile);
		} catch (IOException e) {
			if (config.getDebug()) e.printStackTrace();
		}
	}
	
	public static ConfigurationSection getWarpGroups() {
		return warps.getConfigurationSection("WarpGroups");
	}
	
	public static List<String> getWarpGroupNames() {
		return new ArrayList<String>(warps.getConfigurationSection("WarpGroups").getKeys(false));
	}

	public static boolean containsWarpGroup(String name) {
		return warps.contains("WarpGroups." + name);
	}
	
	public static ConfigurationSection getWarpGroup(String name) {
		return warps.getConfigurationSection("WarpGroup." + name);
	}
	
	public static ConfigurationSection getWarpRegions() {
		return warps.getConfigurationSection("WarpRegions");
	}
	
	public static List<String> getWarpRegionNames() {
		return new ArrayList<String>(warps.getConfigurationSection("WarpRegions").getKeys(false));
	}
}
