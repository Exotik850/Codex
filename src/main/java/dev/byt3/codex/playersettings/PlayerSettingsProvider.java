package dev.byt3.codex.playersettings;

import com.hypixel.hytale.server.core.entity.entities.player.pages.InteractiveCustomUIPage;
import com.hypixel.hytale.server.core.universe.PlayerRef;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface PlayerSettingsProvider {
    /**
    * Unique ID for this settings provider.
    **/
    @Nonnull
    String getId();

    /**
     * Display name of the category shown in the UI. Should be localized.
     */
    @Nonnull
    String getDisplayName();

    /**
     * Optional path to an icon to show in the UI. Should be a 16x16 image. Can be null to use a default icon.
     */
    @Nullable
    default String getIconPath() {
        return null;
    }

    /**
     * Creates and returns the CustomUIPage to show when the player clicks on this category.
     * @param playerRef
     * The player for which the settings page is being created.
     */
    @Nonnull
    InteractiveCustomUIPage<?> createSettingsPage(@Nonnull PlayerRef playerRef);
}
