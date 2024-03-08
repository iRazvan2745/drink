package com.jonahseguin.drink.provider.spigot;

import com.jonahseguin.drink.argument.CommandArg;
import com.jonahseguin.drink.exception.CommandExitMessage;
import com.jonahseguin.drink.parametric.DrinkProvider;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.util.List;

public class WorldProvider extends DrinkProvider<World> {

    @Override
    public boolean doesConsumeArgument() {
        return true;
    }

    @Override
    public boolean isAsync() {
        return false;
    }

    @Nullable
    @Override
    public World provide(@NotNull CommandArg arg, @NotNull List<? extends Annotation> annotations) throws CommandExitMessage {
        String worldName = arg.get();
        World world = arg.getSender().getServer().getWorld(worldName);
        if (world == null) {
            throw new CommandExitMessage("World '" + worldName + "' not found");
        }
        return world;
    }

    @Override
    public String argumentDescription() {
        return "world";
    }

    @Override
    public List<String> getSuggestions(@NotNull String prefix) {
        final String lowerPrefix = prefix.toLowerCase();
        return Bukkit.getWorlds().stream().map(World::getName).filter(name -> name.toLowerCase().startsWith(lowerPrefix)).toList();
    }
}
