package org.efreak.warps.help;

import java.util.ArrayList;
import java.util.List;

public class HelpManager {

	private static List<HelpTopic> helpTopics;
	private static HelpFile helpFile;

	public void init() {
		helpTopics = new ArrayList<HelpTopic>();
		helpFile = new HelpFile();
		helpFile.initialize();
	}
	
	public static void registerCommand(String helpNode, String label, List<String> args, String perm) {
		String argString;
		if (args.size() != 0) {
			argString = args.get(0);
			for (int i = 1; i < args.size(); i++) argString += " " + args.get(i);
		}else argString = "";
		registerTopic(new HelpTopic(label, argString, helpFile.getHelp(helpNode), perm));
	}
	
	public static void registerTopic(HelpTopic topic) {
		helpTopics.add(topic);
	}
	
	public static void registerTopics(List<HelpTopic> topics) {
		helpTopics.addAll(topics);
	}
	
	public static boolean unregisterTopic(HelpTopic topic) {
		for (int i = 0; i < helpTopics.size(); i++) {
			if (helpTopics.get(i).equals(topic)) {
				helpTopics.remove(i);
				return true;
			}
		}		
		return false;
	}
	
	public static HelpTopic getTopic(int i) {
		return helpTopics.get(i);
	}
	
	public static List<HelpTopic> getTopics() {
		return helpTopics;
	}	
}
