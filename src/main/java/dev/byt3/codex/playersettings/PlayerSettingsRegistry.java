package dev.byt3.codex.playersettings;

import com.hypixel.hytale.codec.builder.BuilderCodec;
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

    public <Data> void registerCodec(@Nonnull String id, @Nonnull BuilderCodec<Data> codec) {
        if (providers.containsKey(id)) {
            throw new IllegalArgumentException("A provider with the ID '" + id + "' is already registered!");
        }
        providers.put(id, new GeneratedSettingsPageProvider<>(id, codec));
    }

    public PlayerSettingsProvider getProvider(String id) {
        return providers.get(id);
    }

    @Nonnull
    public Collection<PlayerSettingsProvider> getProviders() {
        return Collections.unmodifiableCollection(this.providers.values());
    }
}
