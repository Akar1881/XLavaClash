package com.xlavaclash.commands.subcommands;

import com.xlavaclash.XLavaClash;
import com.xlavaclash.commands.SubCommand;
import com.xlavaclash.models.GameMap;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class SpectateCommand extends SubCommand {
    private final XLavaClash plugin;

    public SpectateCommand(XLavaClash plugin) {
        super("spec", "xlavaclash.admin", "/xlc spec <join/quit> [mapname]");
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cThis command can only be used by players!");
            return true;
        }

        if (args.length < 1 || args.length > 2) {
            sender.sendMessage("§cUsage: " + usage);
            return true;
        }

        Player player = (Player) sender;
        String action = args[0].toLowerCase();

        switch (action) {
            case "join":
                if (args.length != 2) {
                    sender.sendMessage("§cUsage: /xlc spec join <mapname>");
                    return true;
                }
                return handleSpectateJoin(player, args[1]);
            case "quit":
                return handleSpectateQuit(player);
            default:
                sender.sendMessage("§cInvalid action! Use: join or quit");
                return true;
        }
    }

    private boolean handleSpectateJoin(Player player, String mapName) {
        return plugin.getMapManager().getMap(mapName)
            .map(map -> {
                Location queueLobby = map.getQueueLobby();
                if (queueLobby == null) {
                    player.sendMessage("§cThis map doesn't have a queue lobby set!");
                    return true;
                }

                // Store previous location and gamemode
                plugin.getSpectateManager().storePlayerState(player);

                // Set to spectator mode and teleport
                player.setGameMode(GameMode.SPECTATOR);
                player.teleport(queueLobby);
                player.sendMessage("§aYou are now spectating map '" + mapName + "'!");
                
                return true;
            })
            .orElseGet(() -> {
                player.sendMessage("§cMap not found!");
                return true;
            });
    }

    private boolean handleSpectateQuit(Player player) {
        if (plugin.getSpectateManager().restorePlayerState(player)) {
            player.sendMessage("§aYou are no longer spectating!");
        } else {
            player.sendMessage("§cYou are not spectating!");
        }
        return true;
    }

    @Override
    public List<String> getTabCompletions(CommandSender sender, String[] args) {
        if (args.length == 1) {
            String input = args[0].toLowerCase();
            return List.of("join", "quit").stream()
                    .filter(s -> s.startsWith(input))
                    .collect(Collectors.toList());
        } else if (args.length == 2 && args[0].equalsIgnoreCase("join")) {
            String input = args[1].toLowerCase();
            return plugin.getMapManager().getAllMaps().keySet().stream()
                    .filter(name -> name.toLowerCase().startsWith(input))
                    .collect(Collectors.toList());
        }
        return List.of();
    }
}