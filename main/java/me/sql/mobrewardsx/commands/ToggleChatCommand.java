package me.sql.mobrewardsx.commands;

import org.bukkit.entity.Player;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.HelpCommand;
import co.aikar.commands.annotation.Subcommand;
import me.sql.mobrewardsx.MobRewardsX;
import me.sql.mobrewardsx.util.Formatting;

@CommandAlias("tc|togglechat")
public class ToggleChatCommand extends BaseCommand {
	
	private String onOff(boolean bool) {
		if(bool)
			return "on";
		else
			return "off";
	}
	
	@Default
	@Description("Toggles all chat messages when you recieve a reward")
	public void onToggleChatMessage(Player sender) {
		MobRewardsX.allMessageNotifications.put(sender, !MobRewardsX.allMessageNotifications.get(sender));
		MobRewardsX.moneyMessageNotifications.put(sender, MobRewardsX.allMessageNotifications.get(sender));
		MobRewardsX.itemMessageNotifications.put(sender, MobRewardsX.allMessageNotifications.get(sender));
		MobRewardsX.rareItemMessageNotifications.put(sender, MobRewardsX.allMessageNotifications.get(sender));
		sender.sendMessage(Formatting.TOGGLED_MESSAGE1+"all"+Formatting.TOGGLED_MESSAGE2+onOff(MobRewardsX.allMessageNotifications.get(sender))+Formatting.TOGGLED_MESSAGE3);
	}
	
	@HelpCommand
	@Description("Shows usage for /togglechat") 
	public void onToggleChatHelp(Player sender, CommandHelp help) {
		help.setPerPage(10);
		help.showHelp();
	}
	
	@Subcommand("item|i")
	@Description("Toggles chat messages when you recieve an item reward")
	public void onToggleItemChatMessage(Player sender) {
		MobRewardsX.itemMessageNotifications.put(sender, !MobRewardsX.itemMessageNotifications.get(sender));
		sender.sendMessage(Formatting.TOGGLED_MESSAGE1+"item"+Formatting.TOGGLED_MESSAGE2+onOff(MobRewardsX.itemMessageNotifications.get(sender))+Formatting.TOGGLED_MESSAGE3);
	}
	
	@Subcommand("rare|r")
	@Description("Toggles chat messages when you recieve a rare item reward")
	public void onToggleRareItemChatMessage(Player sender) {
		MobRewardsX.rareItemMessageNotifications.put(sender, !MobRewardsX.rareItemMessageNotifications.get(sender));
		sender.sendMessage(Formatting.TOGGLED_MESSAGE1+"rare item"+Formatting.TOGGLED_MESSAGE2+onOff(MobRewardsX.rareItemMessageNotifications.get(sender))+Formatting.TOGGLED_MESSAGE3);
	}
	
	@Subcommand("money|m")
	@Description("Toggles chat messages when you recieve a money reward")
	public void onToggleMoneyChatMessage(Player sender) {
		MobRewardsX.moneyMessageNotifications.put(sender, !MobRewardsX.moneyMessageNotifications.get(sender));
		sender.sendMessage(Formatting.TOGGLED_MESSAGE1+"money"+Formatting.TOGGLED_MESSAGE2+onOff(MobRewardsX.moneyMessageNotifications.get(sender))+Formatting.TOGGLED_MESSAGE3);
	}
	
}
