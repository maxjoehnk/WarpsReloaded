package org.efreak.warps.commands;

import java.util.Arrays;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.efreak.warps.help.HelpManager;

public class WarpCommand implements CommandExecutor {

	public WarpCommand() {
		HelpManager.registerCommand("Warp.Create", "/warp create", Arrays.asList("(name)"), "warps.warp.create");
		HelpManager.registerCommand("Warp.List", "/warp list", Arrays.asList("[#]"), "warps.list");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = null;
		ConsoleCommandSender c = null;
		if (sender instanceof Player) p = (Player) sender;
		if (sender instanceof ConsoleCommandSender) c = (ConsoleCommandSender) sender;
		if (p == c) return false;
		if (args.length == 0) return false;
		else {
			if (label.equalsIgnoreCase("warp")) {
				
				return true;
			}else return false;
		}
	}
}
