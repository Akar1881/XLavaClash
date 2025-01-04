package com.xlavaclash.managers;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SpectateManager {
    private final Map<UUID, PlayerState> playerStates = new HashMap<>();

    public void storePlayerState(Player player) {
        PlayerState state = new PlayerState(
            player.getLocation(),
            player.getGameMode()
        );
        playerStates.put(player.getUniqueId(), state);
    }

    public boolean restorePlayerState(Player player) {
        PlayerState state = playerStates.remove(player.getUniqueId());
        if (state == null) {
            return false;
        }

        player.teleport(state.location());
        player.setGameMode(state.gameMode());
        return true;
    }

    public boolean isSpectating(Player player) {
        return playerStates.containsKey(player.getUniqueId());
    }

    private record PlayerState(Location location, GameMode gameMode) {}
}