package me.sql.mobrewardsx.events;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import me.sql.mobrewardsx.MobRewardsX;
import me.sql.mobrewardsx.util.EnchantUtil_1_11_1;
import me.sql.mobrewardsx.util.EnchantUtil_1_8;
import me.sql.mobrewardsx.util.EnchantUtil_1_9;
import me.sql.mobrewardsx.util.VersionUtil;
import net.md_5.bungee.api.ChatColor;

public class OnMobDamaged implements Listener {

	private Plugin plugin = Bukkit.getPluginManager().getPlugin("MobRewardsX");
	
	private Map<LivingEntity,Player> lastPlayerAttackers = new HashMap<>();
	@EventHandler
	public void onMobHitByPlayer(EntityDamageByEntityEvent e) {
		if(!(e.getEntity() instanceof Creature)) {
			return;
		}
		if(e.getDamager() instanceof Player) {
			lastPlayerAttackers.put((LivingEntity) e.getEntity(), (Player) e.getDamager());
		}
	}
	
	@EventHandler
	public void onMobKilled(EntityDeathEvent e) {
		if(!(e.getEntity() instanceof Creature) || e.getEntity().getLastDamageCause().getCause()==DamageCause.STARVATION) {
			return;
		}
		
		ConfigurationSection mobSection = getMobSection(e.getEntity());
		ConfigurationSection itemsSection = plugin.getConfig().getConfigurationSection("items");
		
		if(mobSection==null) {
			return;
		}
		
		boolean soundPlayed = false;
		Player killer = e.getEntity().getKiller();
		int rewardedMoney = getRewardedMoney(e.getEntity());
		int itemId = mobSection.getInt("item-rewarded");
		// ugly code, basically if the mobsection has a custom chance, then use that, else use the chance from the item section
		double chance = mobSection.getDouble("chance") == 0 ? itemsSection.getConfigurationSection(Integer.toString(itemId)).getDouble("chance")/100 : mobSection.getDouble("chance")/100;
		// Player killed
		if(killer!=null) {
			// The mob or item had a chance and we rolled a number in that range
			if(Math.random()<chance) {
				ItemStack rewardedItem = getItem(itemsSection, itemId);
				// Item is valid
				if(rewardedItem!=null) {
					// Item has a name
					soundPlayed = rewardItem(rewardedItem, killer, rewardedMoney, chance); 
				} else {
					Bukkit.getLogger().info("[MobRewardsX] The item at id "+itemId+" is invalid or doesn't exist.");
				}
			}
			// There was no item reward
			if(rewardedMoney>0) {
				if(lastPlayerAttackers.get(e.getEntity())!=null)	
					MobRewardsX.getEconomy().depositPlayer((Player) killer, rewardedMoney);
				if(!soundPlayed)
					soundPlayed = playNotifications(killer, rewardedMoney, 0d, "", 0);
			} else {
				if(lastPlayerAttackers.get(e.getEntity())!=null)	
					MobRewardsX.getEconomy().withdrawPlayer((Player) killer, Math.abs(rewardedMoney));
			}
		}
		// Not killed by player
		else {
			// Config: kill-assist = true
			if(plugin.getConfig().getBoolean("kill-assist")) {
				
				if(Math.random()<chance) {
					ItemStack rewardedItem = getItem(itemsSection, itemId);
					if(rewardedItem!=null) {
						rewardItem(rewardedItem, lastPlayerAttackers.get(e.getEntity()), rewardedMoney, chance);
						soundPlayed = true;
					} else {
						Bukkit.getLogger().info("[MobRewardsX] The item at id "+itemId+" is invalid or doesn't exist.");
					}
				}
				if(rewardedMoney>0) {
					if(lastPlayerAttackers.get(e.getEntity())!=null) {
						if(!soundPlayed)
							soundPlayed = playNotifications(lastPlayerAttackers.get(e.getEntity()), rewardedMoney, 0d, "", 0);
						MobRewardsX.getEconomy().depositPlayer(lastPlayerAttackers.get(e.getEntity()), rewardedMoney);
					}
				} else {
					if(lastPlayerAttackers.get(e.getEntity())!=null) {
						if(!soundPlayed)
							soundPlayed = playNotifications(lastPlayerAttackers.get(e.getEntity()), rewardedMoney, 0d, "", 0);
						MobRewardsX.getEconomy().withdrawPlayer(lastPlayerAttackers.get(e.getEntity()), Math.abs(rewardedMoney));
					}
				}
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	private boolean rewardItem(ItemStack rewardedItem, Player killer, int rewardedMoney, double chance) {
		boolean soundPlayed = false;
		if(killer==null) {
			return false;
		}
		if(rewardedItem.getItemMeta().hasDisplayName()) {
			// Play notifications
			soundPlayed = playNotifications(killer, rewardedMoney, chance, rewardedItem.getItemMeta().getDisplayName(), rewardedItem.getAmount());
		} else {
			soundPlayed = playNotifications(killer, rewardedMoney, chance, rewardedItem.getType().toString(), rewardedItem.getAmount());
		}
		HashMap<Integer, ItemStack> items = killer.getInventory().addItem(rewardedItem);
		for(ItemStack item : items.values()) {
			killer.getWorld().dropItem(killer.getLocation(), item);		
		}
		return soundPlayed;
	}
	
	private boolean playNotifications(Player ply, int moneyReward, double itemChance, String itemName, int itemAmount) {
		boolean playedSound = false;
		if(moneyReward>0) {
			if(MobRewardsX.moneySoundNotifications.get(ply)) {
				if(VersionUtil.versionGreaterThan(Bukkit.getVersion(), "1.8.8")) {
					ply.playSound(ply.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.8f, 0.8f);
				} else {
					ply.playSound(ply.getLocation(), Sound.valueOf("ORB_PICKUP"), 0.8f, 0.8f);
				}
				playedSound = true;
			}
			if(MobRewardsX.moneyMessageNotifications.get(ply)) {
				if(MobRewardsX.getEconomy().currencyNameSingular().length()==1) {
					ply.sendMessage(ChatColor.GREEN+"+"+ChatColor.GOLD+MobRewardsX.getEconomy().currencyNameSingular()+moneyReward);
				} else if(MobRewardsX.getEconomy().currencyNameSingular().length()==0) {
					ply.sendMessage(ChatColor.GREEN+"+"+ChatColor.GOLD+"$"+moneyReward);
				} else {
					ply.sendMessage(ChatColor.GREEN+"+"+ChatColor.GOLD+" "+MobRewardsX.getEconomy().currencyNamePlural());
				}
			}
		}
		if(itemChance>0) {
			if(MobRewardsX.itemSoundNotifications.get(ply) && !playedSound) {
				if(VersionUtil.versionGreaterThan(Bukkit.getVersion(), "1.8.8")) {
					if(itemChance<=plugin.getConfig().getInt("rare-chance")) {
						ply.playSound(ply.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5f, 0.9f);
					}
					ply.playSound(ply.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.8f, 0.8f);
				} else {
					if(itemChance<=plugin.getConfig().getInt("rare-chance")) {
						ply.playSound(ply.getLocation(), Sound.valueOf("ORB_PICKUP"), 0.5f, 0.9f);
					}
					ply.playSound(ply.getLocation(), Sound.valueOf("ORB_PICKUP"), 0.8f, 0.8f);
				}
				playedSound = true;
			}
			if(MobRewardsX.itemMessageNotifications.get(ply) || (MobRewardsX.rareItemMessageNotifications.get(ply) && itemChance<=plugin.getConfig().getInt("rare-chance"))) {
				if(itemChance<=plugin.getConfig().getDouble("rare-chance")/100) {
					if(MobRewardsX.rareItemMessageNotifications.get(ply) || MobRewardsX.itemMessageNotifications.get(ply)) {
						ply.sendMessage(placeholderReplace(plugin.getConfig().getString("rare-reward-message"), itemName, itemChance, itemAmount));
					}
				} else if(MobRewardsX.itemMessageNotifications.get(ply)) {
					ply.sendMessage(placeholderReplace(plugin.getConfig().getString("normal-reward-message"), itemName, itemChance, itemAmount));
				}
			}
		}
		return playedSound;
	}
	
	private int getRewardedMoney(LivingEntity mob) {
		
		ConfigurationSection mobSection = getMobSection(mob);
		if(mob!=null && mobSection!=null) {
			// Mob exists in config
			return mobSection.getInt("money-rewarded");
		}
		return 0;
	}
	
	private String placeholderReplace(String str, String itemName, double itemChance, int itemAmount) {
		return str.replaceAll("%chance%", Double.toString(itemChance*100).replaceAll("(\\.0)+?$","")).replaceAll("%item%", itemName).replaceAll("%amount%", Integer.toString(itemAmount));
	}
	
	private ConfigurationSection getMobSection(LivingEntity mob) {
		ConfigurationSection mobsSection = plugin.getConfig().getConfigurationSection("mobs");
		String[] mobTypes = {"passive","neutral","hostile","boss"};
		if(mobsSection==null) {
			Bukkit.getLogger().info("[MobRewardsX] Config not configured properly (No mobs Section). Please delete it to reset it.");
			return null;
		}
		for(String s : mobTypes) {
			if(!plugin.getConfig().getBoolean(s)) {
				continue;
			}
			
			// search
			for(int i = mobTypes.length/2; i<mobTypes.length; i++) {
				if(mobsSection.getConfigurationSection(mobTypes[i]).contains(mob.getType().toString().toLowerCase())) {
					return mobsSection.getConfigurationSection(mobTypes[i]).getConfigurationSection(mob.getType().toString().toLowerCase());
				};
			}
			for(int i = mobTypes.length/2-1; i>=0; i--) {
				if(mobsSection.getConfigurationSection(mobTypes[i]).contains(mob.getType().toString().toLowerCase())) {
					return mobsSection.getConfigurationSection(mobTypes[i]).getConfigurationSection(mob.getType().toString().toLowerCase());
				};
			}
		}
		return null;
	}
	
	@SuppressWarnings("deprecation")
	private ItemStack getItem(ConfigurationSection sec, int id) {
		ConfigurationSection itemSection = sec.getConfigurationSection(Integer.toString(id));
		Material material = null;
		if(itemSection!=null) {
			try {
				material = Material.valueOf(itemSection.getString("material").toUpperCase());
			} catch(IllegalArgumentException e) {
				Bukkit.getLogger().info("[MobRewardsX] The material for item id "+id+" is invalid.");
				return null;
			} catch(NullPointerException e) {
				Bukkit.getLogger().info("[MobRewardsX] The material for item id "+id+" doesn't exist");
				return null;
			}
			int quantity = itemSection.getInt("quantity");
			List<String> itemLore = itemSection.getStringList("lore");
			ConfigurationSection enchantSection = itemSection.getConfigurationSection("enchants");
			ItemStack returnedItem = new ItemStack(material, quantity);
			for(String s : enchantSection.getKeys(false)) {
				Enchantment ench = null;
				// Get enchantments depending on version
				if(VersionUtil.versionGreaterThan(Bukkit.getVersion(), "1.12.2")) {
					ench = EnchantmentWrapper.getByKey(NamespacedKey.minecraft(s));
				} else if(VersionUtil.versionGreaterThan(Bukkit.getVersion(), "1.11"))  {
					ench = EnchantUtil_1_11_1.valueOf(s.toUpperCase()).getEnchant();
				} else if(VersionUtil.versionGreaterThan(Bukkit.getVersion(), "1.8.8")) {
					ench = EnchantUtil_1_9.valueOf(s.toUpperCase()).getEnchant();
				} else if(VersionUtil.versionGreaterThan(Bukkit.getVersion(), "1.7.10"))  {
					ench = EnchantUtil_1_8.valueOf(s.toUpperCase()).getEnchant();
				}
				if(ench == null) {
					Bukkit.getLogger().info("[MobRewardsX] Enchantment "+s+" is not a valid enchant, item id: "+id);
					continue;
				}
				returnedItem.addUnsafeEnchantment(ench, enchantSection.getInt(s));
			}
			ItemMeta itemMeta = returnedItem.getItemMeta();
			if(itemMeta.hasLore()) 
				itemLore.addAll(itemMeta.getLore());
			itemMeta.setLore(itemLore);
			itemMeta.setDisplayName(itemSection.getString("name"));
			returnedItem.setItemMeta(itemMeta);
			return returnedItem;
		}
		return null;
	}
}
