package org.efreak.warps;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
	
	public static ConfigurationSection getWarpRegions() {
		return warps.getConfigurationSection("WarpRegions");
	}
	
	public static List<String> getWarpRegionNames() {
		return new ArrayList<String>(warps.getConfigurationSection("WarpRegions").getKeys(false));
	}
}