package org.efreak.warps;

import java.util.List;

import org.bukkit.entity.Player;

public class WarpGroup {

	private String name;
	private List<Warp> warps;
	
	public WarpGroup(String name) {
		this.name = name;
		
	}
	
	public String getName() {
		return name;
	}
	
	public void warp(Player player) {
		Warp warp = warps.get(0 + (int)(Math.random() * ((warps.size() - 0) + 1)));
		warp.warp(player, true);
	}
	
}
