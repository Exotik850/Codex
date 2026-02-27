package dev.byt3.codex.generated;

import com.hypixel.hytale.codec.Codec;
import dev.byt3.codex.generated.providers.BooleanCodecUIProvider;
import dev.byt3.codex.generated.providers.NumberCodecUIProvider;
import dev.byt3.codex.generated.providers.StringCodecUIProvider;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

/// Maps {@link Codec} instances to the {@link CodecUIProvider} that renders them. Pre-loaded with providers for String, Boolean, and numeric types.
public class GeneratedSettingsRegistry {

    private static final Map<Codec<?>, TypedEntry<?>> CODECS = new Object2ObjectOpenHashMap<>();

    static {
        registerDefaults();
    }

    private record TypedEntry<T>(Codec<T> codec, CodecUIProvider<T> provider) {
    }

    /// Registers (or overwrites) a UI provider for the given codec.
    public static <T> void put(@Nonnull Codec<T> codec, @Nonnull CodecUIProvider<T> provider) {
        CODECS.put(codec, new TypedEntry<>(codec, provider));
    }

    /// Registers a UI provider for the given codec. Throws if the codec is already registered — use {@link #put} to overwrite.
    public static <T> void register(@Nonnull Codec<T> codec, @Nonnull CodecUIProvider<T> provider) {
        if (CODECS.containsKey(codec)) {
            throw new IllegalArgumentException("Codec already registered: " + codec);
        }
        CODECS.put(codec, new TypedEntry<>(codec, provider));
    }

    /// Returns the provider for {@code codec}, or {@code null} if none is registered.
    @Nullable
    public static <T> CodecUIProvider<T> get(@Nonnull Codec<T> codec) {
        TypedEntry<?> entry = CODECS.get(codec);
        if (entry == null) {
            return null;
        }
        if (entry.codec() != codec) {
            return null;
        }
        // Safe: the entry was created by register(), which enforces that
        // the codec and provider share the same type parameter T.
        // The identity check above confirms we retrieved the exact codec instance.
        @SuppressWarnings("unchecked")
        CodecUIProvider<T> provider = (CodecUIProvider<T>) entry.provider();
        return provider;
    }

    private static void registerDefaults() {
        register(Codec.STRING, new StringCodecUIProvider());
        register(Codec.BOOLEAN, new BooleanCodecUIProvider());

        NumberCodecUIProvider numberProvider = new NumberCodecUIProvider();
        registerWidened(Codec.FLOAT, numberProvider);
        registerWidened(Codec.INTEGER, numberProvider);
        registerWidened(Codec.DOUBLE, numberProvider);
    }

    /**
     * Registers a provider whose type parameter is a supertype of the codec's type.
     * This is safe because {@link CodecUIProvider#buildCodec} operates on {@code BsonValue},
     * not on {@code T} directly.
     */
    @SuppressWarnings("unchecked")
    private static <T> void registerWidened(@Nonnull Codec<? extends T> codec, @Nonnull CodecUIProvider<T> provider) {
        CODECS.put(codec, new TypedEntry<>((Codec<T>) codec, provider));
    }
}
