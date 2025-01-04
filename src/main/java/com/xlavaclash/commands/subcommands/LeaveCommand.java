package com.xlavaclash.commands.subcommands;

import com.xlavaclash.XLavaClash;
import com.xlavaclash.commands.SubCommand;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class LeaveCommand extends SubCommand {
    private final XLavaClash plugin;

    public LeaveCommand(XLavaClash plugin) {
        super("leave", "xlavaclash.play", "/xlc leave");
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cThis command can only be used by players!");
            return true;
        }

        Player player = (Player) sender;
        plugin.getQueueManager().removePlayer(player);
        
        // Get global lobby location
        Location lobbyLocation = plugin.getConfigManager().getLobbyLocation();
        if (lobbyLocation != null) {
            player.teleport(lobbyLocation);
            player.sendMessage("§aYou left all queues and were teleported to the lobby!");
        } else {
            player.sendMessage("§aYou left all queues!");
        }
        
        return true;
    }

    @Override
    public List<String> getTabCompletions(CommandSender sender, String[] args) {
        return List.of();
    }
}