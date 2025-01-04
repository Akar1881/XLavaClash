package com.xlavaclash.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;

public class LobbyItems {
    public static ItemStack createMapSelector() {
        ItemStack item = new ItemStack(Material.COMPASS);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§6Map Selector");
        meta.setLore(Arrays.asList(
            "§7Click to open the map selection menu",
            "§7and join a game!"
        ));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createPlayerHead(Player player) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setDisplayName("§6Your Profile");
        meta.setLore(Arrays.asList(
            "§7Click to view your statistics",
            "§7and rankings!"
        ));
        meta.setOwningPlayer(player);
        item.setItemMeta(meta);
        return item;
    }

    public static boolean isMapSelector(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        return item.getType() == Material.COMPASS && 
               item.getItemMeta().getDisplayName().equals("§6Map Selector");
    }

    public static boolean isPlayerHead(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        return item.getType() == Material.PLAYER_HEAD && 
               item.getItemMeta().getDisplayName().equals("§6Your Profile");
    }

    public static void giveLobbyItems(Player player) {
        player.getInventory().setItem(0, createMapSelector());
        player.getInventory().setItem(8, createPlayerHead(player));
    }
}