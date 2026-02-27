package dev.byt3.codex.generated;

import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.entity.entities.player.pages.InteractiveCustomUIPage;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import dev.byt3.codex.playersettings.PlayerSettingsProvider;

import javax.annotation.Nonnull;

import static com.hypixel.hytale.server.core.universe.world.WorldConfig.formatDisplayName;

/// Default {@link PlayerSettingsProvider} created by {@code PlayerSettingsRegistry.registerCodec}. Delegates page creation to {@link GeneratedSettingsPage}.
public class GeneratedSettingsPageProvider<Data extends Component<EntityStore>> implements PlayerSettingsProvider {

    @Nonnull
    private final String id;
    @Nonnull
    BuilderCodec<Data> dataCodec;
    @Nonnull
    ComponentType<EntityStore, Data> componentType;

    public GeneratedSettingsPageProvider(@Nonnull String id, @Nonnull BuilderCodec<Data> dataCodec, @Nonnull ComponentType<EntityStore, Data> componentType) {
        this.id = id;
        this.dataCodec = dataCodec;
        this.componentType = componentType;
    }

    @Nonnull
    @Override
    public String getId() {
        return id;
    }

    @Nonnull
    @Override
    public String getDisplayName() {
        return formatDisplayName(id);
    }

    @Nonnull
    @Override
    public InteractiveCustomUIPage<?> createSettingsPage(@Nonnull PlayerRef playerRef) {
        return new GeneratedSettingsPage<>(playerRef, dataCodec, componentType, getDisplayName());
    }
}
