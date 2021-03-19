package me.sql.mobrewardsx.commands;

import org.bukkit.command.CommandSender;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import me.sql.mobrewardsx.util.Formatting;

// -------------------------------------------------
// | This file may be deleted, and this class will |
// | be put in the main MobRewardsXCommand class.  |
// -------------------------------------------------

@CommandAlias("mobrewardsx|mrx|mobrewards")
public class MobCommand extends BaseCommand {
	
		@Subcommand("reward")
		@Description("Change and view properties of rewards")
		@CommandPermission("mrx.reward")
		public void onReward(CommandSender sender) {
			sender.sendMessage(Formatting.REWARD_HELP.toString());
		}
		
}