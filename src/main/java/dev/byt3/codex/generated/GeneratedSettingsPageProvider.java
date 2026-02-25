package dev.byt3.codex.generated;

import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.server.core.entity.entities.player.pages.InteractiveCustomUIPage;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import dev.byt3.codex.playersettings.PlayerSettingsProvider;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

import static com.hypixel.hytale.server.core.universe.world.WorldConfig.formatDisplayName;

public class GeneratedSettingsPageProvider<Data> implements PlayerSettingsProvider {

    @Nonnull
    private final String id;
    @Nonnull
    BuilderCodec<Data> dataCodec;

    public GeneratedSettingsPageProvider(@Nonnull String id, @Nonnull BuilderCodec<Data> dataCodec) {
        this.id = id;
        this.dataCodec = dataCodec;
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
        return new GeneratedSettingsPage<>(playerRef, dataCodec, getDisplayName());
    }
}
