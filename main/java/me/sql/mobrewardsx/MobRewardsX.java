package me.sql.mobrewardsx;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import co.aikar.commands.PaperCommandManager;
import me.sql.mobrewardsx.commands.MobCommand;
import me.sql.mobrewardsx.commands.MobRewardsXCommand;
import me.sql.mobrewardsx.commands.ToggleChatCommand;
import me.sql.mobrewardsx.commands.ToggleSoundCommand;
import me.sql.mobrewardsx.events.OnMobDamaged;
import me.sql.mobrewardsx.events.OnPlayerJoin;
import net.milkbowl.vault.economy.Economy;

public class MobRewardsX extends JavaPlugin {
	
	private static final Logger log = Logger.getLogger("Minecraft");
	private PaperCommandManager manager;
	private static Economy econ;	
	public static final Properties properties = new Properties();
	public static Map<Player, Boolean> allSoundNotifications;
	public static Map<Player, Boolean> moneySoundNotifications;
	public static Map<Player, Boolean> itemSoundNotifications;
	
	public static Map<Player, Boolean> allMessageNotifications;
	public static Map<Player, Boolean> itemMessageNotifications;
	public static Map<Player, Boolean> rareItemMessageNotifications;
	public static Map<Player, Boolean> moneyMessageNotifications;
	
	@Override
	public void onEnable() {
		try {
			properties.load(this.getClassLoader().getResourceAsStream("project.properties"));	
		} catch (IOException e) {
			Bukkit.getLogger().severe("[MRX] Fatal error, no project.properties in plugin jar, corrupted plugin file? Please download the latest version from the spigot page.");
		}
		
		if (!setupEconomy() ) {
            log.severe(String.format("[%s] - Vault is required! Please either download it or update it. Disabling.", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
		
		allSoundNotifications = new HashMap<Player, Boolean>();
		moneySoundNotifications = new HashMap<Player, Boolean>();
		itemSoundNotifications = new HashMap<Player, Boolean>();
		
		allMessageNotifications = new HashMap<Player, Boolean>();
		itemMessageNotifications = new HashMap<Player, Boolean>();
		rareItemMessageNotifications = new HashMap<Player, Boolean>();
		moneyMessageNotifications = new HashMap<Player, Boolean>();
		for(Player ply : Bukkit.getServer().getOnlinePlayers()) {
			initPlayerDefaults(ply, this.getConfig());
		}
		
		
		initConfig();
		
		Bukkit.getPluginManager().registerEvents(new OnMobDamaged(), this);
		Bukkit.getPluginManager().registerEvents(new OnPlayerJoin(), this);
		
		manager = new PaperCommandManager(this);
		manager.enableUnstableAPI("help");
		manager.registerCommand(new MobRewardsXCommand());
		manager.registerCommand(new ToggleSoundCommand());
		manager.registerCommand(new ToggleChatCommand());
		manager.registerCommand(new MobCommand());
	}
	
	@Override
	public void onDisable() {
		// TODO: Potentially save configs etc..
	}
	
	public static void initPlayerDefaults(Player ply, FileConfiguration cfg) {
		// TODO: please fix this terrible code, add checkers for all notifications in 
		// OnMobDamaged, please i beg you.
		allSoundNotifications.put(ply, cfg.getBoolean("all-sound-toggled-default"));
		itemSoundNotifications.put(ply, cfg.getBoolean("all-sound-toggled-default"));
		moneySoundNotifications.put(ply, cfg.getBoolean("all-sound-toggled-default"));
		itemSoundNotifications.put(ply, cfg.getBoolean("item-sound-toggled-default"));
		moneySoundNotifications.put(ply, cfg.getBoolean("money-sound-toggled-default"));
		
		allMessageNotifications.put(ply, cfg.getBoolean("all-message-toggled-default"));
		itemMessageNotifications.put(ply, cfg.getBoolean("all-message-toggled-default"));
		rareItemMessageNotifications.put(ply, cfg.getBoolean("all-message-toggled-default"));
		moneyMessageNotifications.put(ply, cfg.getBoolean("all-message-toggled-default"));
		itemMessageNotifications.put(ply, cfg.getBoolean("item-message-toggled-default"));
		rareItemMessageNotifications.put(ply, cfg.getBoolean("item-rare-message-toggled-default"));
		moneyMessageNotifications.put(ply, cfg.getBoolean("money-message-toggled-default"));
	}
	
	private void initConfig() {
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
