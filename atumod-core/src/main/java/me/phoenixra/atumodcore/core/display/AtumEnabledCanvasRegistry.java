package me.phoenixra.atumodcore.core.display;

import lombok.Getter;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.DisplayElement;
import me.phoenixra.atumodcore.api.display.EnabledCanvasRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AtumEnabledCanvasRegistry implements EnabledCanvasRegistry {

    @Getter
    private AtumMod atumMod;
    private Map<String, DisplayCanvas> registry = new ConcurrentHashMap<>();
    public AtumEnabledCanvasRegistry(AtumMod atumMod) {
        this.atumMod = atumMod;

    }
    @Override
    public DisplayCanvas getCanvasById(@NotNull String id) {
        return registry.get(id);
    }

    @Override
    public void registerCanvas(@NotNull String id, @NotNull DisplayCanvas canvas) {
        registry.put(id, canvas);
    }

    @Override
    public void unregisterCanvas(@NotNull String id) {
        registry.remove(id);
    }

}
