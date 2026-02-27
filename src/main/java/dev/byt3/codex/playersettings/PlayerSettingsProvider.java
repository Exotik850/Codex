package dev.byt3.codex.playersettings;

import com.hypixel.hytale.server.core.entity.entities.player.pages.InteractiveCustomUIPage;
import com.hypixel.hytale.server.core.universe.PlayerRef;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/// Implement this interface to add a custom settings category to the Codex menu. For auto-generated pages from a codec, use {@link PlayerSettingsRegistry#registerCodec} instead.
public interface PlayerSettingsProvider {
    /// Unique ID for this settings provider. Used as the registry key.
    @Nonnull
    String getId();

    /// Display name shown in the category list. Derived from the ID by default.
    @Nonnull
    String getDisplayName();

    /// Optional path to a 100×100 icon shown next to the category name. Return null for the default icon.
    @Nullable
    default String getIconPath() {
        return null;
    }

    ///  Optional description shown below the category name in smaller text. Return null for no description.
    @Nullable
    default String getDescription() {
        return null;
    }

    /// Creates the UI page to display when the player selects this category.
    @Nonnull
    InteractiveCustomUIPage<?> createSettingsPage(@Nonnull PlayerRef playerRef);
}
