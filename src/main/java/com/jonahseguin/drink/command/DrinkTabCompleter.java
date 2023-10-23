package com.jonahseguin.drink.command;

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

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

        Map.Entry<DrinkCommand, String[]> data = container.getCommand(args);
        if (data != null && data.getKey() != null) {
            String tabCompleting = "";
            int tabCompletingIndex = 0;
            if (data.getValue().length > 0) {
                tabCompleting = data.getValue()[data.getValue().length - 1];
                tabCompletingIndex = data.getValue().length - 1;
            }
            DrinkCommand drinkCommand = data.getKey();
            if (drinkCommand.getConsumingProviders().length > tabCompletingIndex) {
                CompletableFuture<List<String>> future = drinkCommand.getConsumingProviders()[tabCompletingIndex].getSuggestionsAsync(e.getSender(), tabCompleting, List.of(args));
                List<String> s = future.join();
                if (s != null) {
                    List<String> suggestions = new ArrayList<>(s);
                    if (args.length == 0 || args.length == 1) {
                        String tC = "";
                        if (args.length > 0) {
                            tC = args[args.length - 1];
                        }
                        suggestions.addAll(container.getCommandSuggestions(tC));
                    }
                    e.setCompletions(suggestions);
                } else {
                    if (args.length == 0 || args.length == 1) {
                        String tC = "";
                        if (args.length > 0) {
                            tC = args[args.length - 1];
                        }
                        e.setCompletions(container.getCommandSuggestions(tC));
                    }
                }
            } else {
                if (args.length == 0 || args.length == 1) {
                    String tC = "";
                    if (args.length > 0) {
                        tC = args[args.length - 1];
                    }
                    e.setCompletions(container.getCommandSuggestions(tC));
                }
            }
        } else {
            if (args.length == 0 || args.length == 1) {
                String tC = "";
                if (args.length > 0) {
                    tC = args[args.length - 1];
                }
                e.setCompletions(container.getCommandSuggestions(tC));
            }
        }

    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase(container.getName())) {
            Map.Entry<DrinkCommand, String[]> data = container.getCommand(args);
            if (data != null && data.getKey() != null) {
                String tabCompleting = "";
                int tabCompletingIndex = 0;
                if (data.getValue().length > 0) {
                    tabCompleting = data.getValue()[data.getValue().length - 1];
                    tabCompletingIndex = data.getValue().length - 1;
                }
                DrinkCommand drinkCommand = data.getKey();
                if (drinkCommand.getConsumingProviders().length > tabCompletingIndex) {
                    List<String> s = drinkCommand.getConsumingProviders()[tabCompletingIndex].getSuggestions(sender, tabCompleting, List.of(args));
                    if (s != null) {
                        List<String> suggestions = new ArrayList<>(s);
                        if (args.length == 0 || args.length == 1) {
                            String tC = "";
                            if (args.length > 0) {
                                tC = args[args.length - 1];
                            }
                            suggestions.addAll(container.getCommandSuggestions(tC));
                        }
                        return suggestions;
                    } else {
                        if (args.length == 0 || args.length == 1) {
                            String tC = "";
                            if (args.length > 0) {
                                tC = args[args.length - 1];
                            }
                            return container.getCommandSuggestions(tC);
                        }
                    }
                } else {
                    if (args.length == 0 || args.length == 1) {
                        String tC = "";
                        if (args.length > 0) {
                            tC = args[args.length - 1];
                        }
                        return container.getCommandSuggestions(tC);
                    }
                }
            } else {
                if (args.length == 0 || args.length == 1) {
                    String tC = "";
                    if (args.length > 0) {
                        tC = args[args.length - 1];
                    }
                    return container.getCommandSuggestions(tC);
                }
            }
        }
        return Collections.emptyList();
    }
}
