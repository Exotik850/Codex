package dev.byt3.codex.component;


import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nullable;

public class CodexComponent implements Component<EntityStore> {


    @Nullable
    @Override
    public Component<EntityStore> clone() {
        return null;
    }
}
