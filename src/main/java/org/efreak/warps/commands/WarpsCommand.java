package org.efreak.warps.commands;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.efreak.warps.IOManager;
import org.efreak.warps.Warp;
import org.efreak.warps.WarpsReloaded;
import org.efreak.warps.help.HelpManager;

public class WarpsCommand implements CommandExecutor {

	private static IOManager io;
	
	static {
		io = WarpsReloaded.getIOManager();
	}
	
	public WarpsCommand() {
		HelpManager.registerCommand("Warp.List", "/warps", Arrays.asList("[#]"), "warps.list");
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
			if (label.equalsIgnoreCase("warps")) {
				if (args.length > 1) return false;
				int pageCount = 1;
				List<Warp> warps = WarpsReloaded.getWarpList();
				if (args.length == 1) {
					
				}else {
					io.sendHeader(sender, "WARPS (1/" + pageCount + ")");
					for (int i = 0; i < warps.size() && i < 9; i++) io.send(sender, warps.get(i).getName(), false);
				}
				return true;
			}else return false;
		}
	}
}
