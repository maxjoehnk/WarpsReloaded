package org.efreak.warps;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class Warp {
	
	private Location location;
	private String name;
	private String perm;
	private double cost;
	private boolean permRequired = false;
	private boolean payRequired = false;

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
					cost = warp.getDouble("cost");
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
			if (!WarpConfiguration.containsWarp(name)) {
				io.sendConsoleWarning("Can't load warp " + name);
				io.sendConsoleWarning("Warp couldn't be found");
				return;
			}
			this.name = name;
			ConfigurationSection warp = WarpConfiguration.getWarp(name);
			World world = Bukkit.getWorld(warp.getString("Location.World"));
			int x = warp.getInt("Location.X");
			int y = warp.getInt("Location.Y");
			int z = warp.getInt("Location.Z");
			location = new Location(world, x, y, z);
			if (warp.contains("Cost")) {
				cost = warp.getDouble("Cost");
				payRequired = true;
			}
			if (warp.contains("Permission")) {
				perm = warp.getString("Permission");
				permRequired = true;
			}
		}
	}
	
	public static Warp create(String name, Location location, String perm, double cost) {
		if (config.getBoolean("Warps.UseDatabase")) {
			
		}else WarpConfiguration.createWarp(name, location, perm, cost);
		Warp warp = new Warp(name);
		WarpsReloaded.addWarp(warp);
		return warp;
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
		if ((permRequired && Permissions.has(player, perm, true)) || !permRequired) {
			if ((payRequired && WarpsReloaded.getEconomy().bankHas(player.getName(), cost).transactionSuccess()) || !payRequired) {
				if (config.getBoolean("Warps.Particles"))player.playEffect(player.getLocation(), Effect.SMOKE, 0);
				player.teleport(location, TeleportCause.PLUGIN);
				if (config.getBoolean("Warps.Particles")) player.playEffect(location, Effect.SMOKE, 0);
				io.send(player, "You've been warped to " + name);
			}
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
	
	public double getCost() {
		return cost;
	}
}
