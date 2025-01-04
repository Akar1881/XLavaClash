package com.xlavaclash;

import com.xlavaclash.commands.XLavaClashCommand;
import com.xlavaclash.listeners.*;
import com.xlavaclash.managers.*;
import com.xlavaclash.scoreboard.ScoreboardManager;
import com.xlavaclash.utils.InventoryManager;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class XLavaClash extends JavaPlugin {
    private static XLavaClash instance;
    private MapManager mapManager;
    private GameManager gameManager;
    private QueueManager queueManager;
    private ConfigManager configManager;
    private GuiManager guiManager;
    private InventoryManager inventoryManager;
    private XpManager xpManager;
    private ScoreboardManager scoreboardManager;
    private SpectateManager spectateManager;

    @Override
    public void onEnable() {
        instance = this;
        
        // Initialize managers
        this.configManager = new ConfigManager(this);
        this.inventoryManager = new InventoryManager();
        this.mapManager = new MapManager(this);
        this.gameManager = new GameManager(this);
        this.queueManager = new QueueManager(this);
        this.xpManager = new XpManager(this);
        this.guiManager = new GuiManager(this);
        this.scoreboardManager = new ScoreboardManager(this);
        this.spectateManager = new SpectateManager();

        // Register commands
        getCommand("xlc").setExecutor(new XLavaClashCommand(this));
        getCommand("xlc").setTabCompleter(new XLavaClashCommand(this));
        
        // Register listeners
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerListener(this), this);
        pm.registerEvents(new GameListener(this), this);
        pm.registerEvents(new PlayerStatusGuiListener(this), this);
        pm.registerEvents(new ButtonListener(this), this);
        pm.registerEvents(new EndGameListener(this), this);
        pm.registerEvents(new MapSelectionListener(this), this);
        pm.registerEvents(new TeamSelectionListener(this), this);
        pm.registerEvents(new QueueItemListener(this), this);
        pm.registerEvents(new LobbyItemListener(this), this);
    }

    @Override
    public void onDisable() {
        if (gameManager != null) {
            gameManager.stopAllGames();
        }
        if (xpManager != null) {
            xpManager.saveData();
        }
    }

    public void reload() {
        reloadConfig();
        configManager = new ConfigManager(this);
        scoreboardManager.loadConfig();
    }

    public static XLavaClash getInstance() {
        return instance;
    }

    public XpManager getXpManager() {
        return xpManager;
    }

    public MapManager getMapManager() {
        return mapManager;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public QueueManager getQueueManager() {
        return queueManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public GuiManager getGuiManager() {
        return guiManager;
    }

    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }

    public ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }

    public SpectateManager getSpectateManager() {
        return spectateManager;
    }
}