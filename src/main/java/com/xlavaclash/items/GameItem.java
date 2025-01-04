package com.xlavaclash.items;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameItem {
    private final Material material;
    private final ItemRarity rarity;
    private final Map<Enchantment, Integer> enchantments;
    private final int amount;

    public GameItem(Material material, ItemRarity rarity, Map<Enchantment, Integer> enchantments, int amount) {
        this.material = material;
        this.rarity = rarity;
        this.enchantments = enchantments;
        this.amount = amount;
    }

    public ItemStack create() {
        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();
        
        String displayName = rarity.getColor() + formatMaterialName(material.name());
        meta.setDisplayName(displayName);
        
        List<String> lore = new ArrayList<>();
        lore.add(rarity.getColor() + "✦ " + formatMaterialName(rarity.name()) + " Item");
        meta.setLore(lore);
        
        enchantments.forEach((enchant, level) -> meta.addEnchant(enchant, level, true));
        item.setItemMeta(meta);
        
        return item;
    }

    private String formatMaterialName(String name) {
        return name.substring(0, 1).toUpperCase() + 
               name.substring(1).toLowerCase().replace("_", " ");
    }
}