package com.xlavaclash.commands.subcommands;

import com.xlavaclash.XLavaClash;
import com.xlavaclash.commands.SubCommand;
import com.xlavaclash.utils.ButtonWand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class SetButtonCommand extends SubCommand {
    private final XLavaClash plugin;

    public SetButtonCommand(XLavaClash plugin) {
        super("setbutton", "xlavaclash.admin", "/xlc setbutton <mapname>");
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
        
        if (!plugin.getMapManager().getMap(mapName).isPresent()) {
            sender.sendMessage("§cMap not found!");
            return true;
        }

        player.getInventory().addItem(ButtonWand.create());
        sender.sendMessage("§aYou received a Button Wand! Break a stone button to set it.");
        return true;
    }

    @Override
    public List<String> getTabCompletions(CommandSender sender, String[] args) {
        if (args.length == 1) {
            String input = args[0].toLowerCase();
            return plugin.getMapManager().getAllMaps().keySet().stream()
                    .filter(name -> name.toLowerCase().startsWith(input))
                    .collect(Collectors.toList());
        }
        return List.of();
    }
}