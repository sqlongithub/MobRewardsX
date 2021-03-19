package me.sql.mobrewardsx.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.HelpCommand;
import co.aikar.commands.annotation.Name;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import me.sql.mobrewardsx.MobRewardsX;
import me.sql.mobrewardsx.util.Formatting;

@CommandAlias("mobrewardsx|mrx|mobrewards")
public class MobRewardsXCommand extends BaseCommand {

	// t: token
	// s: section
	
	// ctrl+f: t-about
	@Subcommand("info|about")
	@Description("Displays information about MRX")
	@Default
	public void onMRX(CommandSender sender) {

	}
	
	// ctrl+f: t-help
	@Subcommand("help")
	@Description("Displays information about the usage of MRX")
	@HelpCommand
	@Syntax("<page> - Shows a help menu for MRX")
	public void onHelp(CommandSender sender, CommandHelp help, @Name("page") @Default("1") Integer page) {
		sender.sendMessage(Formatting.PREFIX+"Help Menu");
		// Set Help settings
		help.setPerPage(10);
		help.setPage(page);
		help.showHelp();
	}
	
	// ctrl+f: t-togglesound
	@Subcommand("togglesound")
	@Description("Toggles a sound when you recieve a reward")
	public void onToggleSound(Player sender) {
		MobRewardsX.playerSoundNotifications.put(sender, !MobRewardsX.playerSoundNotifications.get(sender));
		sender.sendMessage(Formatting.TOGGLED_SOUND1+MobRewardsX.playerSoundNotifications.get(sender).toString()+Formatting.TOGGLED_SOUND2);
	}
	
	// ctrl+f: t-togglechat
		@Subcommand("togglechat")
		@Description("Toggles a chat message when you recieve a reward")
		public void onToggleChatMessage(Player sender) {
			MobRewardsX.playerMessageNotification.put(sender, !MobRewardsX.playerMessageNotifications.get(sender));
			sender.sendMessage(Formatting.TOGGLED_MESSAGE1+MobRewardsX.playerMessageNotifications.get(sender).toString()+Formatting.TOGGLED_MESSAGE2);
		}
	
}
