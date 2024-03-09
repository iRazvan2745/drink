package com.jonahseguin.drink.command;

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent;
import com.google.common.collect.Maps;
import com.jonahseguin.drink.parametric.CommandParameter;
import com.jonahseguin.drink.parametric.DrinkProvider;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class DrinkTabCompleter implements TabCompleter, Listener {

    private final DrinkCommandService commandService;
    private final DrinkCommandContainer container;

    public DrinkTabCompleter(DrinkCommandService commandService, DrinkCommandContainer container) {
        this.commandService = commandService;
        this.container = container;
    }

    @EventHandler
    public void onAsyncTabComplete(AsyncTabCompleteEvent e) {
        if (!e.isCommand()) return;

        if (e.getBuffer().length() < 2) return;

        String buffer = e.getBuffer().substring(1);
        String[] args = buffer.split(" ");

        if (args.length == 0) return;

        String commandName = args[0];
        if (!commandName.equalsIgnoreCase(container.getName()) && container.getAliases().stream().noneMatch(s -> s.equalsIgnoreCase(commandName))) {
            return;
        }


        String[] args2 = new String[args.length - 1];
        System.arraycopy(args, 1, args2, 0, args2.length);
        args = args2;

        if (buffer.endsWith(" ")) {
            args = Arrays.copyOf(args, args.length + 1);
            args[args.length - 1] = "";
        }

        List<String> completions = getCompletions(e.getSender(), args, true);
        if(completions == null || completions.isEmpty()) {
            return;
        }
        e.setCompletions(completions);
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, Command command, @NotNull String label, String[] args) {
        if (!command.getName().equalsIgnoreCase(container.getName()) && container.getAliases().stream().noneMatch(s -> s.equalsIgnoreCase(command.getName()))) {
            return Collections.emptyList();
        }

        return getCompletions(sender, args, false);
    }

    public List<String> getCompletions(CommandSender sender, String[] args, boolean async) {
        Map.Entry<DrinkCommand, String[]> data = container.getCommandWithPermission(args, sender);
        List<String> completions = new ArrayList<>();
        if (data != null && data.getKey() != null) {
            String tabCompleting = "";
            int tabCompletingIndex = 0;
            if (data.getValue().length > 0) {
                tabCompleting = data.getValue()[data.getValue().length - 1];
                tabCompletingIndex = data.getValue().length - 1;
            }
            DrinkCommand drinkCommand = data.getKey();
            if (drinkCommand.getConsumingProviders().length > tabCompletingIndex) {
                DrinkProvider<?> provider = drinkCommand.getConsumingProviders()[tabCompletingIndex];
                CommandParameter commandParameter = drinkCommand.getParameters().getParameters()[tabCompletingIndex + 1];
                Map<CommandParameter, String> parameters = Maps.newLinkedHashMap();
                int index = 0;
                for (CommandParameter parameter : drinkCommand.getParameters().getParameters()) {
                    String name = args.length > index ? args[index] : null;
                    parameters.put(parameter, name);
                    index++;
                }
                List<String> s = async ?
                        provider.getSuggestionsAsync(sender, tabCompleting, parameters, commandParameter.getAllAnnotations()).join() :
                        provider.getSuggestions(sender, tabCompleting, parameters, commandParameter.getAllAnnotations());
                if (s != null) {
                    completions.addAll(s);
                    if (args.length == 0 || args.length == 1) {
                        String tC = "";
                        if (args.length > 0) {
                            tC = args[args.length - 1];
                        }
                        completions.addAll(container.getCommandSuggestions(tC, sender));
                    }
                } else {
                    if (args.length == 0 || args.length == 1) {
                        String tC = "";
                        if (args.length > 0) {
                            tC = args[args.length - 1];
                        }
                        completions.addAll(container.getCommandSuggestions(tC, sender));
                    }
                }
            } else {
                if (args.length == 0 || args.length == 1) {
                    String tC = "";
                    if (args.length > 0) {
                        tC = args[args.length - 1];
                    }
                    completions.addAll(container.getCommandSuggestions(tC, sender));
                }
            }
        } else {
            if (args.length == 0 || args.length == 1) {
                String tC = "";
                if (args.length > 0) {
                    tC = args[args.length - 1];
                }
                completions.addAll(container.getCommandSuggestions(tC, sender));
            }
        }
        return completions;
    }
}
