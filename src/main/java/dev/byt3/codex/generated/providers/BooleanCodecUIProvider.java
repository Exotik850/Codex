package dev.byt3.codex.generated.providers;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.ExtraInfo;
import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType;
import com.hypixel.hytale.server.core.ui.builder.EventData;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import dev.byt3.codex.generated.CodecUIProvider;
import org.bson.BsonValue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BooleanCodecUIProvider implements CodecUIProvider<Boolean> {

    @Override
    public void buildCodec(@Nonnull UICommandBuilder uiCommandBuilder, @Nonnull UIEventBuilder uiEventBuilder, int index, @Nonnull String displayName, @Nonnull String fieldKey, @Nullable BsonValue value) {
        String selector = "#SettingsList[" + index + "]";

        uiCommandBuilder.append("#SettingsList", "Pages/Prefabs/BooleanSetting.ui");
        uiCommandBuilder.set(selector + " #SettingName.Text", displayName);

        if (value != null) {
            ExtraInfo threadLocalInfo = ExtraInfo.THREAD_LOCAL.get();
            Boolean decoded = Codec.BOOLEAN.decode(value, threadLocalInfo);
            uiCommandBuilder.set(selector + " #ToggleControl.Value", decoded);
        }

        uiEventBuilder.addEventBinding(
                CustomUIEventBindingType.ValueChanged,
                selector + " #ToggleControl",
                EventData.of(fieldKey, selector + " #ToggleControl.Value"),
                false
        );
    }
}


