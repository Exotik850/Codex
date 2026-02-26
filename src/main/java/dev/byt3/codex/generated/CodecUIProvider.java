package dev.byt3.codex.generated;

import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import org.bson.BsonValue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@FunctionalInterface
public interface CodecUIProvider<T> {
    void buildCodec(@Nonnull UICommandBuilder uiCommandBuilder, @Nonnull UIEventBuilder uiEventBuilder, int index, @Nonnull String displayName, @Nonnull String fieldKey, @Nullable BsonValue value);
}
