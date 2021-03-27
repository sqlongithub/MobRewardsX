package me.sql.mobrewardsx.events;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.sql.mobrewardsx.MobRewardsX;

public class OnPlayerJoin implements Listener {

	Map<Player, Boolean> firstJoinAfterStartup = new HashMap<Player, Boolean>();
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		if(firstJoinAfterStartup.get(e.getPlayer())==null) {
			firstJoinAfterStartup.put(e.getPlayer(), true);
			MobRewardsX.initPlayerDefaults(e.getPlayer(), Bukkit.getPluginManager().getPlugin("MobRewardsX").getConfig());
		}
	}
	
}
