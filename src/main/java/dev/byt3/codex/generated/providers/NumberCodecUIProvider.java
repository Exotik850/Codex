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

public class NumberCodecUIProvider implements CodecUIProvider<Number> {

    @Override
    public void buildCodec(@Nonnull UICommandBuilder uiCommandBuilder, @Nonnull UIEventBuilder uiEventBuilder, int index, @Nonnull String displayName, @Nonnull String fieldKey, @Nullable BsonValue value) {
        String selector = "#SettingsList[" + index + "]";

        uiCommandBuilder.append("#SettingsList", "Pages/Prefabs/NumberSetting.ui");
        uiCommandBuilder.set(selector + " #SettingName.Text", displayName);

        if (value != null) {
            ExtraInfo threadLocalInfo = ExtraInfo.THREAD_LOCAL.get();
            Number decoded = Codec.FLOAT.decode(value, threadLocalInfo);
            uiCommandBuilder.set(selector + " #NumberControl.Value", decoded.floatValue());
        }

        uiEventBuilder.addEventBinding(
                CustomUIEventBindingType.ValueChanged,
                selector + " #NumberControl",
                EventData.of(fieldKey, selector + " #NumberControl.Value"),
                false
        );
    }
}


