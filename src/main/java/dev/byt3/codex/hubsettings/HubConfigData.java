package dev.byt3.codex.hubsettings;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import dev.byt3.codex.CodexPlugin;

import javax.annotation.Nullable;

public class HubConfigData implements Component<EntityStore> {
    public Integer windowWidth;
    public Integer windowHeight;
    public Boolean showBackButton;
    public Boolean compactMode;

    public static final int DEFAULT_WINDOW_WIDTH = 600;
    public static final int DEFAULT_WINDOW_HEIGHT = 700;

    public static final BuilderCodec<HubConfigData> CODEC = BuilderCodec.builder(HubConfigData.class, HubConfigData::new)
            .append(new KeyedCodec<>("WindowWidth", Codec.INTEGER), (o, v) -> o.windowWidth = v, o -> o.windowWidth)
            .add()
            .append(new KeyedCodec<>("WindowHeight", Codec.INTEGER), (o, v) -> o.windowHeight = v, o -> o.windowHeight)
            .add()
            .append(new KeyedCodec<>("ShowBackButton", Codec.BOOLEAN), (o, v) -> o.showBackButton = v, o -> o.showBackButton)
            .add()
            .append(new KeyedCodec<>("CompactMode", Codec.BOOLEAN), (o, v) -> o.compactMode = v, o -> o.compactMode)
            .add()
            .build();

    public int getWindowWidth() {
        return windowWidth != null ? windowWidth : DEFAULT_WINDOW_WIDTH;
    }

    public int getWindowHeight() {
        return windowHeight != null ? windowHeight : DEFAULT_WINDOW_HEIGHT;
    }

    public boolean isShowBackButton() {
        return showBackButton == null || showBackButton;
    }

    public boolean isCompactMode() {
        return compactMode != null && compactMode;
    }

    @Nullable
    @Override
    public Component<EntityStore> clone() {
        HubConfigData clone = new HubConfigData();
        clone.windowWidth = this.windowWidth;
        clone.windowHeight = this.windowHeight;
        clone.showBackButton = this.showBackButton;
        clone.compactMode = this.compactMode;
        return clone;
    }

    public static ComponentType<EntityStore, HubConfigData> getComponentType() {
        return CodexPlugin.hcComponentType;
    }
}
