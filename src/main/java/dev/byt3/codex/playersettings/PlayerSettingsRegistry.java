package dev.byt3.codex.playersettings;

import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import dev.byt3.codex.generated.GeneratedSettingsPageProvider;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.function.Supplier;

public class PlayerSettingsRegistry {
    private static final PlayerSettingsRegistry INSTANCE = new PlayerSettingsRegistry();

    private final Map<String, PlayerSettingsProvider> providers = new Object2ObjectOpenHashMap<>();

    private PlayerSettingsRegistry() {
    }

    public static PlayerSettingsRegistry get() {
        return INSTANCE;
    }

    public void registerProvider(PlayerSettingsProvider provider) {
        if (providers.containsKey(provider.getId())) {
            throw new IllegalArgumentException("A provider with the ID '" + provider.getId() + "' is already registered!");
        }
        providers.put(provider.getId(), provider);
    }

    public <T extends Component<EntityStore>> void putCodecProvider(@Nonnull String id, @Nonnull Supplier<BuilderCodec<T>> codecSupplier, @Nonnull ComponentType<EntityStore, T> componentType) {
        registerCodec(id, codecSupplier.get(), componentType, true);
    }

    public <T extends Component<EntityStore>> void registerCodec(@Nonnull String id, @Nonnull BuilderCodec<T> codec, @Nonnull ComponentType<EntityStore, T> componentType, boolean overwrite) {
        if (providers.containsKey(id) && !overwrite) {
            throw new IllegalArgumentException("A provider with the ID '" + id + "' is already registered!");
        }
        providers.put(id, new GeneratedSettingsPageProvider<>(id, codec, componentType));
    }

    public <T extends Component<EntityStore>> void registerCodec(@Nonnull String id, @Nonnull BuilderCodec<T> codec, @Nonnull ComponentType<EntityStore, T> componentType) {
        registerCodec(id, codec, componentType, false);
    }

    public PlayerSettingsProvider getProvider(String id) {
        return providers.get(id);
    }

    @Nonnull
    public Collection<PlayerSettingsProvider> getProviders() {
        return Collections.unmodifiableCollection(this.providers.values());
    }
}
