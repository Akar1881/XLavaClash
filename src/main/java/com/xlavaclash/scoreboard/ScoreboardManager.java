package com.xlavaclash.scoreboard;

import com.xlavaclash.XLavaClash;
import com.xlavaclash.models.PlayerRank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ScoreboardManager {
    private final XLavaClash plugin;
    private FileConfiguration scoreboardConfig;
    private String title;
    private List<String> lines;
    private int updateInterval;

    public ScoreboardManager(XLavaClash plugin) {
        this.plugin = plugin;
        loadConfig();
        startUpdateTask();
    }

    public void loadConfig() {
        File configFile = new File(plugin.getDataFolder(), "scoreboard.yml");
        if (!configFile.exists()) {
            plugin.saveResource("scoreboard.yml", false);
        }
        
        scoreboardConfig = YamlConfiguration.loadConfiguration(configFile);
        title = ChatColor.translateAlternateColorCodes('&', scoreboardConfig.getString("title", "&6&lYour Server Name"));
        lines = new ArrayList<>();
        scoreboardConfig.getStringList("lines").forEach(line -> 
            lines.add(ChatColor.translateAlternateColorCodes('&', line)));
        updateInterval = scoreboardConfig.getInt("update-interval", 20);
    }

    private void startUpdateTask() {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            String lobbyWorld = plugin.getConfigManager().getLobbyWorld();
            if (lobbyWorld == null) return;

            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getWorld().getName().equals(lobbyWorld)) {
                    updateScoreboard(player);
                }
            }
        }, 20L, updateInterval);
    }

    public void updateScoreboard(Player player) {
        org.bukkit.scoreboard.ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getNewScoreboard();
        Objective obj = board.registerNewObjective("lobby", "dummy", title);
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        int score = lines.size();
        for (String line : lines) {
            String processedLine = processPlaceholders(line, player);
            Score lineScore = obj.getScore(processedLine);
            lineScore.setScore(score--);
        }

        player.setScoreboard(board);
    }

    private String processPlaceholders(String line, Player player) {
        // Get player stats and rank
        PlayerRank rank = plugin.getXpManager().getPlayerRank(player);
        
        // Replace placeholders
        return line.replace("%xlc_onlines%", String.valueOf(getTotalPlayers()))
                  .replace("%player_rank%", String.valueOf(rank.getRank()))
                  .replace("%player_xp%", String.valueOf(rank.getCurrentXp()))
                  .replace("%require_xp%", String.valueOf(rank.getXpToNextRank()))
                  .replace("%player_wins%", String.valueOf(plugin.getXpManager().getPlayerStats(player).getWins()))
                  .replace("%player_loses%", String.valueOf(plugin.getXpManager().getPlayerStats(player).getLosses()));
    }

    private int getTotalPlayers() {
        int total = 0;
        String lobbyWorld = plugin.getConfigManager().getLobbyWorld();
        
        // Count players in lobby
        if (lobbyWorld != null) {
            total += Bukkit.getWorld(lobbyWorld).getPlayers().size();
        }
        
        // Count players in games
        total += plugin.getGameManager().getActiveGames().values().stream()
            .mapToInt(game -> game.getPlayers().size())
            .sum();
            
        // Count players in queues
        total += plugin.getQueueManager().getPlayersInQueue();
        
        return total;
    }
}