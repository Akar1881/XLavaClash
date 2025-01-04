package com.xlavaclash.cooldown;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ButtonCooldown {
    private final Map<UUID, Long> cooldowns = new HashMap<>();
    private final long cooldownTime;

    public ButtonCooldown(long cooldownTime) {
        this.cooldownTime = cooldownTime;
    }

    public boolean isOnCooldown(UUID playerId) {
        if (!cooldowns.containsKey(playerId)) return false;
        
        long timeElapsed = System.currentTimeMillis() - cooldowns.get(playerId);
        return timeElapsed < cooldownTime;
    }

    public void setCooldown(UUID playerId) {
        cooldowns.put(playerId, System.currentTimeMillis());
    }

    public long getRemainingTime(UUID playerId) {
        if (!cooldowns.containsKey(playerId)) return 0;
        
        long timeElapsed = System.currentTimeMillis() - cooldowns.get(playerId);
        long remaining = cooldownTime - timeElapsed;
        return Math.max(0, remaining);
    }

    public void removeCooldown(UUID playerId) {
        cooldowns.remove(playerId);
    }
}