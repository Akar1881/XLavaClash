package com.xlavaclash.items;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class ItemGenerator {
    private static final Random random = new Random();
    
    private static final List<ItemCategory> CATEGORIES = Arrays.asList(
        new WeaponCategory(),
        new ArmorCategory(),
        new UtilityCategory(),
        new PotionCategory()
    );

    public static ItemStack generateRandomItem() {
        ItemCategory category = CATEGORIES.get(random.nextInt(CATEGORIES.size()));
        return category.generateItem();
    }

    public static List<ItemStack> generateStarterKit() {
        List<ItemStack> kit = new ArrayList<>();
        
        // Add basic armor
        kit.add(new ItemStack(Material.IRON_HELMET));
        kit.add(new ItemStack(Material.IRON_CHESTPLATE));
        kit.add(new ItemStack(Material.IRON_LEGGINGS));
        kit.add(new ItemStack(Material.IRON_BOOTS));
        
        // Add basic weapon
        kit.add(new ItemStack(Material.IRON_SWORD));
        
        // Add basic tools
        kit.add(new ItemStack(Material.STONE_PICKAXE));
        kit.add(new ItemStack(Material.STONE_AXE));
        
        // Add some blocks
        kit.add(new ItemStack(Material.STONE, 64));
        
        return kit;
    }
}