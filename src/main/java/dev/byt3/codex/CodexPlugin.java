package dev.byt3.codex;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class CodexPlugin extends JavaPlugin {
    private static HytaleLogger LOGGER = HytaleLogger.get("Codex");

    public CodexPlugin(@NonNullDecl JavaPluginInit init) {
        super(init);
    }

    @Override
    public void setup() {
        LOGGER.atInfo().log("Codex plugin has been loaded! (twice)");
    }
}
