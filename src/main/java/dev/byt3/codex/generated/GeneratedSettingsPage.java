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
import com.hypixel.hytale.server.core.ui.Anchor;
import com.hypixel.hytale.server.core.ui.Value;
import com.hypixel.hytale.server.core.ui.builder.EventData;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import dev.byt3.codex.hubsettings.HubConfigData;
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
        uiCommandBuilder.set("#TitleLabel.Text", displayName);

        HubConfigData hubConfig = store.getComponent(ref, HubConfigData.getComponentType());
        int windowWidth = hubConfig != null ? hubConfig.getWindowWidth() : HubConfigData.DEFAULT_WINDOW_WIDTH;
        int windowHeight = hubConfig != null ? hubConfig.getWindowHeight() : HubConfigData.DEFAULT_WINDOW_HEIGHT;
        boolean showBack = hubConfig == null || hubConfig.isShowBackButton();
        boolean compact = hubConfig != null && hubConfig.isCompactMode();

        Anchor anchor = new Anchor();
        anchor.setWidth(Value.of(windowWidth));
        anchor.setHeight(Value.of(windowHeight));
        uiCommandBuilder.setObject("#MainContainer.Anchor", anchor);

        if (showBack) {
            uiEventBuilder.addEventBinding(
                    CustomUIEventBindingType.Activating,
                    "#BackButton",
                    EventData.of("_GoBack", "")
            );
        } else {
            uiCommandBuilder.set("#BackButton.Visible", false);
        }

        if (compact) {
            Anchor padding = new Anchor();
            padding.setHorizontal(Value.of(10));
            padding.setVertical(Value.of(6));
            uiCommandBuilder.setObject("#SettingsList.Padding", padding);
        }

        int index = 0;
        ExtraInfo threadLocalInfo = ExtraInfo.THREAD_LOCAL.get();
        Data currentData = store.getComponent(ref, componentType);
        if (currentData == null) {
            HytaleLogger.getLogger().atWarning().log("No existing component data found for ref %s, using default instance", ref);
            currentData = eventDataCodec.getSupplier().get();
            store.addComponent(ref, componentType, currentData);
        }
        BsonDocument document = eventDataCodec.encode(currentData, threadLocalInfo);
        HytaleLogger.getLogger().atInfo().log("Encoded component data for ref %s into document: %s", ref, document.toJson());

        Map<String, List<BuilderField<Data, ?>>> entries = eventDataCodec.getEntries();

        for (Map.Entry<String, List<BuilderField<Data, ?>>> entry : entries.entrySet()) {
            String key = entry.getKey();
            List<BuilderField<Data, ?>> fields = entry.getValue();

            if (fields == null || fields.isEmpty()) {
                continue;
            }

            BuilderField<Data, ?> firstField = fields.getFirst();
            if (firstField == null) {
                continue;
            }
            Codec<?> codec = firstField.getCodec().getChildCodec();

            String valueKey = key.startsWith("@") ? key.substring(1) : key;
            String displayName = formatDisplayName(valueKey);

            BsonValue value = document.get(valueKey);

            if (value == null) {
                HytaleLogger.getLogger().atInfo().log("No value found in document for field '%s'", valueKey);
            }

            CodecUIProvider<?> provider = GeneratedSettingsRegistry.get(codec);
            if (provider != null) {
                provider.buildCodec(uiCommandBuilder, uiEventBuilder, index, displayName, key, value);
                if (compact) {
                    String selector = "#SettingsList[" + index + "]";
                    Anchor itemPadding = new Anchor();
                    itemPadding.setHorizontal(Value.of(10));
                    itemPadding.setTop(Value.of(4));
                    uiCommandBuilder.setObject(selector + ".Padding", itemPadding);

                }
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

        Data currentData = store.getComponent(ref, componentType);
        if (currentData == null) {
            currentData = eventDataCodec.getSupplier().get();
            store.addComponent(ref, componentType, currentData);
        }

        try {
            BsonDocument incomingDoc = BsonDocument.parse(data.replaceAll("\"@", "\""));
            ExtraInfo threadLocalInfo = ExtraInfo.THREAD_LOCAL.get();

            boolean dirty = false;
            // TODO : There's got to be a better way to do this
            BsonDocument filteredDoc = eventDataCodec.originalCodec.encode(currentData, threadLocalInfo);
            for (Map.Entry<String, BsonValue> entry : incomingDoc.entrySet()) {
                if (entry.getValue() != null && !entry.getValue().isNull()) {
                    filteredDoc.put(entry.getKey(), entry.getValue());
                    dirty = true;
                }
            }

            if (dirty) {
                eventDataCodec.originalCodec.decode(filteredDoc, currentData, threadLocalInfo);
            }
            sendUpdate();
        } catch (Exception e) {
            HytaleLogger.getLogger().atWarning().withCause(e).log("Failed to sparsely decode UI data update for ref %s: %s", ref, data);
        }
    }


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
                if (key.startsWith("@")) {
                    throw new IllegalArgumentException("Codec keys cannot start with '@' as it is reserved for UI field mapping. Invalid key: " + key);
                }
                String uiKey = "@" + key;
                entries.put(uiKey, fields);
            });
        }
    }
}
