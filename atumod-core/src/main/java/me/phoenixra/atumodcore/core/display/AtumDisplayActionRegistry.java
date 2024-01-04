package me.phoenixra.atumodcore.core.display;

import lombok.Getter;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.display.actions.DisplayAction;
import me.phoenixra.atumodcore.api.display.actions.DisplayActionRegistry;
import me.phoenixra.atumodcore.core.display.actions.*;
import me.phoenixra.atumodcore.core.display.actions.canvas.ActionAddElement;
import me.phoenixra.atumodcore.core.display.actions.canvas.ActionRemoveElement;
import me.phoenixra.atumodcore.core.display.actions.renderer.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AtumDisplayActionRegistry implements DisplayActionRegistry {

    @Getter
    private final AtumMod atumMod;

    private Map<String, DisplayAction> registry = new ConcurrentHashMap<>();
    public AtumDisplayActionRegistry(AtumMod atumMod) {
        this.atumMod = atumMod;

        register("add_element", new ActionAddElement());
        register("remove_element", new ActionRemoveElement());

        register("change_base_canvas", new ActionChangeBaseCanvas());
        register("set_data", new ActionSetData());
        register("set_multiple_data", new ActionSetMultipleData());
        register("remove_data", new ActionRemoveData());
        register("remove_multiple_data", new ActionRemoveMultipleData());
        register("clear_data", new ActionClearData());

        register("open_gui", new ActionOpenGui());
        register("close_gui", new ActionCloseGui());
        register("quit", new ActionQuit());
        register("open_settings", new ActionOpenSettings());
        register("open_singleplayer", new ActionOpenSingleplayer());
        register("open_multiplayer", new ActionOpenMultiplayer());
        register("open_link", new ActionOpenLink());
        register("connect_to_server", new ActionConnectToServer());
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
