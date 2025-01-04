package com.xlavaclash.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.List;

public class ButtonWand {
    public static ItemStack create() {
        ItemStack wand = new ItemStack(Material.GOLDEN_SHOVEL);
        ItemMeta meta = wand.getItemMeta();
        meta.setDisplayName("§6Button Wand");
        meta.setLore(List.of("§7Break a button to set it as the game button"));
        wand.setItemMeta(meta);
        return wand;
    }

    public static boolean isButtonWand(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        return item.getType() == Material.GOLDEN_SHOVEL && 
               "§6Button Wand".equals(item.getItemMeta().getDisplayName());
    }
}