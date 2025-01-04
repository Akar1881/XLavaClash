package com.xlavaclash.commands;

import org.bukkit.command.CommandSender;
import java.util.List;

public abstract class SubCommand {
    protected final String name;
    protected final String permission;
    protected final String usage;

    public SubCommand(String name, String permission, String usage) {
        this.name = name;
        this.permission = permission;
        this.usage = usage;
    }

    public abstract boolean execute(CommandSender sender, String[] args);
    
    public abstract List<String> getTabCompletions(CommandSender sender, String[] args);

    public String getName() {
        return name;
    }

    public String getPermission() {
        return permission;
    }

    public String getUsage() {
        return usage;
    }
}