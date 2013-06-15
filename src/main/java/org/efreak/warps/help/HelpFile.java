package org.efreak.warps.help;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.efreak.warps.Configuration;
import org.efreak.warps.WarpsReloaded;

public class HelpFile {

	private static Configuration config;
	private File helpFile;
	private YamlConfiguration help;
	
	static {
		config = WarpsReloaded.getConfiguration();		
	}
	
	public void initialize() {
		helpFile = new File(WarpsReloaded.getInstance().getDataFolder(), "help.yml");
		help = new YamlConfiguration();
		try {
			if (!helpFile.exists()) helpFile.createNewFile();
			help.load(helpFile);
			addContent();
			help.save(helpFile);
		} catch (FileNotFoundException e) {
			if (config.getDebug()) e.printStackTrace();
		} catch (IOException e) {
			if (config.getDebug()) e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			if (config.getDebug()) e.printStackTrace();
		}
	}
	
	private void addContent() {
		update("Warp.Warp", "Warp a player");
		update("Warp.Create", "Create a new Warp");
		update("Warp.List", "List all available warps");
		update("Warp.Help", "Displays this help");
	}
	
	public String getHelp(String key) {
		return help.getString(key);
	}
	
	public boolean update(String path, Object value) {
		if (!help.contains(path)) {
			help.set(path, value);
			return false;
		}else return true;
	}
}
