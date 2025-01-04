package com.xlavaclash.commands.subcommands;

import com.xlavaclash.XLavaClash;
import com.xlavaclash.commands.SubCommand;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.stream.Collectors;

public class DeleteCommand extends SubCommand {
    private final XLavaClash plugin;

    public DeleteCommand(XLavaClash plugin) {
        super("delete", "xlavaclash.admin", "/xlc delete <mapname>");
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length != 1) {
            sender.sendMessage("§cUsage: " + usage);
            return true;
        }

        String mapName = args[0];
        if (plugin.getMapManager().deleteMap(mapName)) {
            sender.sendMessage("§aMap '" + mapName + "' deleted successfully!");
        } else {
            sender.sendMessage("§cMap not found!");
        }

        return true;
    }

    @Override
    public List<String> getTabCompletions(CommandSender sender, String[] args) {
        if (args.length == 1) {
            String input = args[0].toLowerCase();
            return plugin.getMapManager().getActiveMaps().keySet().stream()
                    .filter(name -> name.toLowerCase().startsWith(input))
                    .collect(Collectors.toList());
        }
        return List.of();
    }
}