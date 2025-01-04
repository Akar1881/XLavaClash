package com.xlavaclash.items;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ArmorCategory implements ItemCategory {
    private static final Random random = new Random();
    
    private static final List<Material> ARMOR = Arrays.asList(
        Material.DIAMOND_HELMET,
        Material.DIAMOND_CHESTPLATE,
        Material.DIAMOND_LEGGINGS,
        Material.DIAMOND_BOOTS,
        Material.NETHERITE_HELMET,
        Material.NETHERITE_CHESTPLATE,
        Material.NETHERITE_LEGGINGS,
        Material.NETHERITE_BOOTS
    );
    
    private static final List<Enchantment> ARMOR_ENCHANTS = Arrays.asList(
        Enchantment.PROTECTION_ENVIRONMENTAL,
        Enchantment.PROTECTION_FIRE,
        Enchantment.PROTECTION_PROJECTILE
    );

    @Override
    public ItemStack generateItem() {
        Material material = ARMOR.get(random.nextInt(ARMOR.size()));
        ItemStack item = new ItemStack(material);
        
        if (random.nextDouble() < 0.6) { // 60% chance for enchantment
            ItemMeta meta = item.getItemMeta();
            Enchantment enchant = ARMOR_ENCHANTS.get(random.nextInt(ARMOR_ENCHANTS.size()));
            meta.addEnchant(enchant, random.nextInt(2) + 1, true);
            item.setItemMeta(meta);
        }
        
        return item;
    }
}