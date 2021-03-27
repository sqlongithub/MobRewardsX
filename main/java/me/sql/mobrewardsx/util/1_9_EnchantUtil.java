package me.sql.mobrewardsx.util;

import org.bukkit.enchantments.Enchantment;

public enum 1_9_EnchantUtil {

	// 1.9.4 Enchants
	AQUA_AFFINITY(Enchantment.WATER_WORKER),
	BANE_OF_ARTHROPODS(Enchantment.DAMAGE_ARTHROPODS),
	BLAST_PROTECTION(Enchantment.PROTECTION_EXPLOSIONS),
	DEPTH_STRIDER(Enchantment.DEPTH_STRIDER),
	EFFICIENCY(Enchantment.DIG_SPEED),
	FEATHER_FALLING(Enchantment.PROTECTION_FALL),
	FIRE_ASPECT(Enchantment.FIRE_ASPECT),
	FIRE_PROTECTION(Enchantment.PROTECTION_FIRE),
	FLAME(Enchantment.ARROW_FIRE),
	FORTUNE(Enchantment.LOOT_BONUS_BLOCKS),
	FROST_WALKER(Enchantment.FROST_WALKER),
	KNOCKBACK(Enchantment.KNOCKBACK),
	LOOTING(Enchantment.LOOT_BONUS_MOBS),
	LUCK_OF_THE_SEA(Enchantment.LUCK),
	LURE(Enchantment.LURE),
	MENDING(Enchantment.MENDING),
	POWER(Enchantment.ARROW_DAMAGE),
	PROJECTILE_PROTECTION(Enchantment.PROTECTION_PROJECTILE),
	PROTECTION(Enchantment.PROTECTION_ENVIRONMENTAL),
	PUNCH(Enchantment.ARROW_KNOCKBACK),
	RESPIRATION(Enchantment.OXYGEN),
	SHARPNESS(Enchantment.DAMAGE_ALL),
	SILK_TOUCH(Enchantment.SILK_TOUCH),
	SMITE(Enchantment.DAMAGE_UNDEAD),
	THORNS(Enchantment.THORNS),
	UNBREAKING(Enchantment.DURABILITY);
	
	private Enchantment ench;
	
	EnchantUtil_1_9(Enchantment ench) {
		this.ench = ench;
	}
	
	public Enchantment getEnchant() {
		return this.ench;
	}
	
}
