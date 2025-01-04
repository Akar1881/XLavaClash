package com.xlavaclash.utils;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InventoryManager {
    private final Map<UUID, ItemStack[]> savedInventories = new HashMap<>();
    private final Map<UUID, ItemStack[]> savedArmor = new HashMap<>();

    public void saveInventory(Player player) {
        UUID playerId = player.getUniqueId();
        savedInventories.put(playerId, player.getInventory().getContents());
        savedArmor.put(playerId, player.getInventory().getArmorContents());
        
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
    }

    public void restoreInventory(Player player) {
        UUID playerId = player.getUniqueId();
        if (savedInventories.containsKey(playerId)) {
            player.getInventory().setContents(savedInventories.get(playerId));
            player.getInventory().setArmorContents(savedArmor.get(playerId));
            
            savedInventories.remove(playerId);
            savedArmor.remove(playerId);
        }
    }

    public boolean hasStoredInventory(Player player) {
        return savedInventories.containsKey(player.getUniqueId());
    }
}