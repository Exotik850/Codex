package dev.byt3.codex.generated;

import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import org.bson.BsonValue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/// Renders a single codec field into the generated settings UI. Register implementations via {@link GeneratedSettingsRegistry}.
@FunctionalInterface
public interface CodecUIProvider<T> {
    /// Appends a UI control for one field to the settings page.
    /// @param index       position in the settings list (used for CSS-style selectors)
    /// @param displayName human-readable label derived from the codec key
    /// @param fieldKey    codec key (prefixed with '@') sent back in events
    /// @param value       current persisted value, or null if unset
    void buildCodec(@Nonnull UICommandBuilder uiCommandBuilder, @Nonnull UIEventBuilder uiEventBuilder, int index, @Nonnull String displayName, @Nonnull String fieldKey, @Nullable BsonValue value);
}
