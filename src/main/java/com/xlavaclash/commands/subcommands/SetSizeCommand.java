package com.xlavaclash.commands.subcommands;

import com.xlavaclash.XLavaClash;
import com.xlavaclash.commands.SubCommand;
import com.xlavaclash.models.TeamSize;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SetSizeCommand extends SubCommand {
    private final XLavaClash plugin;

    public SetSizeCommand(XLavaClash plugin) {
        super("setsize", "xlavaclash.admin", "/xlc setsize <mapname> <solo/duo/trio/squad>");
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length != 2) {
            sender.sendMessage("§cUsage: " + usage);
            return true;
        }

        String mapName = args[0];
        String sizeStr = args[1].toUpperCase();

        try {
            TeamSize size = TeamSize.valueOf(sizeStr);
            plugin.getMapManager().getMap(mapName).ifPresentOrElse(
                map -> {
                    map.setTeamSize(size);
                    sender.sendMessage("§aTeam size set to " + size.name() + " for map '" + mapName + "'!");
                },
                () -> sender.sendMessage("§cMap not found!")
            );
        } catch (IllegalArgumentException e) {
            sender.sendMessage("§cInvalid team size! Use: solo, duo, trio, or squad");
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
        } else if (args.length == 2) {
            String input = args[1].toLowerCase();
            return Arrays.stream(TeamSize.values())
                    .map(size -> size.name().toLowerCase())
                    .filter(size -> size.startsWith(input))
                    .collect(Collectors.toList());
        }
        return List.of();
    }
}