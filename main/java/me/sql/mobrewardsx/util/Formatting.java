package me.sql.mobrewardsx.util;

import org.bukkit.ChatColor;

public enum Formatting {
	PREFIX(ChatColor.GRAY+"["+ChatColor.GOLD+"MRX"+ChatColor.GRAY+"] "),
	REWARD_HELP(PREFIX.toString()+ChatColor.AQUA+"Rewards\n"+
	        	"/mrx reward <mob>"),
	TOGGLED_SOUND1(PREFIX.toString()+ChatColor.AQUA+"Toggled sound notifications "+ChatColor.GOLD),
	TOGGLED_SOUND2(ChatColor.AQUA+".");
	
	private final String text;
	
	Formatting(final String text) {
        this.text = text;
    }
	
	@Override
    public String toString() {
        return text;
    }
}
