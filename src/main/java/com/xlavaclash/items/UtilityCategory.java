package com.xlavaclash.items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class UtilityCategory implements ItemCategory {
    private static final Random random = new Random();
    
    private static final List<Material> UTILITY_ITEMS = Arrays.asList(
        Material.GOLDEN_APPLE,
        Material.ENDER_PEARL,
        Material.COBWEB,
        Material.TNT,
        Material.WATER_BUCKET,
        Material.LAVA_BUCKET,
        Material.SHIELD
    );
    
    private static final List<Integer> POSSIBLE_AMOUNTS = Arrays.asList(1, 2, 4, 8, 16);

    @Override
    public ItemStack generateItem() {
        Material material = UTILITY_ITEMS.get(random.nextInt(UTILITY_ITEMS.size()));
        int amount = POSSIBLE_AMOUNTS.get(random.nextInt(POSSIBLE_AMOUNTS.size()));
        
        return new ItemStack(material, amount);
    }
}