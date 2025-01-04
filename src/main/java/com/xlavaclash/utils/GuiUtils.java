package com.xlavaclash.utils;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class GuiUtils {
    private static final NamespacedKey MAP_KEY = new NamespacedKey("xlavaclash", "map");
    private static final NamespacedKey TEAM_KEY = new NamespacedKey("xlavaclash", "team");
    private static final NamespacedKey INV_MAP_KEY = new NamespacedKey("xlavaclash", "inv_map");

    public static void fillEmptySlots(Inventory inventory) {
        ItemStack filler = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta meta = filler.getItemMeta();
        meta.setDisplayName(" ");
        filler.setItemMeta(meta);

        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, filler);
            }
        }
    }

    public static void setMapData(ItemStack item, String mapName) {
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(MAP_KEY, PersistentDataType.STRING, mapName);
        item.setItemMeta(meta);
    }

    public static void setMapData(Inventory inventory, String mapName) {
        // Create a hidden item to store the map data
        ItemStack dataItem = new ItemStack(Material.BARRIER);
        ItemMeta meta = dataItem.getItemMeta();
        meta.getPersistentDataContainer().set(INV_MAP_KEY, PersistentDataType.STRING, mapName);
        dataItem.setItemMeta(meta);
        
        // Store it in the last slot
        inventory.setItem(inventory.getSize() - 1, dataItem);
    }

    public static void setTeamData(ItemStack item, String team) {
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(TEAM_KEY, PersistentDataType.STRING, team);
        item.setItemMeta(meta);
    }

    public static String getMapData(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return null;
        return item.getItemMeta().getPersistentDataContainer()
            .get(MAP_KEY, PersistentDataType.STRING);
    }

    public static String getTeamData(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return null;
        return item.getItemMeta().getPersistentDataContainer()
            .get(TEAM_KEY, PersistentDataType.STRING);
    }

    public static String getMapData(Inventory inventory) {
        ItemStack dataItem = inventory.getItem(inventory.getSize() - 1);
        if (dataItem != null && dataItem.hasItemMeta()) {
            return dataItem.getItemMeta().getPersistentDataContainer()
                .get(INV_MAP_KEY, PersistentDataType.STRING);
        }
        return null;
    }
}