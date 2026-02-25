package dev.byt3.codex.generated;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.builder.BuilderField;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.entities.player.pages.InteractiveCustomUIPage;
import com.hypixel.hytale.server.core.ui.builder.EventData;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import dev.byt3.codex.playersettings.PlayerSettingsMainPage;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static com.hypixel.hytale.server.core.universe.world.WorldConfig.formatDisplayName;

public class GeneratedSettingsPage<Data> extends InteractiveCustomUIPage<Data> {

    private final String displayName;
    private final BuilderCodec<Data> eventDataCodec;

    public GeneratedSettingsPage(@Nonnull PlayerRef playerRef, @Nonnull BuilderCodec<Data> dataCodec, @Nonnull String displayName) {
        super(playerRef, CustomPageLifetime.CanDismissOrCloseThroughInteraction, dataCodec);
        this.eventDataCodec = dataCodec;
        this.displayName = displayName;
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
                EventData.of("_Action", "_GoBack")
        );

        int index = 0;

        // Iterate over the map entries returned by getEntries()
        for (Map.Entry<String, List<BuilderField<Data, ?>>> entry : eventDataCodec.getEntries().entrySet()) {
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

            String selector = "#SettingsList[" + index + "]";
            String displayName = formatDisplayName(key);

            if (codec == Codec.STRING) {
                uiCommandBuilder.append("#SettingsList", "Pages/Prefabs/StringSetting.ui");
                uiCommandBuilder.set(selector + " #SettingName.Text", displayName);

                uiEventBuilder.addEventBinding(
                        CustomUIEventBindingType.ValueChanged,
                        selector + " #InputControl",
                        EventData.of(key, selector + " #InputControl.Value"),
                        false
                );
            } else if (codec == Codec.BOOLEAN) {
                uiCommandBuilder.append("#SettingsList", "Pages/Prefabs/BooleanSetting.ui");
                uiCommandBuilder.set(selector + " #SettingName.Text", displayName);

                uiEventBuilder.addEventBinding(
                        CustomUIEventBindingType.ValueChanged,
                        selector + " #ToggleControl",
                        EventData.of(key, selector + " #ToggleControl.Value"),
                        false
                );
            } else if (codec == Codec.FLOAT || codec == Codec.INTEGER || codec == Codec.DOUBLE) {
                uiCommandBuilder.append("#SettingsList", "Pages/Prefabs/NumberSetting.ui");
                uiCommandBuilder.set(selector + " #SettingName.Text", displayName);
                uiEventBuilder.addEventBinding(
                        CustomUIEventBindingType.ValueChanged,
                        selector + " #NumberControl",
                        EventData.of(key, selector + " #NumberControl.Value"),
                        false
                );
            }

            index++;
        }
    }

    @Override
    public void handleDataEvent(@Nonnull Ref<EntityStore> ref, @Nonnull Store<EntityStore> store, @Nonnull String data) {
        HytaleLogger.getLogger().atInfo().log("Received data event: " + data);
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
}
