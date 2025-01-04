package com.xlavaclash.items;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class WeaponCategory implements ItemCategory {
    private static final Random random = new Random();
    
    private static final List<Material> WEAPONS = Arrays.asList(
        Material.DIAMOND_SWORD,
        Material.IRON_SWORD,
        Material.BOW,
        Material.CROSSBOW,
        Material.TRIDENT
    );
    
    private static final List<Enchantment> WEAPON_ENCHANTS = Arrays.asList(
        Enchantment.DAMAGE_ALL,
        Enchantment.FIRE_ASPECT,
        Enchantment.KNOCKBACK
    );

    @Override
    public ItemStack generateItem() {
        Material material = WEAPONS.get(random.nextInt(WEAPONS.size()));
        ItemStack item = new ItemStack(material);
        
        if (random.nextDouble() < 0.7) { // 70% chance for enchantment
            ItemMeta meta = item.getItemMeta();
            Enchantment enchant = WEAPON_ENCHANTS.get(random.nextInt(WEAPON_ENCHANTS.size()));
            meta.addEnchant(enchant, random.nextInt(2) + 1, true);
            item.setItemMeta(meta);
        }
        
        return item;
    }
}