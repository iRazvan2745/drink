package com.jonahseguin.drink.util;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;

public class ComponentHelper {

    @Getter
    private final static LegacyComponentSerializer STUPID = LegacyComponentSerializer.builder()
            .character('&')
            .hexCharacter('#')
            .useUnusualXRepeatedCharacterHexFormat()
            .hexColors()
            .build();

    @NotNull
    public static Component format(@NotNull String message) {
        final Component component = STUPID.deserialize(message);
        final String deserialized = MiniMessage.miniMessage().serialize(component).replace("\\<", "<");
        return MiniMessage.miniMessage().deserialize(deserialized);
    }

}
