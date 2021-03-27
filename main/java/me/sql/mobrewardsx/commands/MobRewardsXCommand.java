package me.sql.mobrewardsx.commands;

import org.bukkit.command.CommandSender;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.HelpCommand;
import co.aikar.commands.annotation.Name;
import co.aikar.commands.annotation.Subcommand;
import me.sql.mobrewardsx.MobRewardsX;
import me.sql.mobrewardsx.util.Formatting;
import net.md_5.bungee.api.ChatColor;

@CommandAlias("mobrewardsx|mrx|mobrewards")
public class MobRewardsXCommand extends BaseCommand {
	
	@Subcommand("info|about")
	@Description("Displays information about MRX")
	@CommandPermission("mrx.about")
	@Default
	public void onMRX(CommandSender sender) {
		sender.sendMessage(Formatting.PREFIX+"About\n"
				+ ChatColor.GOLD+"Version: "+ChatColor.AQUA+MobRewardsX.properties.get("version")+"\n"
				+ ChatColor.GOLD+"Author: "+ChatColor.AQUA+MobRewardsX.properties.get("author").toString().replaceAll("\"", ""));
	}
	
	@Subcommand("help")
	@Description("Displays information about the usage of MRX")
	@HelpCommand
	@CommandPermission("mrx.help")
	public void onHelp(CommandSender sender, CommandHelp help, @Name("page") @Default("1") Integer page) {
		sender.sendMessage(Formatting.PREFIX+"Help Menu");
		// Set Help settings
		help.setPerPage(10);
		help.setPage(page);
		help.showHelp();
	}
}
