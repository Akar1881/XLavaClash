package com.xlavaclash.commands.subcommands;

import com.xlavaclash.XLavaClash;
import com.xlavaclash.commands.SubCommand;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.stream.Collectors;

public class MapCommand extends SubCommand {
    private final XLavaClash plugin;

    public MapCommand(XLavaClash plugin) {
        super("map", "xlavaclash.admin", "/xlc map <list|info|active|deactive> [mapname]");
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("§cUsage: " + usage);
            return true;
        }

        String subCommand = args[0].toLowerCase();
        switch (subCommand) {
            case "list":
                listMaps(sender);
                break;
            case "info":
                if (args.length < 2) {
                    sender.sendMessage("§cUsage: /xlc map info <mapname>");
                    return true;
                }
                showMapInfo(sender, args[1]);
                break;
            case "active":
                if (args.length < 2) {
                    sender.sendMessage("§cUsage: /xlc map active <mapname>");
                    return true;
                }
                setMapActive(sender, args[1], true);
                break;
            case "deactive":
                if (args.length < 2) {
                    sender.sendMessage("§cUsage: /xlc map deactive <mapname>");
                    return true;
                }
                setMapActive(sender, args[1], false);
                break;
            default:
                sender.sendMessage("§cUnknown subcommand. Use: list, info, active, deactive");
        }

        return true;
    }

    private void listMaps(CommandSender sender) {
        sender.sendMessage("§6=== Maps ===");
        sender.sendMessage("§eActive Maps:");
        plugin.getMapManager().getActiveMaps().forEach((name, map) -> 
            sender.sendMessage("§7- §a" + name + " §7(" + map.getTeamSize() + ")"));
            
        sender.sendMessage("\n§eInactive Maps:");
        plugin.getMapManager().getAllMaps().forEach((name, map) -> {
            if (!map.isActive()) {
                sender.sendMessage("§7- §c" + name + " §7(" + map.getTeamSize() + ")");
            }
        });
    }

    private void showMapInfo(CommandSender sender, String mapName) {
        plugin.getMapManager().getMap(mapName).ifPresentOrElse(
            map -> {
                sender.sendMessage("§6=== Map Information: §e" + map.getName() + " §6===");
                sender.sendMessage("§7World: §f" + map.getWorldName());
                sender.sendMessage("§7Team Size: §f" + map.getTeamSize());
                sender.sendMessage("§7Status: " + (map.isActive() ? "§aActive" : "§cInactive"));
                sender.sendMessage("§7Configuration:");
                sender.sendMessage("  §7Queue Lobby: " + (map.getQueueLobby() != null ? "§aSet" : "§cNot Set"));
                sender.sendMessage("  §7Button Location: " + (map.getButtonLocation() != null ? "§aSet" : "§cNot Set"));
                sender.sendMessage("  §7Red Spawn: " + (map.getRedSpawn() != null ? "§aSet" : "§cNot Set"));
                sender.sendMessage("  §7Blue Spawn: " + (map.getBlueSpawn() != null ? "§aSet" : "§cNot Set"));
                sender.sendMessage("§7Ready for Play: " + (map.isConfigured() ? "§aYes" : "§cNo"));
            },
            () -> sender.sendMessage("§cMap not found!")
        );
    }

    private void setMapActive(CommandSender sender, String mapName, boolean active) {
        plugin.getMapManager().getMap(mapName).ifPresentOrElse(
            map -> {
                if (!map.isConfigured()) {
                    sender.sendMessage("§cCannot " + (active ? "activate" : "deactivate") + 
                        " map: Map is not fully configured!");
                    return;
                }
                
                map.setActive(active);
                plugin.getMapManager().saveMap(map);
                sender.sendMessage("§aMap '" + mapName + "' " + 
                    (active ? "activated" : "deactivated") + " successfully!");
            },
            () -> sender.sendMessage("§cMap not found!")
        );
    }

    @Override
    public List<String> getTabCompletions(CommandSender sender, String[] args) {
        if (args.length == 1) {
            String input = args[0].toLowerCase();
            return List.of("list", "info", "active", "deactive").stream()
                    .filter(cmd -> cmd.startsWith(input))
                    .collect(Collectors.toList());
        } else if (args.length == 2 && !args[0].equalsIgnoreCase("list")) {
            String input = args[1].toLowerCase();
            return plugin.getMapManager().getAllMaps().keySet().stream()
                    .filter(name -> name.toLowerCase().startsWith(input))
                    .collect(Collectors.toList());
        }
        return List.of();
    }
}