package dev.byt3.codex.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.protocol.packets.interface_.Page;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.entities.player.pages.CustomUIPage;
import com.hypixel.hytale.server.core.entity.entities.player.pages.PageManager;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import dev.byt3.codex.playersettings.PlayerSettingsMainPage;

import javax.annotation.Nonnull;

public class OpenSettingsCommand extends AbstractPlayerCommand {

    public OpenSettingsCommand() {
        super("settings", "Open the Codex settings menu");
        this.addAliases("codexsettings", "codexconfig", "config");
    }

    @Override
    protected void execute(@Nonnull CommandContext commandContext, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
        Player player = store.getComponent(ref, Player.getComponentType());
        if (player == null) return;
        PageManager pageManager = player.getPageManager();
        CustomUIPage customPage = pageManager.getCustomPage();
        if (!(customPage instanceof PlayerSettingsMainPage)) {
            pageManager.openCustomPage(ref, store, new PlayerSettingsMainPage(playerRef));
        } else {
            pageManager.setPage(ref, store, Page.None);
        }
    }
}
