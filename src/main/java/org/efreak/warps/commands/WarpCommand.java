package org.efreak.warps.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.efreak.warps.Configuration;
import org.efreak.warps.IOManager;
import org.efreak.warps.Permissions;
import org.efreak.warps.Warp;
import org.efreak.warps.WarpsReloaded;
import org.efreak.warps.help.HelpManager;

public class WarpCommand implements CommandExecutor {

	private static IOManager io;
	private static Configuration config;
	
	static {
		io = WarpsReloaded.getIOManager();
		config = WarpsReloaded.getConfiguration();
	}
	
	public WarpCommand() {
		HelpManager.registerCommand("Warp", "/warp", Arrays.asList("(name)", "[player]"), "warps.warp");
		HelpManager.registerCommand("Warp.Create", "/warp create", Arrays.asList("(name)"), "warps.warp.create");
		HelpManager.registerCommand("Warp.List", "/warp list", Arrays.asList("[#]"), "warps.list");
		HelpManager.registerCommand("Warp.Help", "/warp help", Arrays.asList("[#]"), "warps.help");
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
				if (args[0].equalsIgnoreCase("list")) runList(sender, cmd, label, args);
				else if (args[0].equalsIgnoreCase("create")) runCreate(sender, cmd, label, args);
				else if (args[0].equalsIgnoreCase("help")) runHelp(sender, cmd, label, args);
				else run(sender, cmd, label, args);				
				return true;
			}else return false;
		}
	}
	
	private boolean runCreate(CommandSender sender, Command cmd, String label, String[] args) {
		return true;
	}
	
	private boolean runList(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length > 2) return false;
		if (Permissions.has(sender, "warps.list", false, "/warp list")) {
			int pageCount = 1;
			List<Warp> warps = WarpsReloaded.getWarpList();
			if (warps.size() % 9 == 0) pageCount = warps.size() / 9;
			else pageCount = (warps.size() / 9) + 1;
			if (args.length == 1) {
				if (Integer.valueOf(args[1]) > pageCount) {
					io.sendError(sender, "This page doesn't exist");
					return true;
				}else {
					io.sendHeader(sender, "WARPS (" + args[1] + "/" + pageCount + ")");
					for (int i = pageCount * 9; i < warps.size() && i < (pageCount + 1) * 9; i++) io.send(sender, warps.get(i).getName(), false);
				}
			}else {
				io.sendHeader(sender, "WARPS (1/" + pageCount + ")");
				for (int i = 0; i < warps.size() && i < 9; i++) io.send(sender, warps.get(i).getName(), false);
			}
		}
		return true;	}
	
	private boolean run(CommandSender sender, Command cmd, String label, String[] args) {
		return true;
	}
	
	private boolean runHelp(CommandSender sender, Command cmd, String label, String[] args) {
		List<org.efreak.warps.help.HelpTopic> helpTopics = HelpManager.getTopics();
		List<String> topics = new ArrayList<String>();
		for (int i = 0; i < helpTopics.size(); i++) {
			if (helpTopics.get(i).hasPerm(sender)) topics.add(helpTopics.get(i).format());
		}
		int pages = 1;
		if (topics.size() > 9) pages = (int)(topics.size() / 9F+0.4F);
		if (args.length == 0) {
			io.sendHeader(sender, config.getString("IO.HelpHeader").replaceAll("%page%",  "1").replaceAll("%pages%", String.valueOf(pages)));
			for (int i = 0; i < 9 && i < topics.size(); i++) io.send(sender, topics.get(i), false);
		}else if (args.length == 1) {
			if (args[0].equalsIgnoreCase("caption")) {
				io.sendHeader(sender, "WARPSRELOADED HELP LEGEND");
	       		io.send(sender, "&c#&f : Number", false);
	       		io.send(sender, "&cvalue&f : place holder", false);
	       		io.send(sender, "&evalue&f : defined value", false);
	       		io.send(sender, "&c[]&f : optional value", false);
	       		io.send(sender, "&c()&f : required value", false);
	       		io.send(sender, "&c|&f : (or) seperator", false);
			}else {
				try {
					if (new Integer(args[0]) <= pages) {
						int page = new Integer(args[0]);
						io.send(sender, config.getString("IO.HelpHeader").replaceAll("%page%",  args[0]).replaceAll("%pages%", String.valueOf(pages)), false);
						for (int i = 9*page-9; i < 9*page && i < topics.size()-1; i++) io.send(sender, topics.get(i), false);
					}else io.sendError(sender, "This Page doesn't exist.");
				}catch (NumberFormatException e) {
					io.sendWarning(sender, "Command related help isn't implemented yet");
				}
			}
		}
		return true;
	}
}