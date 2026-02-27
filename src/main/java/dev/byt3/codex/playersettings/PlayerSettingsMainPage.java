package dev.byt3.codex.playersettings;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType;
import com.hypixel.hytale.protocol.packets.interface_.Page;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.entities.player.pages.InteractiveCustomUIPage;
import com.hypixel.hytale.server.core.ui.builder.EventData;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/// Top-level settings page that lists all registered {@link PlayerSettingsProvider} categories as clickable buttons.
public class PlayerSettingsMainPage extends InteractiveCustomUIPage<PlayerSettingsMainPage.PageData> {
    public PlayerSettingsMainPage(@Nonnull PlayerRef playerRef) {
        super(playerRef, CustomPageLifetime.CanDismissOrCloseThroughInteraction, PageData.CODEC);
    }

    @Override
    public void build(@Nonnull Ref<EntityStore> ref, @Nonnull UICommandBuilder commandBuilder, @Nonnull UIEventBuilder eventBuilder, @Nonnull Store<EntityStore> store) {
        Player playerComponent = store.getComponent(ref, Player.getComponentType());
        if (playerComponent == null) return;

        commandBuilder.append("Pages/PlayerSettingsMain.ui");

        eventBuilder.addEventBinding(CustomUIEventBindingType.Activating, "#CloseButton", EventData.of("Action", "ClosePage"));

        List<PlayerSettingsProvider> providers = new ArrayList<>(PlayerSettingsRegistry.get().getProviders());

        for (int i = 0; i < providers.size(); i++) {
            PlayerSettingsProvider provider = providers.get(i);
            String selector = "#CategoryList[" + i + "]";

            commandBuilder.append("#CategoryList", "Pages/SettingsCategoryItem.ui");
            commandBuilder.set(selector + " #CategoryName.Text", provider.getDisplayName());

            if (provider.getIconPath() != null && !provider.getIconPath().isEmpty()) {
                commandBuilder.set(selector + " #CategoryIcon.AssetPath", provider.getIconPath());
            }

            eventBuilder.addEventBinding(
                    CustomUIEventBindingType.Activating,
                    selector + " #CategoryButton",
                    EventData.of("Action", "OpenCategory").append("ProviderId", provider.getId())
            );
        }
    }

    @Override
    public void handleDataEvent(@Nonnull Ref<EntityStore> ref, @Nonnull Store<EntityStore> store, @Nonnull PageData data) {
        Player player = store.getComponent(ref, Player.getComponentType());
        if (player == null) return;
        if ("ClosePage".equals(data.action)) {
            player.getPageManager().setPage(ref, store, Page.None);
            return;
        }
        if (!"OpenCategory".equals(data.action) || data.providerId == null) return;
        PlayerSettingsProvider provider = PlayerSettingsRegistry.get().getProvider(data.providerId);
        if (provider == null) return;
        player.getPageManager().openCustomPage(ref, store, provider.createSettingsPage(this.playerRef));
    }


    public static class PageData {
        public String action;
        public String providerId;

        public static final BuilderCodec<PageData> CODEC = BuilderCodec.builder(PageData.class, PageData::new)
                .append(new KeyedCodec<>("Action", Codec.STRING), (o, action) -> o.action = action, o -> o.action)
                .add()
                .append(new KeyedCodec<>("ProviderId", Codec.STRING), (o, id) -> o.providerId = id, o -> o.providerId)
                .add()
                .build();
    }
}
