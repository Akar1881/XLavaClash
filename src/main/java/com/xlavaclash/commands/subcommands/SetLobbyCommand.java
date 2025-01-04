package com.xlavaclash.commands.subcommands;

import com.xlavaclash.XLavaClash;
import com.xlavaclash.commands.SubCommand;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class SetLobbyCommand extends SubCommand {
    private final XLavaClash plugin;

    public SetLobbyCommand(XLavaClash plugin) {
        super("setlobby", "xlavaclash.admin", "/xlc setlobby");
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cThis command can only be used by players!");
            return true;
        }

        Player player = (Player) sender;
        Location location = player.getLocation();
        
        plugin.getConfigManager().getConfig().set("lobby.world", location.getWorld().getName());
        plugin.getConfigManager().getConfig().set("lobby.x", location.getX());
        plugin.getConfigManager().getConfig().set("lobby.y", location.getY());
        plugin.getConfigManager().getConfig().set("lobby.z", location.getZ());
        plugin.getConfigManager().getConfig().set("lobby.yaw", location.getYaw());
        plugin.getConfigManager().getConfig().set("lobby.pitch", location.getPitch());
        plugin.getConfigManager().saveConfig();

        sender.sendMessage("§aGlobal lobby location has been set!");
        return true;
    }

    @Override
    public List<String> getTabCompletions(CommandSender sender, String[] args) {
        return List.of();
    }
}