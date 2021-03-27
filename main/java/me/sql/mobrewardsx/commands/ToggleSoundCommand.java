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

@CommandAlias("ts|togglesound")
public class ToggleSoundCommand extends BaseCommand {
	
	private String onOff(boolean bool) {
		if(bool)
			return "on";
		else
			return "off";
	}
	
	@Default
	@Description("Toggles a sound when you recieve a reward")
	public void onToggleSound(Player sender) {
		MobRewardsX.allSoundNotifications.put(sender, !MobRewardsX.allSoundNotifications.get(sender));
		MobRewardsX.itemSoundNotifications.put(sender, MobRewardsX.allSoundNotifications.get(sender));
		MobRewardsX.moneySoundNotifications.put(sender, MobRewardsX.allSoundNotifications.get(sender));
		sender.sendMessage(Formatting.TOGGLED_SOUND1+"all"+Formatting.TOGGLED_SOUND2+onOff(MobRewardsX.allSoundNotifications.get(sender))+Formatting.TOGGLED_SOUND3);
	}
	
	@HelpCommand
	@Description("Shows usage for /togglesound") 
	public void onToggleSoundHelp(Player sender, CommandHelp help) {
		help.setPerPage(10);
		help.showHelp();
	}
	
	@Subcommand("money|m")
	@Description("Toggles a sound when you recieve a money reward")
	public void onToggleMoneySound(Player sender) {
		MobRewardsX.moneySoundNotifications.put(sender, !MobRewardsX.moneySoundNotifications.get(sender));
		sender.sendMessage(Formatting.TOGGLED_SOUND1+"money"+Formatting.TOGGLED_SOUND2+onOff(MobRewardsX.moneySoundNotifications.get(sender))+Formatting.TOGGLED_SOUND3);
	}
	
	@Subcommand("item|i")
	@Description("Toggles a sound when you recieve an item reward")
	public void onToggleItemSound(Player sender) {
		MobRewardsX.itemSoundNotifications.put(sender, !MobRewardsX.itemSoundNotifications.get(sender));
		sender.sendMessage(Formatting.TOGGLED_SOUND1+"item"+Formatting.TOGGLED_SOUND2+onOff(MobRewardsX.itemSoundNotifications.get(sender))+Formatting.TOGGLED_SOUND3);
	}
	
}
