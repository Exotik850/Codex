package dev.byt3.codex.hubsettings;

import com.hypixel.hytale.server.core.entity.entities.player.pages.InteractiveCustomUIPage;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import dev.byt3.codex.playersettings.PlayerSettingsProvider;
import dev.byt3.codex.playersettings.PlayerSettingsRegistry;

import javax.annotation.Nonnull;

public class HubSettingsProvider implements PlayerSettingsProvider {
    @Nonnull
    @Override
    public String getId() {
        return "codex_hub_settings";
    }

    @Nonnull
    @Override
    public String getDisplayName() {
        return "Hub Preferences";
    }

    @Nonnull
    @Override
    public InteractiveCustomUIPage<?> createSettingsPage(@Nonnull PlayerRef playerRef) {
        return new HubSettingsPage(playerRef);
    }

    static {
        PlayerSettingsRegistry.get().registerProvider(new HubSettingsProvider());
    }
}
