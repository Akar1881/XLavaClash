package com.xlavaclash.commands.subcommands;

import com.xlavaclash.XLavaClash;
import com.xlavaclash.commands.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class SetTeamCommand extends SubCommand {
    private final XLavaClash plugin;

    public SetTeamCommand(XLavaClash plugin) {
        super("setteam", "xlavaclash.admin", "/xlc setteam <mapname> <red/blue>");
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cThis command can only be used by players!");
            return true;
        }

        if (args.length != 2) {
            sender.sendMessage("§cUsage: " + usage);
            return true;
        }

        Player player = (Player) sender;
        String mapName = args[0];
        String team = args[1].toLowerCase();

        if (!team.equals("red") && !team.equals("blue")) {
            sender.sendMessage("§cTeam must be either 'red' or 'blue'!");
            return true;
        }

        plugin.getMapManager().getMap(mapName).ifPresentOrElse(
            map -> {
                if (team.equals("red")) {
                    map.setRedSpawn(player.getLocation());
                } else {
                    map.setBlueSpawn(player.getLocation());
                }
                sender.sendMessage("§a" + team.toUpperCase() + " team spawn set for map '" + mapName + "'!");
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
                    .collect(Collectors.toList());
        } else if (args.length == 2) {
            String input = args[1].toLowerCase();
            return List.of("red", "blue").stream()
                    .filter(team -> team.startsWith(input))
                    .collect(Collectors.toList());
        }
        return List.of();
    }
}