package com.xlavaclash.commands;

import com.xlavaclash.XLavaClash;
import com.xlavaclash.commands.subcommands.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.*;

public class XLavaClashCommand implements CommandExecutor, TabCompleter {
    private final XLavaClash plugin;
    private final Map<String, SubCommand> subCommands;

    public XLavaClashCommand(XLavaClash plugin) {
        this.plugin = plugin;
        this.subCommands = new HashMap<>();
        registerSubCommands();
    }

    private void registerSubCommands() {
        addSubCommand(new CreateCommand(plugin));
        addSubCommand(new DeleteCommand(plugin));
        addSubCommand(new SetQueueCommand(plugin));
        addSubCommand(new SetButtonCommand(plugin));
        addSubCommand(new SetTeamCommand(plugin));
        addSubCommand(new SetSizeCommand(plugin));
        addSubCommand(new SetLobbyCommand(plugin));
        addSubCommand(new LobbyCommand(plugin));
        addSubCommand(new MapCommand(plugin));
        addSubCommand(new JoinCommand(plugin));
        addSubCommand(new LeaveCommand(plugin));
        addSubCommand(new HelpCommand(plugin));
        addSubCommand(new RankCommand(plugin));
        addSubCommand(new ReloadCommand(plugin));
        addSubCommand(new SpectateCommand(plugin));
    }

    private void addSubCommand(SubCommand cmd) {
        subCommands.put(cmd.getName().toLowerCase(), cmd);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            subCommands.get("help").execute(sender, args);
            return true;
        }

        String subCommandName = args[0].toLowerCase();
        SubCommand subCommand = subCommands.get(subCommandName);

        if (subCommand == null) {
            sender.sendMessage("§cUnknown subcommand. Use /xlc help for help.");
            return true;
        }

        if (!sender.hasPermission(subCommand.getPermission())) {
            sender.sendMessage("§cYou don't have permission to use this command.");
            return true;
        }

        String[] subArgs = Arrays.copyOfRange(args, 1, args.length);
        return subCommand.execute(sender, subArgs);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            String input = args[0].toLowerCase();
            List<String> completions = new ArrayList<>();
            
            for (SubCommand subCommand : subCommands.values()) {
                if (sender.hasPermission(subCommand.getPermission()) && 
                    subCommand.getName().toLowerCase().startsWith(input)) {
                    completions.add(subCommand.getName());
                }
            }
            
            return completions;
        }

        if (args.length >= 2) {
            SubCommand subCommand = subCommands.get(args[0].toLowerCase());
            if (subCommand != null && sender.hasPermission(subCommand.getPermission())) {
                return subCommand.getTabCompletions(sender, Arrays.copyOfRange(args, 1, args.length));
            }
        }

        return Collections.emptyList();
    }
}