package dev.byt3.codex.hubsettings;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
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

public class HubSettingsPage extends InteractiveCustomUIPage<HubSettingsPage.HubConfigData> {

    public HubSettingsPage(@Nonnull PlayerRef playerRef) {
        super(playerRef, CustomPageLifetime.CanDismissOrCloseThroughInteraction, HubConfigData.CODEC);
    }

    @Override
    public void build(@Nonnull Ref<EntityStore> ref, @Nonnull UICommandBuilder commandBuilder, @Nonnull UIEventBuilder eventBuilder, @Nonnull Store<EntityStore> store) {
        // Load the specific UI file for this page
        commandBuilder.append("Pages/HubSettings.ui");

        // Populate initial values (in a real scenario, fetch these from the player's ECS component)
        commandBuilder.set("#DarkModeToggle.Value", true);
        commandBuilder.set("#AnimationsToggle.Value", true);
        commandBuilder.set("#UIScaleSlider.Value", 1.0f);

        // Bind events for the UI controls
        eventBuilder.addEventBinding(
                CustomUIEventBindingType.ValueChanged,
                "#DarkModeToggle",
                EventData.of("Action", "UpdateDarkMode").append("DarkModeEnabled", "#DarkModeToggle.Value")
        );

        eventBuilder.addEventBinding(
                CustomUIEventBindingType.ValueChanged,
                "#AnimationsToggle",
                EventData.of("Action", "UpdateAnimations").append("AnimationsEnabled", "#AnimationsToggle.Value")
        );

        eventBuilder.addEventBinding(
                CustomUIEventBindingType.ValueChanged,
                "#UIScaleSlider",
                EventData.of("Action", "UpdateScale").append("UIScale", "#UIScaleSlider.Value")
        );

        // Bind the back button to return to the main hub
        eventBuilder.addEventBinding(
                CustomUIEventBindingType.Activating,
                "#BackButton",
                EventData.of("Action", "GoBack")
        );
    }

    @Override
    public void handleDataEvent(@Nonnull Ref<EntityStore> ref, @Nonnull Store<EntityStore> store, HubConfigData data) {
        super.handleDataEvent(ref, store, data);
        Player player = store.getComponent(ref, Player.getComponentType());
        if (player == null) return;

        // Route the action
        if ("GoBack".equals(data.action)) {
            player.getPageManager().openCustomPage(ref, store, new PlayerSettingsMainPage(this.playerRef));
            return; // No need to sendUpdate() if we are transitioning pages
        }

        if ("UpdateDarkMode".equals(data.action) && data.darkModeEnabled != null) {
            // TODO: Save to ECS Component
            System.out.println("Dark mode updated to: " + data.darkModeEnabled);
        } else if ("UpdateAnimations".equals(data.action) && data.animationsEnabled != null) {
            // TODO: Save to ECS Component
            System.out.println("Animations updated to: " + data.animationsEnabled);
        } else if ("UpdateScale".equals(data.action) && data.uiScale != null) {
            // TODO: Save to ECS Component
            System.out.println("UI Scale updated to: " + data.uiScale);
        }

        // Acknowledge the interaction so the client doesn't hang
        sendUpdate();
    }

    /**
     * Defines the structure of the payload sent from the client UI.
     * All fields except 'action' are nullable because they won't all be present in every event.
     */
    public static class HubConfigData {
        public String action;
        public Boolean darkModeEnabled;
        public Boolean animationsEnabled;
        public Float uiScale;

        public static final BuilderCodec<HubConfigData> CODEC = BuilderCodec.builder(HubConfigData.class, HubConfigData::new)
                .append(new KeyedCodec<>("Action", Codec.STRING), (o, a) -> o.action = a, o -> o.action)
                .add()
                .append(new KeyedCodec<>("DarkModeEnabled", Codec.BOOLEAN), (o, v) -> o.darkModeEnabled = v, o -> o.darkModeEnabled)
                .add()
                .append(new KeyedCodec<>("AnimationsEnabled", Codec.BOOLEAN), (o, v) -> o.animationsEnabled = v, o -> o.animationsEnabled)
                .add()
                .append(new KeyedCodec<>("UIScale", Codec.FLOAT), (o, v) -> o.uiScale = v, o -> o.uiScale)
                .add()
                .build();
    }
}