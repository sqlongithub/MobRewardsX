package me.sql.mobrewardsx.events;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
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

public class OnMobDamaged implements Listener {

	Plugin plugin = Bukkit.getPluginManager().getPlugin("MobRewardsX");
	
	Map<LivingEntity,Player> lastPlayerAttackers = new HashMap<>();
	@EventHandler
	public void onMobHitByPlayer(EntityDamageByEntityEvent e) {
		if(!(e.getEntity() instanceof Creature)) {
			return;
		}
		if(e.getDamager() instanceof Player) {
			lastPlayerAttackers.put((LivingEntity) e.getEntity(), (Player) e.getDamager());
		}
	}
	
	// TODO: Implement PvE Rewards. Started 18/3. Money reward finished 19/3
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
		Player killer = e.getEntity().getKiller();
		int rewardedMoney = getRewardedMoney(plugin.getConfig(), e.getEntity());
		int itemId = mobSection.getInt("item-rewarded");
		// ugly code, basically if the mobsection has a custom chance, then use that, else use the chance from the item section
		float chance = (mobSection.getInt("chance") / 100) == 0 ? itemsSection.getConfigurationSection(Integer.toString(itemId)).getInt("chance") : mobSection.getInt("chance");
		// Player killed
		if(killer!=null) {
			if(Math.random()<chance) {
				ItemStack rewardedItem = getItem(itemsSection, itemId);
				if(rewardedItem!=null) {
					killer.getInventory().addItem(rewardedItem);
				} else {
					Bukkit.getLogger().info("[MobRewardsX] The item at id "+itemId+" is invalid or doesn't exist.");
				}
			}
			if(rewardedMoney>0) {
				MobRewardsX.getEconomy().depositPlayer((Player) killer, rewardedMoney);
			} else {
				MobRewardsX.getEconomy().withdrawPlayer((Player) killer, Math.abs(rewardedMoney));
			}
		}
		// Config: kill-assist = true
		else {
			if(plugin.getConfig().getBoolean("kill-assist")) {
				if(Math.random()<chance) {
					ItemStack rewardedItem = getItem(itemsSection, itemId);
					if(rewardedItem!=null) {
						lastPlayerAttackers.get(e.getEntity()).getInventory().addItem(rewardedItem);
					} else {
						Bukkit.getLogger().info("[MobRewardsX] The item at id "+itemId+" is invalid or doesn't exist.");
					}
				}
				if(rewardedMoney>0) {
					MobRewardsX.getEconomy().depositPlayer(lastPlayerAttackers.get(e.getEntity()), rewardedMoney);
				} else {
					MobRewardsX.getEconomy().withdrawPlayer(lastPlayerAttackers.get(e.getEntity()), Math.abs(rewardedMoney));
				}
			}
		}
	}
	
	private int getRewardedMoney(FileConfiguration config, LivingEntity mob) {
		
		ConfigurationSection mobSection = getMobSection(mob);
		if(mob!=null && mobSection!=null) {
			// Mob exists in config
			return mobSection.getInt("money-rewarded");
		};
		return 0;
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
			
			// Binary search
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