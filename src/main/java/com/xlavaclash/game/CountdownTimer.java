package com.xlavaclash.game;

import com.xlavaclash.XLavaClash;
import org.bukkit.scheduler.BukkitRunnable;

public class CountdownTimer extends BukkitRunnable {
    private final XLavaClash plugin;
    private final Game game;
    private int timeLeft;
    private final Runnable onComplete;

    public CountdownTimer(XLavaClash plugin, Game game, int seconds, Runnable onComplete) {
        this.plugin = plugin;
        this.game = game;
        this.timeLeft = seconds;
        this.onComplete = onComplete;
    }

    @Override
    public void run() {
        if (timeLeft <= 0) {
            cancel();
            onComplete.run();
            return;
        }

        if (timeLeft <= 5 || timeLeft % 10 == 0) {
            game.broadcast("§eGame starting in §6" + timeLeft + " §eseconds!");
        }

        timeLeft--;
    }

    public void start() {
        runTaskTimer(plugin, 0L, 20L);
    }
}