package dev.byt3.codex;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import dev.byt3.codex.commands.OpenSettingsCommand;
import dev.byt3.codex.hubsettings.HubConfigData;
import dev.byt3.codex.playersettings.PlayerSettingsRegistry;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class CodexPlugin extends JavaPlugin {
    private static final HytaleLogger LOGGER = HytaleLogger.get("Codex");

    public CodexPlugin(@NonNullDecl JavaPluginInit init) {
        super(init);
    }

    @Override
    public void setup() {
        this.getCommandRegistry().registerCommand(new OpenSettingsCommand());
        PlayerSettingsRegistry.get().registerCodec("HubSettings", HubConfigData.CODEC);
        LOGGER.atInfo().log("Codex plugin has been loaded!");
    }
}
