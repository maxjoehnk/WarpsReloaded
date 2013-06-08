package org.efreak.warps.help;

import org.bukkit.command.CommandSender;
import org.efreak.warps.Configuration;
import org.efreak.warps.Permissions;
import org.efreak.warps.WarpsReloaded;

public class HelpTopic {

	private String cmd = null;
	private String args = null;
	private String desc = null;
	private String perms;
	private static Configuration config;
	
	static {
		config = WarpsReloaded.getConfiguration();
	}
	
	/**
	 * 
	 * Creates a new HelpTopic
	 * 
	 * @param arg1cmd The Command Label
	 * @param arg2Args The Arguments of the Command
	 * @param arg3Desc The Description of what the Command does
	 * @param arg4Perms The needed Permission to view the Command in the Help List
	 * 
	 */
	public HelpTopic(String arg1cmd, String arg2Args, String arg3Desc, String arg4Perms) {
		cmd = arg1cmd;
		args = arg2Args;
		desc = arg3Desc;
		perms = arg4Perms;
	}
	
	public String format() {
		String formatted = "";
		formatted = config.getString("IO.HelpFormat");
		if (cmd != null) formatted = formatted.replaceAll("%cmd%", cmd);
		if (args != null) formatted = formatted.replaceAll("%args%", args);
		if (desc != null) formatted = formatted.replaceAll("%desc%", desc);
		return formatted;
	}
	
	public boolean hasPerm(CommandSender sender) {
		return Permissions.has(sender, perms);
	}
	
}
