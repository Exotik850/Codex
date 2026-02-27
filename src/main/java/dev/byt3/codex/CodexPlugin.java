package dev.byt3.codex;

import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import dev.byt3.codex.commands.OpenSettingsCommand;
import dev.byt3.codex.hubsettings.HubConfigData;
import dev.byt3.codex.playersettings.PlayerSettingsRegistry;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

/// Entry point for the Codex mod. Registers the /settings command, built-in hub config, and wires them into the settings registry.
public class CodexPlugin extends JavaPlugin {
    private static final HytaleLogger LOGGER = HytaleLogger.get("Codex");

    // Stored statically so HubConfigData can look itself up without a plugin reference.
    public static ComponentType<EntityStore, HubConfigData> hcComponentType;

    public CodexPlugin(@NonNullDecl JavaPluginInit init) {
        super(init);
    }

    @Override
    public void setup() {
        this.getCommandRegistry().registerCommand(new OpenSettingsCommand());
        hcComponentType = this.getEntityStoreRegistry().registerComponent(HubConfigData.class, "HubConfigData", HubConfigData.CODEC);
        PlayerSettingsRegistry.get().registerCodec("HubSettings", HubConfigData.CODEC, hcComponentType);
        LOGGER.atInfo().log("Codex plugin has been loaded!");
    }
}
