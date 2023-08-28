package me.phoenixra.atumodcore.core.display;

import lombok.Getter;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.display.actions.DisplayAction;
import me.phoenixra.atumodcore.api.display.actions.DisplayActionRegistry;
import me.phoenixra.atumodcore.core.display.actions.ActionOpenSettings;
import me.phoenixra.atumodcore.core.display.actions.ActionOpenSingleplayer;
import me.phoenixra.atumodcore.core.display.actions.ActionQuit;
import me.phoenixra.atumodcore.core.display.actions.canvas.ActionAddElement;
import me.phoenixra.atumodcore.core.display.actions.canvas.ActionRemoveElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class AtumDisplayActionRegistry implements DisplayActionRegistry {

    @Getter
    private final AtumMod atumMod;

    private HashMap<String, DisplayAction> registry = new HashMap<>();
    public AtumDisplayActionRegistry(AtumMod atumMod) {
        this.atumMod = atumMod;

        register("add_element", new ActionAddElement());
        register("remove_element", new ActionRemoveElement());

        register("quit", new ActionQuit());
        register("open_settings", new ActionOpenSettings());
        register("open_singleplayer", new ActionOpenSingleplayer());
        register("open_multiplayer", new ActionOpenSingleplayer());
    }

    @Override
    public @Nullable DisplayAction getActionById(@NotNull String id) {
        return registry.get(id.toLowerCase());
    }

    @Override
    public void register(@NotNull String id, @NotNull DisplayAction action) {
        registry.put(id.toLowerCase(), action);
    }
    @Override
    public void unregister(@NotNull String id) {
        registry.remove(id.toLowerCase());
    }

}
