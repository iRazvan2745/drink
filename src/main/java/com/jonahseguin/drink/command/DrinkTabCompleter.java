package com.jonahseguin.drink.command;

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent;
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
        if (!commandName.equalsIgnoreCase(container.getName())) return;

        String[] args2 = new String[args.length - 1];
        System.arraycopy(args, 1, args2, 0, args2.length);
        args = args2;

        if (buffer.endsWith(" ")) {
            args = Arrays.copyOf(args, args.length + 1);
            args[args.length - 1] = "";
        }

        Map.Entry<DrinkCommand, String[]> data = container.getCommandWithPermission(args, e.getSender());
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
                TreeMap<CommandParameter, String> parameters = new TreeMap<>();
                int index = 0;
                for (CommandParameter parameter : drinkCommand.getParameters().getParameters()) {
                    String name = args.length > index ? args[index] : null;
                    parameters.put(parameter, name);
                    index++;
                }
                CommandParameter parameter = drinkCommand.getParameters().getParameters()[tabCompletingIndex];
                CompletableFuture<List<String>> future = provider.getSuggestionsAsync(e.getSender(), tabCompleting, parameters, parameter.getAllAnnotations());
                List<String> s = future.join();
                if (s != null) {
                    List<String> suggestions = new ArrayList<>(s);
                    if (args.length == 0 || args.length == 1) {
                        String tC = "";
                        if (args.length > 0) {
                            tC = args[args.length - 1];
                        }
                        suggestions.addAll(container.getCommandSuggestions(tC, e.getSender()));
                    }
                    e.setCompletions(suggestions);
                } else {
                    if (args.length == 0 || args.length == 1) {
                        String tC = "";
                        if (args.length > 0) {
                            tC = args[args.length - 1];
                        }
                        e.setCompletions(container.getCommandSuggestions(tC, e.getSender()));
                    }
                }
            } else {
                if (args.length == 0 || args.length == 1) {
                    String tC = "";
                    if (args.length > 0) {
                        tC = args[args.length - 1];
                    }
                    e.setCompletions(container.getCommandSuggestions(tC, e.getSender()));
                }
            }
        } else {
            if (args.length == 0 || args.length == 1) {
                String tC = "";
                if (args.length > 0) {
                    tC = args[args.length - 1];
                }
                e.setCompletions(container.getCommandSuggestions(tC, e.getSender()));
            }
        }

    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, Command command, @NotNull String label, String[] args) {
        if (command.getName().equalsIgnoreCase(container.getName())) {
            Map.Entry<DrinkCommand, String[]> data = container.getCommandWithPermission(args, sender);
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
                    CommandParameter commandParameter = drinkCommand.getParameters().getParameters()[tabCompletingIndex];
                    TreeMap<CommandParameter, String> parameters = new TreeMap<>();
                    int index = 0;
                    for (CommandParameter parameter : drinkCommand.getParameters().getParameters()) {
                        String name = args.length > index ? args[index] : null;
                        parameters.put(parameter, name);
                        index++;
                    }
                    List<String> s = provider.getSuggestions(sender, tabCompleting, parameters, commandParameter.getAllAnnotations());
                    if (s != null) {
                        List<String> suggestions = new ArrayList<>(s);
                        if (args.length == 0 || args.length == 1) {
                            String tC = "";
                            if (args.length > 0) {
                                tC = args[args.length - 1];
                            }
                            suggestions.addAll(container.getCommandSuggestions(tC, sender));
                        }
                        return suggestions;
                    } else {
                        if (args.length == 0 || args.length == 1) {
                            String tC = "";
                            if (args.length > 0) {
                                tC = args[args.length - 1];
                            }
                            return container.getCommandSuggestions(tC, sender);
                        }
                    }
                } else {
                    if (args.length == 0 || args.length == 1) {
                        String tC = "";
                        if (args.length > 0) {
                            tC = args[args.length - 1];
                        }
                        return container.getCommandSuggestions(tC, sender);
                    }
                }
            } else {
                if (args.length == 0 || args.length == 1) {
                    String tC = "";
                    if (args.length > 0) {
                        tC = args[args.length - 1];
                    }
                    return container.getCommandSuggestions(tC, sender);
                }
            }
        }
        return Collections.emptyList();
    }
}
