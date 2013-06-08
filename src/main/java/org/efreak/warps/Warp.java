package org.efreak.warps;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class Warp {
	
	private Location location;
	private String name;
	private String perm;
	private int cost;
	private boolean permRequired;
	private boolean payRequired;

	private static Configuration config;
	private static Database db;
	private static IOManager io;
	
	static {
		config = WarpsReloaded.getConfiguration();
		db = WarpsReloaded.getDb();
		io = WarpsReloaded.getIOManager();
	}
	
	public Warp(String name) {
		if (config.getBoolean("Warps.Database")) {
			if (db.tableContains("warps", "name", name)) {
				this.name = name;
				ResultSet warp = db.query("SELECT * FROM `warps` WHERE `name`='" + name + "'");
				try {
					String world = warp.getString("location_world");
					int x = warp.getInt("location_x");
					int y = warp.getInt("location_y");
					int z = warp.getInt("location_z");
					location = new Location(Bukkit.getWorld(world), x, y, z);
				}catch (SQLException e) {
					if (config.getDebug()) e.printStackTrace();
					io.sendConsoleError("Error loading Warp " + name);
					io.sendConsoleError("SQLException: " + e.getMessage());
					return;
				}
				try {
					perm = warp.getString("permission");
					permRequired = perm == null ? false : true;
				}catch (SQLException e) {
					if (config.getDebug()) e.printStackTrace();
					permRequired = false;
				}
				try {
					cost = warp.getInt("cost");
					payRequired = cost == 0 ? false : true;
				}catch (SQLException e) {
					if (config.getDebug()) e.printStackTrace();
					payRequired = false;
				}
			}else {
				io.sendConsoleWarning("Can't load warp " + name);
				io.sendConsoleWarning("Warp couldn't be found");
				return;
			}
		}else {
			YamlConfiguration warpsFile = new YamlConfiguration();
			try {
				warpsFile.load(new File(config.getString("Warps.File")));
			} catch (Exception e) {
				if (config.getDebug()) e.printStackTrace();
				io.sendConsoleError("Error loading Warp " + name);
				io.sendConsoleError("IOException: " + e.getMessage());
				return;
			}
			if (!warpsFile.contains(name)) {
				io.sendConsoleWarning("Can't load warp " + name);
				io.sendConsoleWarning("Warp couldn't be found");
				return;
			}
			ConfigurationSection warpSection = warpsFile.getConfigurationSection(name);
			
		}
	}
	
	public String getName() {
		return name;
	}
	
	public Location getLocation() {
		return location;
	}
	
	public World getWorld() {
		return location.getWorld();
	}
	
	public void warp(Player player) {
		if (permRequired && !Permissions.has(player, perm)) {
			
		}
	}
	
	public void warp(Player player, boolean force) {
		if (config.getBoolean("Warps.Particles"))player.playEffect(player.getLocation(), Effect.SMOKE, 0);
		player.teleport(location, TeleportCause.PLUGIN);
		if (config.getBoolean("Warps.Particles")) player.playEffect(location, Effect.SMOKE, 0);
		io.send(player, "You've been warped to " + name);
	}
	
	public boolean isPermRequired() {
		return permRequired;
	}
	
	public boolean isPayRequired() {
		return payRequired;
	}
	
	public String getPerm() {
		return perm;
	}
	
	public int getCost() {
		return cost;
	}
}
