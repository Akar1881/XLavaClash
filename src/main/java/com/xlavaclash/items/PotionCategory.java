package com.xlavaclash.items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class PotionCategory implements ItemCategory {
    private static final Random random = new Random();
    
    private static final List<PotionEffectType> POTION_EFFECTS = Arrays.asList(
        PotionEffectType.SPEED,
        PotionEffectType.JUMP,
        PotionEffectType.HEAL,
        PotionEffectType.DAMAGE_RESISTANCE,
        PotionEffectType.INCREASE_DAMAGE
    );

    @Override
    public ItemStack generateItem() {
        ItemStack potion = new ItemStack(Material.SPLASH_POTION);
        PotionMeta meta = (PotionMeta) potion.getItemMeta();
        
        PotionEffectType effectType = POTION_EFFECTS.get(random.nextInt(POTION_EFFECTS.size()));
        int duration = (random.nextInt(20) + 10) * 20; // 10-30 seconds
        int amplifier = random.nextInt(2); // 0-1 (Level I or II)
        
        meta.addCustomEffect(new PotionEffect(effectType, duration, amplifier), true);
        potion.setItemMeta(meta);
        
        return potion;
    }
}