package com.jonahseguin.drink.command;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;

@Getter
@Setter
public class DrinkAuthorizer {

    private Component noPermissionMessage = Component.text("You do not have permission to perform /{command}", TextColor.color(0xFF0000));

    public boolean isAuthorized(@Nonnull CommandSender sender, @Nonnull DrinkCommand command, @Nonnull String label) {
        if (command.getPermission() == null || !command.getPermission().isEmpty()) {
            return true;
        }
        if (sender.hasPermission(command.getPermission())) {
            return true;
        }
        final String message = command.getPermissionMessage();
        Component component = message != null && !message.isEmpty() ? Component.text(message) : noPermissionMessage;
        component = component.replaceText(builder -> builder.matchLiteral("{command}").replacement(label));
        sender.sendMessage(component);
        return false;
    }

}
