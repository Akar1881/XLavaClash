package com.xlavaclash.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class QueueItems {
    public static ItemStack createLeaveItem(String mapName) {
        ItemStack item = new ItemStack(Material.BARRIER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§cGo Back To Lobby");
        meta.setLore(Arrays.asList(
            "§7Currently in queue for: §f" + mapName,
            "§7Click to leave the queue"
        ));
        item.setItemMeta(meta);
        return item;
    }

    public static boolean isLeaveItem(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        return item.getType() == Material.BARRIER && 
               item.getItemMeta().getDisplayName().equals("§cGo Back To Lobby");
    }
}