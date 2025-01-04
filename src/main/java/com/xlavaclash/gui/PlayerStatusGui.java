package com.xlavaclash.gui;

import com.xlavaclash.XLavaClash;
import com.xlavaclash.models.PlayerRank;
import com.xlavaclash.models.PlayerStats;
import com.xlavaclash.utils.GuiUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class PlayerStatusGui {
    private final XLavaClash plugin;
    private final Inventory inventory;
    private final Player player;

    public PlayerStatusGui(XLavaClash plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        this.inventory = Bukkit.createInventory(null, 27, "§8Your Statistics");
        setupItems();
    }

    private void setupItems() {
        GuiUtils.fillEmptySlots(inventory);

        PlayerStats stats = plugin.getXpManager().getPlayerStats(player);
        PlayerRank rank = plugin.getXpManager().getPlayerRank(player);

        // Wins (Diamond Sword)
        inventory.setItem(11, createStatItem(Material.DIAMOND_SWORD,
            "§aYour Wins", stats.getWins(),
            "§7Total victories in LavaClash"));

        // Losses (Shield)
        inventory.setItem(13, createStatItem(Material.SHIELD,
            "§cYour Losses", stats.getLosses(),
            "§7Total defeats in LavaClash"));

        // Rankings Book
        inventory.setItem(15, createRankingsBook());
    }

    private ItemStack createStatItem(Material material, String name, int value, String description) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        List<String> lore = new ArrayList<>();
        lore.add("§f" + value);
        lore.add("");
        lore.add(description);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack createRankingsBook() {
        ItemStack item = new ItemStack(Material.BOOK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§6Rankings");
        List<String> lore = new ArrayList<>();
        lore.add("§7Click to view the top players");
        lore.add("§7in different categories!");
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public void showRankings(Player player) {
        player.closeInventory();
        player.sendMessage("§6=== Top Players ===");
        
        // Top 10 by Rank
        player.sendMessage("\n§e=== Top 10 by Rank ===");
        plugin.getXpManager().getTopPlayersByRank(10).forEach((rank, name) ->
            player.sendMessage(String.format("§7%d. §f%s §7(Rank: §6%d§7)", rank, name, rank)));
        
        // Top 10 by Wins
        player.sendMessage("\n§e=== Top 10 by Wins ===");
        plugin.getXpManager().getTopPlayersByWins(10).forEach((rank, name) ->
            player.sendMessage(String.format("§7%d. §f%s §7(Wins: §a%d§7)", rank, name, rank)));
        
        // Top 10 by Win/Loss Ratio (minimum 10 games)
        player.sendMessage("\n§e=== Top 10 by Win/Loss Ratio ===");
        plugin.getXpManager().getTopPlayersByWinLossRatio(10).forEach((rank, entry) ->
            player.sendMessage(String.format("§7%d. §f%s §7(Ratio: §6%.2f§7)", 
                rank, entry.getKey(), entry.getValue())));
    }

    public void open() {
        player.openInventory(inventory);
    }
}