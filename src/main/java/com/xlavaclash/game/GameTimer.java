package com.xlavaclash.game;

import com.xlavaclash.XLavaClash;
import com.xlavaclash.models.GameState;
import org.bukkit.scheduler.BukkitRunnable;

public class GameTimer {
    private final XLavaClash plugin;
    private final Game game;
    private BukkitRunnable countdownTask;
    private BukkitRunnable gameTask;
    private static final int COUNTDOWN_TIME = 30;

    public GameTimer(XLavaClash plugin, Game game) {
        this.plugin = plugin;
        this.game = game;
    }

    public void startCountdown() {
        game.setState(GameState.COUNTDOWN);
        int countdownSeconds = COUNTDOWN_TIME;

        countdownTask = new BukkitRunnable() {
            private int timeLeft = countdownSeconds;

            @Override
            public void run() {
                if (timeLeft <= 0) {
                    cancel();
                    startGame();
                    return;
                }

                if (timeLeft <= 5 || timeLeft % 10 == 0) {
                    game.broadcast("§eGame starting in §6" + timeLeft + " §eseconds!");
                }

                timeLeft--;
            }
        };

        countdownTask.runTaskTimer(plugin, 0L, 20L);
    }

    public void cancelCountdown() {
        if (countdownTask != null) {
            countdownTask.cancel();
            countdownTask = null;
            game.setState(GameState.WAITING);
        }
    }

    private void startGame() {
        game.setState(GameState.PLAYING);
        game.teleportPlayers();
        startGameTimer();
    }

    private void startGameTimer() {
        gameTask = new BukkitRunnable() {
            @Override
            public void run() {
                // Game logic here if needed
            }
        };

        gameTask.runTaskTimer(plugin, 0L, 20L);
    }

    public void stop() {
        if (countdownTask != null) {
            countdownTask.cancel();
        }
        if (gameTask != null) {
            gameTask.cancel();
        }
    }
}