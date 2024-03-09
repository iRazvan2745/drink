package com.jonahseguin.drink.exception;

import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CommandExitMessage extends Exception {

    private Component component;

    public CommandExitMessage(@Nullable String message) {
        super(message);
    }

    public CommandExitMessage(@Nonnull Component component) {
        super("");
        this.component = component;
    }

    public void print(CommandSender sender) {
        if (getMessage() == null || getMessage().isEmpty()) {
            if (component != null) {
                sender.sendMessage(component);
            }
            return;
        }
        sender.sendMessage(ChatColor.RED + getMessage());
    }
}
