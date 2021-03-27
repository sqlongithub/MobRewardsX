package me.sql.mobrewardsx.util;

import org.bukkit.ChatColor;

public enum Formatting {
	PREFIX(ChatColor.GRAY+"["+ChatColor.GOLD+"MRX"+ChatColor.GRAY+"] "),
	MOB_HELP(PREFIX.toString()+ChatColor.AQUA+"Mobs\n"+
	        	"/mrx mob <mob>"+ChatColor.GREEN+" - Prints the rewards of a mob\n"+
				"/mrx mob <mob> setitemreward <id>"+ChatColor.GREEN+" - Sets the reward ID, use 0 to set off\n"+
	        	"/mrx mob <mob> setchance <chance>"+ChatColor.GREEN+" - Overwrites the item reward chance\n"+
				"/mrx mob <mob> setmoneyreward <money>"+ChatColor.GREEN+" - Sets the money reward, use 0 to set off\n"+
	        	"/mrx mob <mob> delrewards"+ChatColor.GREEN+" - Removes all rewards from this mob, and deletes it from the config"),
	
	ITEM_HELP(PREFIX.toString()+ChatColor.AQUA+"Items\n"+
	        	"/mrx createitem <chance>"+ChatColor.GREEN+" - Grabs your item in hand and stores it in the config at the lowest available id\n"+
				"/mrx listitems"+ChatColor.GREEN+" - Lists all item ids and the item with them\n"+
	        	"/mrx item <itemid>"+ChatColor.GREEN+" - View information about an item\n"+
				"/mrx item <itemid> setchance <chance>"+ChatColor.GREEN+" - Sets the reward chance for the item\n"+
	        	"/mrx item <itemid> setname <name>"+ChatColor.GREEN+" - Sets the name for the item\n"+
	        	"/mrx item <itemid> setmaterial <material>"+ChatColor.GREEN+" - Sets the material for the item\n"+
	        	"/mrx item <itemid> setloreline <line> <lore..>"+ChatColor.GREEN+" - Sets the reward chance for the item\n"+
	        	"/mrx item <itemid> setquantity <quantity>"+ChatColor.GREEN+" - Sets the quantity for the item\n"+
	        	"/mrx item <itemid> setenchant <encahntid> <enchantlevel>"+ChatColor.GREEN+" - Adds the specified enchant to the item\n"+
	        	"/mrx item <itemid> removeenchant <encahntid>"+ChatColor.GREEN+" - Removes the specified enchant from the item\n"+
	        	"/mrx item <itemid> setid <id>"+ChatColor.GREEN+" - Sets the item to the specified id\n"),
	
	TOGGLED_SOUND1(PREFIX.toString()+ChatColor.AQUA+"Toggled "+ChatColor.GOLD),
	TOGGLED_SOUND2(ChatColor.AQUA+" sound notifications "+ChatColor.GOLD),
	TOGGLED_SOUND3(ChatColor.AQUA+"."),
	TOGGLED_MESSAGE1(PREFIX.toString()+ChatColor.AQUA+"Toggled "+ChatColor.GOLD),
	TOGGLED_MESSAGE2(ChatColor.AQUA+" message notifications "+ChatColor.GOLD),
	TOGGLED_MESSAGE3(ChatColor.AQUA+".");
	
	private final String text;
	
	Formatting(final String text) {
        this.text = text;
    }
	
	public static String translateToChatColor(String itemName) {
		// Is there an easier way to do this?
		String translatedString = itemName;
		translatedString.replaceAll("§0", ChatColor.BLACK.toString());
		translatedString.replaceAll("§1", ChatColor.DARK_BLUE.toString());
		translatedString.replaceAll("§2", ChatColor.DARK_GREEN.toString());
		translatedString.replaceAll("§3", ChatColor.DARK_AQUA.toString());
		translatedString.replaceAll("§4", ChatColor.DARK_RED.toString());
		translatedString.replaceAll("§5", ChatColor.DARK_PURPLE.toString());
		translatedString.replaceAll("§6", ChatColor.GOLD.toString());
		translatedString.replaceAll("§7", ChatColor.GRAY.toString());
		translatedString.replaceAll("§8", ChatColor.DARK_GRAY.toString());
		translatedString.replaceAll("§9", ChatColor.BLUE.toString());
		
		translatedString.replaceAll("§a", ChatColor.GREEN.toString());
		translatedString.replaceAll("§b", ChatColor.AQUA.toString());
		translatedString.replaceAll("§c", ChatColor.RED.toString());
		translatedString.replaceAll("§d", ChatColor.LIGHT_PURPLE.toString());
		translatedString.replaceAll("§e", ChatColor.YELLOW.toString());
		translatedString.replaceAll("§f", ChatColor.WHITE.toString());
		
		translatedString.replaceAll("§k", ChatColor.MAGIC.toString());
		translatedString.replaceAll("§l", ChatColor.BOLD.toString());
		translatedString.replaceAll("§m", ChatColor.STRIKETHROUGH.toString());
		translatedString.replaceAll("§n", ChatColor.UNDERLINE.toString());
		translatedString.replaceAll("§o", ChatColor.ITALIC.toString());
		translatedString.replaceAll("§r", ChatColor.RESET.toString());
		
		return translatedString;
	}
	
	@Override
    public String toString() {
        return text;
    }
}
