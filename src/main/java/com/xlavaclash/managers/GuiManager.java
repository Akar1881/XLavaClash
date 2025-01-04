package com.xlavaclash.managers;

import com.xlavaclash.XLavaClash;
import com.xlavaclash.gui.MapSelectionGui;
import com.xlavaclash.gui.TeamSelectionGui;
import org.bukkit.entity.Player;

public class GuiManager {
    private final XLavaClash plugin;

    public GuiManager(XLavaClash plugin) {
        this.plugin = plugin;
    }

    public void openMapSelection(Player player) {
        new MapSelectionGui(plugin).open(player);
    }

    public void openTeamSelection(Player player, String mapName) {
        if (!plugin.getGameManager().hasActiveGame(mapName)) {
            new TeamSelectionGui(plugin, mapName).open(player);
        } else {
            player.sendMessage("§cThis map currently has an active game!");
        }
    }
}