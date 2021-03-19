package me.sql.mobrewardsx;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import co.aikar.commands.PaperCommandManager;
import me.sql.mobrewardsx.commands.MobCommand;
import me.sql.mobrewardsx.commands.MobRewardsXCommand;
import me.sql.mobrewardsx.events.OnMobDamaged;
import net.milkbowl.vault.economy.Economy;

public class MobRewardsX extends JavaPlugin {
	
	private static final Logger log = Logger.getLogger("Minecraft");
	private PaperCommandManager manager;
	private static Economy econ;	
	public static Map<Player, Boolean> playerSoundNotifications;
	public static Map<Player, Boolean> playerMessageNotifications;
	
	@Override
	public void onEnable() {
		if (!setupEconomy() ) {
            log.severe(String.format("[%s] - Vault is required! Please either download it or update it. Disabling.", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
		
		playerSoundNotifications = new HashMap<Player, Boolean>();
		playerMessageNotifications = new HashMap<Player, Boolean>();
		for(Player ply : Bukkit.getServer().getOnlinePlayers()) {
			playerSoundNotifications.put(ply, this.getConfig().getBoolean("sound-toggled-default"));
			playerMessageNotifications.put(ply, this.getConfig().getBoolean("message-toggled-default"));
		}
		
		
		FileConfiguration config = this.getConfig();
		initConfig(config);
		
		Bukkit.getPluginManager().registerEvents(new OnMobDamaged(), this);
		
		manager = new PaperCommandManager(this);
		manager.registerCommand(new MobRewardsXCommand());
		manager.registerCommand(new MobCommand());
		// TODO: Add and implement more commands 17/3
	}
	
	@Override
	public void onDisable() {
		// TODO: Potentially save configs etc..
	}
	
	private void initConfig(FileConfiguration config) {
		this.saveDefaultConfig();
	}
	
	private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
	
	public static Economy getEconomy() {
        return econ;
    }
 
}
