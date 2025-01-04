package com.xlavaclash.managers;

import com.xlavaclash.XLavaClash;
import com.xlavaclash.models.PlayerRank;
import com.xlavaclash.models.PlayerStats;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class XpManager {
    private final XLavaClash plugin;
    private final Map<UUID, PlayerRank> playerRanks;
    private final Map<UUID, PlayerStats> playerStats;
    private final Map<UUID, PlayerStats> gameStats;
    private final File rankFile;
    private final File statsFile;

    // XP rewards configuration
    private static final int XP_PER_KILL = 50;
    private static final int XP_PER_DAMAGE = 1;
    private static final int XP_PER_MINUTE_SURVIVED = 10;
    private static final int XP_PARTICIPATION = 10;
    private static final int XP_WIN = 100;
    private static final int XP_LOSS = 25;

    public XpManager(XLavaClash plugin) {
        this.plugin = plugin;
        this.playerRanks = new HashMap<>();
        this.playerStats = new HashMap<>();
        this.gameStats = new HashMap<>();
        this.rankFile = new File(plugin.getDataFolder(), "ranks.yml");
        this.statsFile = new File(plugin.getDataFolder(), "stats.yml");
        loadData();
    }

    private void loadData() {
        loadRanks();
        loadStats();
    }

    private void loadRanks() {
        if (!rankFile.exists()) {
            plugin.saveResource("ranks.yml", false);
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(rankFile);
        for (String uuidStr : config.getKeys(false)) {
            UUID uuid = UUID.fromString(uuidStr);
            int rank = config.getInt(uuidStr + ".rank", 1);
            int xp = config.getInt(uuidStr + ".xp", 0);
            playerRanks.put(uuid, new PlayerRank(rank, xp));
        }
    }

    private void loadStats() {
        if (!statsFile.exists()) {
            try {
                statsFile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("Could not create stats file!");
                return;
            }
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(statsFile);
        for (String uuidStr : config.getKeys(false)) {
            UUID uuid = UUID.fromString(uuidStr);
            PlayerStats stats = new PlayerStats();
            config.getInt(uuidStr + ".wins", 0);
            config.getInt(uuidStr + ".losses", 0);
            playerStats.put(uuid, stats);
        }
    }

    public void saveData() {
        saveRanks();
        saveStats();
    }

    private void saveRanks() {
        FileConfiguration config = new YamlConfiguration();
        for (Map.Entry<UUID, PlayerRank> entry : playerRanks.entrySet()) {
            String path = entry.getKey().toString();
            PlayerRank rank = entry.getValue();
            config.set(path + ".rank", rank.getRank());
            config.set(path + ".xp", rank.getCurrentXp());
        }

        try {
            config.save(rankFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save ranks file!");
            e.printStackTrace();
        }
    }

    private void saveStats() {
        FileConfiguration config = new YamlConfiguration();
        for (Map.Entry<UUID, PlayerStats> entry : playerStats.entrySet()) {
            String path = entry.getKey().toString();
            PlayerStats stats = entry.getValue();
            config.set(path + ".wins", stats.getWins());
            config.set(path + ".losses", stats.getLosses());
        }

        try {
            config.save(statsFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save stats file!");
            e.printStackTrace();
        }
    }

    public void addWin(Player player) {
        PlayerStats stats = getPlayerStats(player);
        stats.addWin();
        awardXp(player, XP_WIN);
        saveData();
    }

    public void addLoss(Player player) {
        PlayerStats stats = getPlayerStats(player);
        stats.addLoss();
        awardXp(player, XP_LOSS);
        saveData();
    }

    public PlayerStats getPlayerStats(Player player) {
        return playerStats.computeIfAbsent(player.getUniqueId(), k -> new PlayerStats());
    }

    public Map<Integer, String> getTopPlayersByRank(int limit) {
        Map<Integer, String> topPlayers = new LinkedHashMap<>();
        playerRanks.entrySet().stream()
            .sorted((e1, e2) -> Integer.compare(e2.getValue().getRank(), e1.getValue().getRank()))
            .limit(limit)
            .forEach(e -> topPlayers.put(
                e.getValue().getRank(),
                plugin.getServer().getOfflinePlayer(e.getKey()).getName()
            ));
        return topPlayers;
    }

    public Map<Integer, String> getTopPlayersByWins(int limit) {
        Map<Integer, String> topPlayers = new LinkedHashMap<>();
        playerStats.entrySet().stream()
            .sorted((e1, e2) -> Integer.compare(e2.getValue().getWins(), e1.getValue().getWins()))
            .limit(limit)
            .forEach(e -> topPlayers.put(
                e.getValue().getWins(),
                plugin.getServer().getOfflinePlayer(e.getKey()).getName()
            ));
        return topPlayers;
    }

    public Map<Integer, Map.Entry<String, Double>> getTopPlayersByWinLossRatio(int limit) {
        Map<Integer, Map.Entry<String, Double>> topPlayers = new LinkedHashMap<>();
        int[] rank = {1};
        playerStats.entrySet().stream()
            .filter(e -> (e.getValue().getWins() + e.getValue().getLosses()) >= 10)
            .map(e -> new AbstractMap.SimpleEntry<>(
                plugin.getServer().getOfflinePlayer(e.getKey()).getName(),
                calculateWinLossRatio(e.getValue())
            ))
            .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
            .limit(limit)
            .forEach(e -> topPlayers.put(rank[0]++, e));
        return topPlayers;
    }

    private double calculateWinLossRatio(PlayerStats stats) {
        if (stats.getLosses() == 0) return stats.getWins();
        return (double) stats.getWins() / stats.getLosses();
    }


    public void initializeGameStats(Player player) {
        gameStats.put(player.getUniqueId(), new PlayerStats());
    }

    public void addKill(Player player) {
        PlayerStats stats = gameStats.get(player.getUniqueId());
        if (stats != null) {
            stats.addKill();
        }
    }

    public void addDamage(Player player, int damage) {
        PlayerStats stats = gameStats.get(player.getUniqueId());
        if (stats != null) {
            stats.addDamage(damage);
        }
    }

    public void setSurvivalTime(Player player, long survivalTime) {
        PlayerStats stats = gameStats.get(player.getUniqueId());
        if (stats != null) {
            stats.setSurvivalTime(survivalTime);
        }
    }

    public void calculateAndAwardXp(Player player) {
        PlayerStats stats = gameStats.get(player.getUniqueId());
        if (stats == null) return;

        int totalXp = 0;

        // Calculate XP from kills
        totalXp += stats.getKills() * XP_PER_KILL;

        // Calculate XP from damage
        totalXp += (stats.getDamageDealt() / 100) * XP_PER_DAMAGE;

        // Calculate XP from survival time (in minutes)
        totalXp += (stats.getSurvivalTime() / 60) * XP_PER_MINUTE_SURVIVED;

        // Add participation XP
        if (stats.hasParticipated()) {
            totalXp += XP_PARTICIPATION;
        }

        // Award XP and send message
        awardXp(player, totalXp);
        
        // Clear game stats
        gameStats.remove(player.getUniqueId());
    }

    public void awardXp(Player player, int xp) {
        PlayerRank rank = getPlayerRank(player);
        int oldRank = rank.getRank();
        rank.addXp(xp);
        
        player.sendMessage(String.format("§a+%d XP", xp));
        
        if (rank.getRank() > oldRank) {
            // Rank up message
            player.sendMessage(String.format("§6§lRANK UP! §eYou are now rank %d!", rank.getRank()));
        }
        
        saveRanks();
    }

    public PlayerRank getPlayerRank(Player player) {
        return playerRanks.computeIfAbsent(player.getUniqueId(), k -> new PlayerRank());
    }
}