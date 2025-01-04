package com.xlavaclash.commands.subcommands;

import com.xlavaclash.XLavaClash;
import com.xlavaclash.commands.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class CreateCommand extends SubCommand {
    private final XLavaClash plugin;

    public CreateCommand(XLavaClash plugin) {
        super("create", "xlavaclash.admin", "/xlc create <mapname> <worldname>");
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length != 2) {
            sender.sendMessage("§cUsage: " + usage);
            return true;
        }

        String mapName = args[0];
        String worldName = args[1];

        if (plugin.getMapManager().createMap(mapName, worldName)) {
            sender.sendMessage("§aMap '" + mapName + "' created successfully!");
        } else {
            sender.sendMessage("§cA map with that name already exists!");
        }

        return true;
    }

    @Override
    public List<String> getTabCompletions(CommandSender sender, String[] args) {
        if (args.length == 2) {
            String input = args[1].toLowerCase();
            return Bukkit.getWorlds().stream()
                    .map(World::getName)
                    .filter(name -> name.toLowerCase().startsWith(input))
                    .collect(Collectors.toList());
        }
        return List.of();
    }
}