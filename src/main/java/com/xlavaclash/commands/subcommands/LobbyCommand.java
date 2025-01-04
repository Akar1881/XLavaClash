package com.xlavaclash.commands.subcommands;

import com.xlavaclash.XLavaClash;
import com.xlavaclash.commands.SubCommand;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class LobbyCommand extends SubCommand {
    private final XLavaClash plugin;

    public LobbyCommand(XLavaClash plugin) {
        super("lobby", "xlavaclash.play", "/xlc lobby");
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cThis command can only be used by players!");
            return true;
        }

        Player player = (Player) sender;
        Location lobbyLocation = plugin.getConfigManager().getLobbyLocation();
        
        if (lobbyLocation != null) {
            player.teleport(lobbyLocation);
            player.sendMessage("§aWelcome to the lobby!");
        } else {
            player.sendMessage("§cLobby location has not been set!");
        }
        
        return true;
    }

    @Override
    public List<String> getTabCompletions(CommandSender sender, String[] args) {
        return List.of();
    }
}