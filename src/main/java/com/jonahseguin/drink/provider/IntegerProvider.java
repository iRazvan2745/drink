package com.jonahseguin.drink.provider;

import com.jonahseguin.drink.command.CommandArgs;
import com.jonahseguin.drink.exception.CommandExitMessage;
import com.jonahseguin.drink.parametric.DrinkProvider;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class IntegerProvider extends DrinkProvider<Integer> {

    public static final IntegerProvider INSTANCE = new IntegerProvider();

    @Override
    public boolean doesConsumeArgument() {
        return true;
    }

    @Override
    public boolean isAsync() {
        return false;
    }

    @Override
    public Optional<Integer> provide(@Nonnull CommandArgs args, @Nonnull List<? extends Annotation> annotations) throws CommandExitMessage {
        String s = args.next();
        try {
            Integer i = Integer.parseInt(s);
            return Optional.of(i);
        }
        catch (NumberFormatException ex) {
            throw new CommandExitMessage("Required: Integer, Given: '" + s + "'");
        }
    }

    @Override
    public String argumentDescription() {
        return "integer";
    }

    @Override
    public List<String> getSuggestions(@Nonnull String prefix) {
        return Collections.emptyList();
    }
}