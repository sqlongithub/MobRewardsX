package me.sql.mobrewardsx.util;

import org.bukkit.enchantments.Enchantment;

public enum 1_11_1_EnchantUtil {
	
	// 1.11.1 Enchantments
	// Note: Hashmaps?
	AQUA_AFFINITY(Enchantment.WATER_WORKER),
	BANE_OF_ARTHROPODS(Enchantment.DAMAGE_ARTHROPODS),
	BLAST_PROTECTION(Enchantment.PROTECTION_EXPLOSIONS),
	BINDING_CURSE(Enchantment.BINDING_CURSE),
	VANISHING_CURSE(Enchantment.VANISHING_CURSE),
	DEPTH_STRIDER(Enchantment.DEPTH_STRIDER),
	EFFICIENCY(Enchantment.DIG_SPEED),
	FEATHER_FALLING(Enchantment.PROTECTION_FALL),
	FIRE_ASPECT(Enchantment.FIRE_ASPECT),
	FIRE_PROTECTION(Enchantment.PROTECTION_FIRE),
	FLAME(Enchantment.ARROW_FIRE),
	FORTUNE(Enchantment.LOOT_BONUS_BLOCKS),
	FROST_WALKER(Enchantment.FROST_WALKER),
	INFINITY(Enchantment.ARROW_INFINITE),
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
	SWEEPING(Enchantment.SWEEPING_EDGE),
	THORNS(Enchantment.THORNS),
	UNBREAKING(Enchantment.DURABILITY);
	
	private Enchantment ench;
	
	EnchantUtil_1_11_1(Enchantment ench) {
		this.ench = ench;
	}
	
	public Enchantment getEnchant() {
		return this.ench;
	}
	
}
