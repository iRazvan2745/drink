package com.jonahseguin.drink.exception;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class CommandExitMessage extends Exception {

    public CommandExitMessage(String message) {
        super(message);
    }

    public void print(CommandSender sender) {
        if (getMessage() == null || getMessage().isEmpty()) return;
        sender.sendMessage(ChatColor.RED + getMessage());
    }
}
