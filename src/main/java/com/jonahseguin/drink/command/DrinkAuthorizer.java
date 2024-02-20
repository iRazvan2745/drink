package com.jonahseguin.drink.command;

import com.jonahseguin.drink.util.ComponentHelper;
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

    private Component noPermissionMessage = ComponentHelper.format("<red>You do not have permission to perform /{command}</red>");

    public boolean isAuthorized(@Nonnull CommandSender sender, @Nonnull DrinkCommand command, @Nonnull String label) {
        if (command.getPermission() == null || command.getPermission().isEmpty()) {
            return true;
        }
        if (sender.hasPermission(command.getPermission())) {
            return true;
        }
        final String message = command.getPermissionMessage();
        Component component = message != null && !message.isEmpty() ? ComponentHelper.format(message) : noPermissionMessage;
        component = component.replaceText(builder -> builder.matchLiteral("{command}").replacement(label));
        sender.sendMessage(component);
        return false;
    }

}
