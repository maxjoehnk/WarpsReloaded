package org.efreak.warps;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;


/**
 * 
 * Loads and manages the config File
 *
 */

public class Configuration{
	 
	private static IOManager io;
	private static final Plugin plugin;
	private static FileConfiguration config;
	private static File configFile;
	private static String dbType = "SQLite";
	
	static {
		plugin = WarpsReloaded.getInstance();
	}
	
	/**
	 * 
	 * Loads and creates if needed the config
	 *
	 */
	
	public void init() {
		io = WarpsReloaded.getIOManager();
		configFile = new File(plugin.getDataFolder(), "config.yml");
		config = plugin.getConfig();
		if (!configFile.exists()){
			io.sendConsole("Creating config.yml...", true);
			try {
				configFile.createNewFile();
				updateConfig();
		        io.sendConsole("config.yml succesfully created!", true);
				config.load(configFile);
			} catch (IOException e) {
				if (getDebug()) e.printStackTrace();
			} catch (InvalidConfigurationException e) {
				if (getDebug()) e.printStackTrace();
			}
		}else {
	        try {
				config.load(configFile);
				updateConfig();
				config.load(configFile);
			} catch (IOException e) {
				if (getDebug()) e.printStackTrace();
			} catch (InvalidConfigurationException e) {
				if (getDebug()) e.printStackTrace();
			}
		}
	}
	
	/**
	 * 
	 * Updates an already existing config
	 * 
	 * @throws IOException
	 * 
	 */
	public void updateConfig() throws IOException {
		update("General.Permissions.Use-Permissions", true);
		update("General.Permissions.Use-Vault", true);
		update("General.Permissions.Force-SuperPerms", false);
		update("General.Permissions.Log", true);
		update("General.Debug", false);
		update("General.Statistics", true);
		update("IO.Show-Prefix", true);
		update("IO.Prefix", "&4[WarpsReloaded]");
		update("IO.Error", "&c[Error]");
		update("IO.Warning", "&e[Warning]");
		update("IO.Language", "en");		
		update("IO.ColoredLogs", true);
		update("IO.HelpHeader", "WARPS RELOADED HELP(%page%/%pages%)");
		update("IO.HelpFormat", "&e%cmd% %args%: &f%desc%");
		update("Database.System", "SQLite");
		update("Warps.UseDatabase", true); //Save Warps in Database or Flatfile
		if (!getBoolean("Warps.UseDatabase")) update("Warps.File", "warps.yml");
		else remove("Warps.File");
		config.save(configFile);
	}
			
	/**
	 * 
	 * Return whether WarpsReloaded is in Debug Mode or not
	 * 
	 * @return The Debug Mode
	 * 
	 */
	public boolean getDebug() {return config.getBoolean("General.Debug", false);}
	
	 /**
	 * 
	 * @return The currently used Database System
	 * @see org.efreak.bukkitmanager.Database
	 */
	public String getDatabaseType() {return dbType;}
	
	public String getString(String path) {return config.getString(path);}	
	public String getString(String path, String def) {return config.getString(path, def);}
	
	public boolean getBoolean(String path) {return config.getBoolean(path);}
	public boolean getBoolean(String path, Boolean def) {return config.getBoolean(path, def);}
	
	public int getInt(String path) {return config.getInt(path);}
	public int getInt(String path, int def) {return config.getInt(path, def);}
	
	public List<?> getList(String path) {return config.getList(path);}
	public List<?> getList(String path, List<?> def) {return config.getList(path, def);}
	
	public List<String> getStringList(String path) {return config.getStringList(path);}
	public List<Integer> getIntegerList(String path) {return config.getIntegerList(path);}

	public Object get(String path) {return config.get(path);}
	public Object get(String path, Object def) {return config.get(path, def);}
	
	public boolean update(String path, Object value) {
		if (!config.contains(path)) {
			config.set(path, value);
			return false;
		}else return true;
	}
	
	public void set(String path, Object value) {config.set(path, value);}
	
	public void remove(String path) {config.set(path, null);}
	
	public boolean contains(String path) {return config.contains(path);}

	public void reload() {
		try {
			config.load(configFile);
		} catch (FileNotFoundException e) {
			if (getDebug()) e.printStackTrace();
		} catch (IOException e) {
			if (getDebug()) e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			if (getDebug()) e.printStackTrace();
		}
	}

	public void save() {
		try {
			config.save(configFile);
		} catch (IOException e) {
			if (getDebug()) e.printStackTrace();
		}
	}

	public void addDefault(String path, Object value) {
		config.addDefault(path, value);
	}
	
	public void addAlias(String cmd) {
		update("General.Aliases." + cmd.substring(0, 1).toUpperCase() + cmd.substring(1), false);
	}
	
	public boolean getAlias(String cmd) {
		return getBoolean("General.Aliases." + cmd.substring(0, 1).toUpperCase() + cmd.substring(1));
	}
	
	public void addToList(String path, Object value) {
		update(path, Arrays.asList(getList(path), value));
	}
}
