package dev.byt3.codex.hubsettings;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nullable;

public class HubConfigData implements Component<EntityStore> {
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

    @Nullable
    @Override
    public Component<EntityStore> clone() {
        HubConfigData clone = new HubConfigData();
        clone.action = this.action;
        clone.darkModeEnabled = this.darkModeEnabled;
        clone.animationsEnabled = this.animationsEnabled;
        clone.uiScale = this.uiScale;
        return clone;
    }
}
