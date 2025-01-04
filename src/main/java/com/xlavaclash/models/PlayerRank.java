package com.xlavaclash.models;

public class PlayerRank {
    private int rank;
    private int currentXp;
    private int xpToNextRank;
    private static final int MAX_RANK = 500;
    private static final int BASE_XP = 500;

    public PlayerRank() {
        this.rank = 1;
        this.currentXp = 0;
        this.xpToNextRank = BASE_XP;
    }

    public PlayerRank(int rank, int currentXp) {
        this.rank = rank;
        this.currentXp = currentXp;
        this.xpToNextRank = calculateXpForRank(rank + 1);
    }

    public static int calculateXpForRank(int targetRank) {
        if (targetRank <= 1) return 0;
        if (targetRank == 2) return BASE_XP;

        double xp = BASE_XP;
        for (int i = 2; i < targetRank; i++) {
            xp *= 1.5;
        }
        return (int) xp;
    }

    public void addXp(int xp) {
        this.currentXp += xp;
        checkRankUp();
    }

    private void checkRankUp() {
        while (currentXp >= xpToNextRank && rank < MAX_RANK) {
            currentXp -= xpToNextRank;
            rank++;
            xpToNextRank = calculateXpForRank(rank + 1);
        }

        if (rank >= MAX_RANK) {
            rank = MAX_RANK;
            currentXp = 0;
            xpToNextRank = 0;
        }
    }

    public int getRank() {
        return rank;
    }

    public int getCurrentXp() {
        return currentXp;
    }

    public int getXpToNextRank() {
        return xpToNextRank;
    }

    public boolean isMaxRank() {
        return rank >= MAX_RANK;
    }
}