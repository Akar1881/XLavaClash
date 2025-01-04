package com.xlavaclash.items;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class ItemRegistry {
    private static final Map<ItemRarity, List<GameItem>> ITEMS = new HashMap<>();
    
    static {
        registerCommonItems();
        registerRareItems();
        registerEpicItems();
    }

    private static void registerCommonItems() {
        List<GameItem> commonItems = Arrays.asList(
            new GameItem(Material.STONE_SWORD, ItemRarity.COMMON, Map.of(), 1),
            new GameItem(Material.CHAINMAIL_HELMET, ItemRarity.COMMON, Map.of(), 1),
            new GameItem(Material.CHAINMAIL_CHESTPLATE, ItemRarity.COMMON, Map.of(), 1),
            new GameItem(Material.CHAINMAIL_LEGGINGS, ItemRarity.COMMON, Map.of(), 1),
            new GameItem(Material.CHAINMAIL_BOOTS, ItemRarity.COMMON, Map.of(), 1),
            new GameItem(Material.GOLDEN_APPLE, ItemRarity.COMMON, Map.of(), 2),
            new GameItem(Material.COBWEB, ItemRarity.COMMON, Map.of(), 4),
            new GameItem(Material.SNOWBALL, ItemRarity.COMMON, Map.of(), 16)
        );
        ITEMS.put(ItemRarity.COMMON, commonItems);
    }

    private static void registerRareItems() {
        List<GameItem> rareItems = Arrays.asList(
            new GameItem(Material.IRON_SWORD, ItemRarity.RARE, 
                Map.of(Enchantment.DAMAGE_ALL, 1), 1),
            new GameItem(Material.BOW, ItemRarity.RARE, 
                Map.of(Enchantment.ARROW_DAMAGE, 1), 1),
            new GameItem(Material.CROSSBOW, ItemRarity.RARE, 
                Map.of(Enchantment.QUICK_CHARGE, 1), 1),
            new GameItem(Material.IRON_HELMET, ItemRarity.RARE, 
                Map.of(Enchantment.PROTECTION_ENVIRONMENTAL, 1), 1),
            new GameItem(Material.IRON_CHESTPLATE, ItemRarity.RARE, 
                Map.of(Enchantment.PROTECTION_ENVIRONMENTAL, 1), 1),
            new GameItem(Material.ENDER_PEARL, ItemRarity.RARE, Map.of(), 2)
        );
        ITEMS.put(ItemRarity.RARE, rareItems);
    }

    private static void registerEpicItems() {
        List<GameItem> epicItems = Arrays.asList(
            new GameItem(Material.DIAMOND_SWORD, ItemRarity.EPIC,
                Map.of(Enchantment.DAMAGE_ALL, 2, Enchantment.FIRE_ASPECT, 1), 1),
            new GameItem(Material.BOW, ItemRarity.EPIC,
                Map.of(Enchantment.ARROW_DAMAGE, 2, Enchantment.ARROW_KNOCKBACK, 1), 1),
            new GameItem(Material.DIAMOND_HELMET, ItemRarity.EPIC,
                Map.of(Enchantment.PROTECTION_ENVIRONMENTAL, 2), 1),
            new GameItem(Material.DIAMOND_CHESTPLATE, ItemRarity.EPIC,
                Map.of(Enchantment.PROTECTION_ENVIRONMENTAL, 2), 1)
        );
        ITEMS.put(ItemRarity.EPIC, epicItems);
    }

    public static Object getRandomItem() {
        // Select rarity based on chances
        int roll = new Random().nextInt(100);
        ItemRarity selectedRarity;
        
        if (roll < ItemRarity.EPIC.getChance()) {
            selectedRarity = ItemRarity.EPIC;
        } else if (roll < ItemRarity.EPIC.getChance() + ItemRarity.RARE.getChance()) {
            selectedRarity = ItemRarity.RARE;
        } else {
            selectedRarity = ItemRarity.COMMON;
        }
        
        // Get random item of selected rarity
        List<GameItem> items = ITEMS.get(selectedRarity);
        GameItem selectedItem = items.get(new Random().nextInt(items.size()));
        
        ItemStack item = selectedItem.create();
        
        // Add arrows if bow/crossbow
        if (item.getType() == Material.BOW || item.getType() == Material.CROSSBOW) {
            return new ItemStack[]{item, new ItemStack(Material.ARROW, 16)};
        }
        
        return item;
    }
}