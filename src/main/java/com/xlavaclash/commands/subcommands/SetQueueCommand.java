package com.xlavaclash.commands.subcommands;

import com.xlavaclash.XLavaClash;
import com.xlavaclash.commands.SubCommand;
import com.xlavaclash.models.GameMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class SetQueueCommand extends SubCommand {
    private final XLavaClash plugin;

    public SetQueueCommand(XLavaClash plugin) {
        super("setqueue", "xlavaclash.admin", "/xlc setqueue <mapname>");
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cThis command can only be used by players!");
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage("§cUsage: " + usage);
            return true;
        }

        Player player = (Player) sender;
        String mapName = args[0];
        
        plugin.getMapManager().getMap(mapName).ifPresentOrElse(
            map -> {
                map.setQueueLobby(player.getLocation());
                sender.sendMessage("§aQueue lobby location set for map '" + mapName + "'!");
            },
            () -> sender.sendMessage("§cMap not found!")
        );

        return true;
    }

    @Override
    public List<String> getTabCompletions(CommandSender sender, String[] args) {
        if (args.length == 1) {
            String input = args[0].toLowerCase();
            return plugin.getMapManager().getActiveMaps().keySet().stream()
                    .filter(name -> name.toLowerCase().startsWith(input))
                    .toList();
        }
        return List.of();
    }
}