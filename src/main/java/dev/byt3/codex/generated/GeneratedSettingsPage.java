package dev.byt3.codex.generated;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.ExtraInfo;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.builder.BuilderField;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.entities.player.pages.InteractiveCustomUIPage;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.ui.builder.EventData;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import dev.byt3.codex.playersettings.PlayerSettingsMainPage;
import org.bson.BsonDocument;
import org.bson.BsonValue;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

import static com.hypixel.hytale.server.core.universe.world.WorldConfig.formatDisplayName;

public class GeneratedSettingsPage<Data extends Component<EntityStore>> extends InteractiveCustomUIPage<Data> {

    private final String displayName;
    private final UICodecWrapper<Data> eventDataCodec;
    private final ComponentType<EntityStore, Data> componentType;

    public GeneratedSettingsPage(@Nonnull PlayerRef playerRef, @Nonnull BuilderCodec<Data> dataCodec, @Nonnull ComponentType<EntityStore, Data> componentType, @Nonnull String displayName) {
        UICodecWrapper<Data> dataCodecWrapper = new UICodecWrapper<>(dataCodec);
        super(playerRef, CustomPageLifetime.CanDismissOrCloseThroughInteraction, dataCodecWrapper);
        this.eventDataCodec = dataCodecWrapper;
        this.displayName = displayName;
        this.componentType = componentType;
    }

    @Override
    public void build(@Nonnull Ref<EntityStore> ref, @Nonnull UICommandBuilder uiCommandBuilder, @Nonnull UIEventBuilder uiEventBuilder, @Nonnull Store<EntityStore> store) {
        uiCommandBuilder.append("Pages/GeneratedSettings.ui");

        // Set the page title to the provider's display name
        uiCommandBuilder.set("#TitleLabel.Text", displayName);

        // Bind the back button to navigate back to the main settings page
        uiEventBuilder.addEventBinding(
                CustomUIEventBindingType.Activating,
                "#BackButton",
                EventData.of("_GoBack", "")
        );

        int index = 0;
        ExtraInfo threadLocalInfo = ExtraInfo.THREAD_LOCAL.get();
        Data currentData = store.getComponent(ref, componentType);
        if (currentData == null) {
            HytaleLogger.getLogger().atWarning().log("No existing component data found for ref %s, using default instance", ref);
            currentData = eventDataCodec.getSupplier().get();
        }
        BsonDocument document = eventDataCodec.encode(currentData, threadLocalInfo);
        HytaleLogger.getLogger().atInfo().log("Encoded component data for ref %s into document: %s", ref, document.toJson());

        Map<String, List<BuilderField<Data, ?>>> entries = eventDataCodec.getEntries();

        // Iterate over the map entries returned by getEntries()
        for (Map.Entry<String, List<BuilderField<Data, ?>>> entry : entries.entrySet()) {
            String key = entry.getKey();
            List<BuilderField<Data, ?>> fields = entry.getValue();

            // Safety check to ensure the field list isn't empty
            if (fields == null || fields.isEmpty()) {
                continue;
            }

            // Grab the primary codec for this field to determine its UI type
            BuilderField<Data, ?> firstField = fields.getFirst();
            if (firstField == null) {
                continue;
            }
            Codec<?> codec = firstField.getCodec().getChildCodec();

            String displayName = formatDisplayName(key);
            if (displayName.startsWith("@")) {
                displayName = displayName.substring(1);
            }

            BsonValue value = document.get(key);

            if (value == null) {
                HytaleLogger.getLogger().atInfo().log("No value found in document for field '%s'", key);
            }

            CodecUIProvider<?> provider = GeneratedSettingsRegistry.get(codec);
            if (provider != null) {
                provider.buildCodec(uiCommandBuilder, uiEventBuilder, index, displayName, key, value);
            }

            index++;
        }
    }

    @Override
    public void handleDataEvent(@Nonnull Ref<EntityStore> ref, @Nonnull Store<EntityStore> store, @Nonnull String data) {
        if (data.contains("\"_GoBack\"")) {
            Player player = store.getComponent(ref, Player.getComponentType());
            if (player == null) return;
            PlayerRef playerRef = store.getComponent(ref, PlayerRef.getComponentType());
            if (playerRef == null) return;
            player.getPageManager().openCustomPage(ref, store, new PlayerSettingsMainPage(playerRef));
            return;
        }
        super.handleDataEvent(ref, store, data);
        sendUpdate();
    }

    @Override
    public void handleDataEvent(@Nonnull Ref<EntityStore> ref, @Nonnull Store<EntityStore> store, @Nonnull Data data) {
        Data currentData = store.getComponent(ref, componentType);
        // use reflection to merge the two
        if (currentData != null) {
            HytaleLogger.getLogger().atInfo().log("Merging new data with existing component data for ref %s", ref);
            eventDataCodec.originalCodec.inherit(currentData, data, ExtraInfo.THREAD_LOCAL.get());
        } else {
            HytaleLogger.getLogger().atInfo().log("No existing component data for ref %s, using new data as is", ref);
            store.addComponent(ref, componentType, data);
        }
    }

    // Make sure that all the fields in the codec start with '@' so that they are included in the UI mapping

    private static class UICodecWrapper<T> extends BuilderCodec<T> {
        private final BuilderCodec<T> originalCodec;

        public UICodecWrapper(BuilderCodec<T> originalCodec) {
            super(new UICodecBuilder<>(originalCodec));
            this.originalCodec = originalCodec;
        }
    }

    private static class UICodecBuilder<T, S extends BuilderCodec.BuilderBase<T, S>> extends BuilderCodec.BuilderBase<T, S> {
        public UICodecBuilder(BuilderCodec<T> originalCodec) {
            super(originalCodec.getInnerClass(), originalCodec.getSupplier(), originalCodec.getParent());
            this.documentation(originalCodec.getDocumentation())
                    .codecVersion(originalCodec.getCodecVersion());
            originalCodec.getEntries().forEach((key, fields) -> {
                String uiKey = "@" + key; // Prepend '@' to the key for UI mapping
                entries.put(uiKey, fields);
            });
        }
    }
}
