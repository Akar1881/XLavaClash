package com.xlavaclash.commands.subcommands;

import com.xlavaclash.XLavaClash;
import com.xlavaclash.commands.SubCommand;
import com.xlavaclash.models.Team;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class JoinCommand extends SubCommand {
    private final XLavaClash plugin;

    public JoinCommand(XLavaClash plugin) {
        super("join", "xlavaclash.play", "/xlc join [mapname]");
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cThis command can only be used by players!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            // Open map selection GUI if no map specified
            plugin.getGuiManager().openMapSelection(player);
            return true;
        }

        String mapName = args[0];
        return plugin.getMapManager().getMap(mapName)
            .map(map -> {
                if (!map.isActive()) {
                    player.sendMessage("§cThis map is not currently active!");
                    return true;
                }
                
                if (!map.isConfigured()) {
                    player.sendMessage("§cThis map is not fully configured!");
                    return true;
                }

                // Join with random team
                if (plugin.getQueueManager().addPlayer(player, mapName, null)) {
                    player.sendMessage("§aYou joined the queue for map '" + mapName + "'!");
                } else {
                    player.sendMessage("§cCouldn't join the queue. You might already be in a queue!");
                }
                return true;
            })
            .orElseGet(() -> {
                player.sendMessage("§cMap not found!");
                return true;
            });
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