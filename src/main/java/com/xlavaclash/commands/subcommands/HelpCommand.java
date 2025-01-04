package com.xlavaclash.commands.subcommands;

import com.xlavaclash.XLavaClash;
import com.xlavaclash.commands.SubCommand;
import org.bukkit.command.CommandSender;

import java.util.List;

public class HelpCommand extends SubCommand {
    private final XLavaClash plugin;

    public HelpCommand(XLavaClash plugin) {
        super("help", "xlavaclash.play", "/xlc help");
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        sender.sendMessage("§6=== XLavaClash Help ===");
        
        if (sender.hasPermission("xlavaclash.admin")) {
            sender.sendMessage("§eAdmin Commands:");
            sender.sendMessage("§7/xlc create <mapname> <worldname> §f- Create a new map");
            sender.sendMessage("§7/xlc delete <mapname> §f- Delete a map");
            sender.sendMessage("§7/xlc setqueue <mapname> §f- Set queue lobby location");
            sender.sendMessage("§7/xlc setbutton <mapname> §f- Set button location");
            sender.sendMessage("§7/xlc setteam <mapname> <red/blue> §f- Set team spawn location");
            sender.sendMessage("§7/xlc setsize <mapname> <solo/duo/trio/squad> §f- Set team size");
            sender.sendMessage("§7/xlc map list §f- List all maps");
            sender.sendMessage("§7/xlc map info <mapname> §f- Show map info");
            sender.sendMessage("§7/xlc map active <mapname> §f- Activate a map");
            sender.sendMessage("§7/xlc map deactive <mapname> §f- Deactivate a map");
        }
        
        sender.sendMessage("\n§ePlayer Commands:");
        sender.sendMessage("§7/xlc join §f- Join a game");
        sender.sendMessage("§7/xlc help §f- Show this help message");
        
        return true;
    }

    @Override
    public List<String> getTabCompletions(CommandSender sender, String[] args) {
        return List.of();
    }
}