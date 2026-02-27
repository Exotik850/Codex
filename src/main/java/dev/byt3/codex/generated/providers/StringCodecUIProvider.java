package dev.byt3.codex.generated.providers;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.ExtraInfo;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType;
import com.hypixel.hytale.server.core.ui.builder.EventData;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import dev.byt3.codex.generated.CodecUIProvider;
import org.bson.BsonValue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/// Renders a {@link Codec#STRING} field as a text input.
public class StringCodecUIProvider implements CodecUIProvider<String> {

    @Override
    public void buildCodec(@Nonnull UICommandBuilder uiCommandBuilder, @Nonnull UIEventBuilder uiEventBuilder, int index, @Nonnull String displayName, @Nonnull String fieldKey, @Nullable BsonValue value) {
        String selector = "#SettingsList[" + index + "]";

        uiCommandBuilder.append("#SettingsList", "Pages/Prefabs/StringSetting.ui");
        uiCommandBuilder.set(selector + " #SettingName.Text", displayName);

        if (value != null) {
            ExtraInfo threadLocalInfo = ExtraInfo.THREAD_LOCAL.get();
            String valueString = Codec.STRING.decode(value, threadLocalInfo);
            if (valueString != null) {
                HytaleLogger.getLogger().atInfo().log("Decoded string value for field '%s': %s", fieldKey, valueString);
                uiCommandBuilder.set(selector + " #InputControl.Value", valueString);
            }
        }

        uiEventBuilder.addEventBinding(
                CustomUIEventBindingType.ValueChanged,
                selector + " #InputControl",
                EventData.of(fieldKey, selector + " #InputControl.Value"),
                false
        );
    }
}


