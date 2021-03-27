package me.sql.mobrewardsx.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Name;
import co.aikar.commands.annotation.Subcommand;
import me.sql.mobrewardsx.util.Formatting;
import net.md_5.bungee.api.ChatColor;

// -------------------------------------------------
// | This file may be deleted, and this class will |
// | be put in the main MobRewardsXCommand class.  |
// -------------------------------------------------

@CommandAlias("mobrewardsx|mrx|mobrewards")
public class MobCommand extends BaseCommand {
	
	private FileConfiguration config = Bukkit.getPluginManager().getPlugin("MobRewardsX").getConfig();
	
	private String formatMobName(String mobName) {
		return mobName.substring(0,1).toUpperCase()+mobName.substring(1).toLowerCase().replaceAll("_", " ");
	}
	
	private ConfigurationSection getRewardItem(ConfigurationSection mobSection) {
		ConfigurationSection itemsSection = config.getConfigurationSection("items");
		if(itemsSection==null) {
			Bukkit.getLogger().info("[MobRewardsX] Config not configured properly (No items Section). Please delete the config to reset it.");
			return null;
		}
		ConfigurationSection itemSection = itemsSection.getConfigurationSection(Integer.toString(mobSection.getInt("item-rewarded")));
		if(itemSection==null) {
			Bukkit.getLogger().info("[MobRewardsX] No item for item id "+mobSection.getInt("item-rewarded")+".");
			return null;
		}
		return itemSection;
	}
	
	private ConfigurationSection getTypeSection(String mobName) {
		ConfigurationSection mobsSection = config.getConfigurationSection("mobs");
		String[] mobTypes = {"passive","neutral","hostile","boss"};
		if(mobsSection==null) {
			Bukkit.getLogger().info("[MobRewardsX] Config not configured properly (No mobs Section). Please delete it to reset it.");
			return null;
		}
		for(String s : mobTypes) {
			if(!config.getBoolean(s)) {
				continue;
			}
			
			// search
			for(int i = mobTypes.length/2; i<mobTypes.length; i++) {
				if(mobsSection.getConfigurationSection(mobTypes[i]).contains(mobName.toLowerCase())) {
					return mobsSection.getConfigurationSection(mobTypes[i]);
				}
			}
			for(int i = mobTypes.length/2-1; i>=0; i--) {
				if(mobsSection.getConfigurationSection(mobTypes[i]).contains(mobName.toLowerCase())) {
					return mobsSection.getConfigurationSection(mobTypes[i]);
				}
			}
		}
		return null;
	}
	
	@Subcommand("mob")
	@Description("Change and view properties of mobs")
	@CommandPermission("mrx.mob")
	public void onMob(CommandSender sender) {
		sender.sendMessage(Formatting.MOB_HELP.toString());
	}
	
	@Subcommand("mob")
	@Description("View rewards of a mob")
	@CommandPermission("mrx.mob")
	// View mob rewards
	public void onViewMobRewards(CommandSender sender, @Name("Mob") String mobName) {
		ConfigurationSection mobTypeSection = getTypeSection(mobName);
		if(mobTypeSection==null) {
			sender.sendMessage(Formatting.PREFIX.toString()+ChatColor.AQUA+"This mob doesn't have rewards!");
			return;
		}
		ConfigurationSection mobSection = mobTypeSection.getConfigurationSection(mobName.toLowerCase());
		if(mobSection==null) {
			sender.sendMessage(Formatting.PREFIX.toString()+ChatColor.AQUA+"This mob doesn't have rewards!");
			return;
		}
		ConfigurationSection itemSection = getRewardItem(mobSection);
		if(itemSection==null) {
			sender.sendMessage(Formatting.PREFIX.toString()+ChatColor.AQUA+"The item reward of this mob is invalid! Check the console for more information.");
			return;
		}
		double itemChance = mobSection.getDouble("chance") == 0 ? itemSection.getDouble("chance") : mobSection.getDouble("chance");
		sender.sendMessage(Formatting.PREFIX.toString()+ChatColor.AQUA+formatMobName(mobName)+"\n"+
						   ChatColor.AQUA+"Money Reward: "+ChatColor.GOLD+mobSection.getInt("money-rewarded")+"\n"+
						   ChatColor.AQUA+"Item Reward: "+ChatColor.GRAY+itemSection.getInt("quantity")+"x "+Formatting.translateToChatColor(itemSection.getString("name"))+"\n"+
		                   ChatColor.AQUA+"Item Chance: "+ChatColor.GOLD+Double.toString(itemChance).replaceAll("(\\.0)+?$","")+"%");
	}
	
	@Subcommand("mob")
	@Description("Delete rewards of a mob")
	@CommandPermission("mrx.mob.deleterewards")
	// Delete rewards
	public void onDeleteRewards(CommandSender sender, @Name("Mob") String mobName, String settingCommand) {
		if(settingCommand.toLowerCase().equalsIgnoreCase("delrewards")) {
			ConfigurationSection mobTypeSection = getTypeSection(mobName);
			if(mobTypeSection==null) {
				sender.sendMessage(Formatting.PREFIX.toString()+ChatColor.AQUA+"This mob doesn't have rewards!");
				return;
			}
			mobTypeSection.set(mobName, null);
			sender.sendMessage(Formatting.PREFIX.toString()+ChatColor.AQUA+"Deleted rewards for "+ChatColor.GOLD+formatMobName(mobName));
		}
	}
	
	@Subcommand("mob")
	@Description("Change the money reward of a mob")
	@CommandPermission("mrx.mob.setmoneyreward")
	// Set money reward
	public void onSetMoneyReward(CommandSender sender, @Name("Mob") String mobName, String settingCommand, int moneyReward) {
		if(settingCommand.toLowerCase().equalsIgnoreCase("setmoneyreward")) {
			ConfigurationSection mobTypeSection = getTypeSection(mobName);
			if(mobTypeSection==null) {
				sender.sendMessage(Formatting.PREFIX.toString()+ChatColor.AQUA+"This mob doesn't have rewards! Please create settings for it.");
				return;
			}
			ConfigurationSection mobSection = mobTypeSection.getConfigurationSection(mobName.toLowerCase());
			mobSection.set("money-rewarded", moneyReward);
			sender.sendMessage(Formatting.PREFIX.toString()+ChatColor.AQUA+"Set the money reward for "+ChatColor.GOLD+formatMobName(mobName)+ChatColor.AQUA+" to "+ChatColor.GOLD+moneyReward+ChatColor.AQUA+".");
		}
	}
	
	@Subcommand("mob")
	@Description("Change the item reward of a mob")
	@CommandPermission("mrx.mob.setitemreward")
	// Set item reward
	public void onSetItemReward(CommandSender sender, @Name("Mob") String mobName, String settingCommand, int itemRewardId) {
		if(settingCommand.toLowerCase().equalsIgnoreCase("setitemreward")) {
			ConfigurationSection mobTypeSection = getTypeSection(mobName);
			if(mobTypeSection==null) {
				sender.sendMessage(Formatting.PREFIX.toString()+ChatColor.AQUA+"This mob doesn't have rewards! Please create settings for it.");
				return;
			}
			ConfigurationSection mobSection = mobTypeSection.getConfigurationSection(mobName.toLowerCase());
			mobSection.set("item-rewarded", itemRewardId);
			sender.sendMessage(Formatting.PREFIX.toString()+ChatColor.AQUA+"Set the item reward id for "+ChatColor.GOLD+formatMobName(mobName)+ChatColor.AQUA+" to "+ChatColor.GOLD+itemRewardId+ChatColor.AQUA+".");
		}
	}
	
	@Subcommand("mob")
	@Description("Change the item reward chance of a mob")
	@CommandPermission("mrx.mob.setchance")
	// Set money reward
	public void onSetItemRewardChance(CommandSender sender, @Name("Mob") String mobName, String settingCommand, int itemRewardChance) {
		if(settingCommand.toLowerCase().equalsIgnoreCase("setchance")) {
			ConfigurationSection mobTypeSection = getTypeSection(mobName);
			if(mobTypeSection==null) {
				sender.sendMessage(Formatting.PREFIX.toString()+ChatColor.AQUA+"This mob doesn't have rewards! Please create settings for it.");
				return;
			}
			ConfigurationSection mobSection = mobTypeSection.getConfigurationSection(mobName.toLowerCase());
			mobSection.set("chance", itemRewardChance);
			sender.sendMessage(Formatting.PREFIX.toString()+ChatColor.AQUA+"Overwrote the item reward chance for "+ChatColor.GOLD+formatMobName(mobName)+ChatColor.AQUA+" to "+ChatColor.GOLD+itemRewardChance+ChatColor.AQUA+".");
		}
	}
	
}