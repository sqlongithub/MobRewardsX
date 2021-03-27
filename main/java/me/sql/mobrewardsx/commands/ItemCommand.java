package me.sql.mobrewardsx.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import me.sql.mobrewardsx.util.Formatting;
import me.sql.mobrewardsx.util.VersionUtil;
import net.md_5.bungee.api.ChatColor;

//-------------------------------------------------
//| This file may be deleted, and this class will |
//| be put in the main MobRewardsXCommand class.  |
//-------------------------------------------------

@CommandAlias("mobrewardsx|mrx|mobrewards")
public class ItemCommand extends BaseCommand {
	
	// Commands:
	// /mrx createitem <chance> - Grabs your item in hand and stores it in the config at the lowest available id. (Done)
	// /mrx listitems - Lists all item ids and the item with them (Done)
	// /mrx item <itemid> - View information about an item
	// /mrx item <itemid> setchance <chance> - Sets the reward chance for the item
	// /mrx item <itemid> setname <name> - Sets the name for the item
	// /mrx item <itemid> setmaterial <material> - Sets the material for the item
	// /mrx item <itemid> setloreline <line> <lore..> - Sets the reward chance for the item
	// /mrx item <itemid> setquantity <quantity> - Sets the quantity for the item
	// /mrx item <itemid> setenchant <encahntid> <enchantlevel> - Adds the specified enchant to the item
	// /mrx item <itemid> removeenchant <encahntid> - Removes the specified enchant from the item
	// /mrx item <itemid> setid <id> - Sets the item to the specified id
	
	private FileConfiguration config = Bukkit.getPluginManager().getPlugin("MobRewardsX").getConfig();
	
	private int getLowestAvailableId(ConfigurationSection itemsSection) {
		List<String> ids_string = new ArrayList<String>();
		List<Integer> ids = new ArrayList<Integer>();
		// Converting Set into List
		ids_string.addAll(itemsSection.getKeys(false));
		// Converting list of strings into list of integers
		ids_string.forEach(s->ids.add(Integer.valueOf(s)));
		
		int len = ids.size();
		Set<Integer> set = new HashSet<>();
		Collections.sort(ids);
		for (int n : ids) {
		    if (n > 0) {
		        set.add(n);
		    }
		}
		for (int i = 1; i <= len + 1; i++) {
		    if (!set.contains(i)) {
		        return i;
		    }
		}
		return 0;
	}
	
	private String formatMaterialName(String material, boolean firstUpper) {
		if(firstUpper)
			return (material.toLowerCase().substring(0,1).toUpperCase()+material.toLowerCase().substring(1)).replaceAll("_", " ");
		else
			return material.toLowerCase().replaceAll("_", " ");
	}
	
	@Subcommand("item")
	@Description("Change and view properties of reward items")
	@CommandPermission("mrx.item")
	public void onItem(CommandSender sender) {
		sender.sendMessage(Formatting.ITEM_HELP.toString());
	}
	
	@SuppressWarnings("deprecation")
	@Subcommand("createitem")
	@Description("Change and view properties of reward items")
	@CommandPermission("mrx.item")
	public void onCreateItem(Player ply, int chance) {
		PlayerInventory plyInventory = ply.getInventory();
		ItemStack item;
		String itemId;
		ConfigurationSection itemsSection = config.getConfigurationSection("items");
		if(itemsSection==null) {
			ply.sendMessage(ChatColor.RED+"Error! The items section doesn't exist in the config.");
			return;
		}
		if(VersionUtil.versionGreaterThan(Bukkit.getVersion(), "1.8.8")) {
			item = plyInventory.getItemInMainHand();
		} else {
			item = plyInventory.getItemInHand();
		}
		itemId = Integer.toString(getLowestAvailableId(itemsSection));
		ItemMeta itemMeta = item.getItemMeta();
		ConfigurationSection itemSection = itemsSection.createSection(itemId);
		itemSection.set("material", item.getType().toString().toLowerCase());
		itemSection.set("quantity", item.getAmount());
		if(item.getItemMeta().hasDisplayName()) {
			itemSection.set("name", itemMeta.getDisplayName());
		} else {
			itemSection.set("name", formatMaterialName(item.getType().toString(), false));
		}
		itemSection.set("chance", chance);
		itemSection.set("lore", item.getLore());
		itemSection.set("material", item.getType().toString().toLowerCase());
		ply.sendMessage(Formatting.PREFIX.toString()+ChatColor.AQUA+"Succesfully created an item at id "+ChatColor.GOLD+itemId+" with the name "+ChatColor.GOLD+itemSection.getString("name"));
	}
	
	@Subcommand("listitems")
	@Description("Lists all items and their ids")
	@CommandPermission("mrx.listitems")
	public void onListItems(CommandSender sender, String settingCommand) {
		// List items
		ConfigurationSection itemsSection = config.getConfigurationSection("items");
		sender.sendMessage(Formatting.PREFIX.toString()+ChatColor.AQUA+"Items List");
		for(String key : itemsSection.getKeys(false)) {
			ConfigurationSection itemSection = itemsSection.getConfigurationSection(key);
			if(itemSection==null) {continue;}
			if(itemSection.getString("name")==null) {
				sender.sendMessage(ChatColor.GRAY+"- ?x "+ChatColor.GOLD+"????");
				continue;
			}
			sender.sendMessage(ChatColor.GRAY+"- "+itemSection.getInt("quantity")+"x "+Formatting.translateToChatColor(itemSection.getString("name")));
		}
	}
	
	@Subcommand("item")
	@Description("Change and view properties of reward items")
	@CommandPermission("mrx.item")
	public void onViewItemInfo(CommandSender sender, int itemId) {
		ConfigurationSection itemSection = config.getConfigurationSection("items."+itemId);
		if(itemSection==null) {
			sender.sendMessage(Formatting.PREFIX.toString()+ChatColor.RED+"There is no item at this id.");
			return;
		}
		sender.sendMessage(Formatting.PREFIX.toString()+"Item at id "+ChatColor.GOLD+itemId);
		sender.sendMessage(ChatColor.AQUA+"Type: "+formatMaterialName(itemSection.getString("material"), true));
		sender.sendMessage(ChatColor.AQUA+"Amount: "+itemSection.getInt("quantity"));
		sender.sendMessage(ChatColor.AQUA+"Name: "+itemSection.getString("name"));
		sender.sendMessage(ChatColor.AQUA+"Chance: "+itemSection.getInt("chance"));
		sender.sendMessage(ChatColor.AQUA+"Lore:");
		for(String loreLine : itemSection.getStringList("lore")) {
			sender.sendMessage(ChatColor.GRAY+"  - "+ChatColor.RESET+loreLine);
		}
		sender.sendMessage(ChatColor.AQUA+"Enchants:");
		for(String enchantName : itemSection.getConfigurationSection("enchants").getKeys(false)) {
			sender.sendMessage(ChatColor.GRAY+"  - "+ChatColor.RESET+enchantName+": "+itemSection.getConfigurationSection("enchants").getInt(enchantName));
		}
	}
	
}
