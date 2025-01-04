package com.xlavaclash.commands.subcommands;

import com.xlavaclash.XLavaClash;
import com.xlavaclash.commands.SubCommand;
import com.xlavaclash.models.PlayerRank;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class RankCommand extends SubCommand {
    private final XLavaClash plugin;

    public RankCommand(XLavaClash plugin) {
        super("rank", "xlavaclash.play", "/xlc rank");
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cThis command can only be used by players!");
            return true;
        }

        Player player = (Player) sender;
        PlayerRank rank = plugin.getXpManager().getPlayerRank(player);

        sender.sendMessage("§6=== Your Rank Info ===");
        sender.sendMessage(String.format("§eRank: §f%d", rank.getRank()));
        
        if (!rank.isMaxRank()) {
            sender.sendMessage(String.format("§eProgress: §f%d§7/§f%d XP", 
                rank.getCurrentXp(), rank.getXpToNextRank()));
            sender.sendMessage(String.format("§eNeeded for next rank: §f%d XP", 
                rank.getXpToNextRank() - rank.getCurrentXp()));
        } else {
            sender.sendMessage("§6§lMAX RANK ACHIEVED!");
        }

        return true;
    }

    @Override
    public List<String> getTabCompletions(CommandSender sender, String[] args) {
        return List.of();
    }
}