package com.jonahseguin.drink.provider;

import com.jonahseguin.drink.command.CommandArgs;
import com.jonahseguin.drink.exception.CommandExitMessage;
import com.jonahseguin.drink.parametric.DrinkProvider;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Optional;

public class BooleanProvider extends DrinkProvider<Boolean> {

    @Override
    public boolean doesConsumeArgument() {
        return false;
    }

    @Override
    public boolean isAsync() {
        return false;
    }

    @Override
    public Optional<Boolean> provide(@Nonnull CommandArgs args, @Nonnull List<? extends Annotation> annotations) throws CommandExitMessage {
        String s = args.next();
        try {
            Boolean i = Boolean.parseBoolean(s);
            return Optional.of(i);
        }
        catch (NumberFormatException ex) {
            throw new CommandExitMessage("Required: Boolean (true/false), Given: '" + s + "'");
        }
    }

    @Override
    public String argumentDescription() {
        return null;
    }

    @Override
    public List<String> getSuggestions(@Nonnull String prefix) {
        return null;
    }
}
