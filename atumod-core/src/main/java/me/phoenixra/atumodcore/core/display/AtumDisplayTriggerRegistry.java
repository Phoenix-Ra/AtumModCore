package me.phoenixra.atumodcore.core.display;

import lombok.Getter;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.display.triggers.DisplayTrigger;
import me.phoenixra.atumodcore.api.display.triggers.DisplayTriggerRegistry;
import me.phoenixra.atumodcore.core.display.triggers.TriggerDataChanged;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AtumDisplayTriggerRegistry implements DisplayTriggerRegistry {
    @Getter
    private final AtumMod atumMod;

    private Map<String, DisplayTrigger> registry = new ConcurrentHashMap<>();

    public AtumDisplayTriggerRegistry(@NotNull AtumMod atumMod) {
        this.atumMod = atumMod;

        registerTemplate("data_changed", new TriggerDataChanged(atumMod));
    }

    @Override
    public void registerTemplate(@NotNull String id,
                                 @NotNull DisplayTrigger trigger) {
        registry.put(id, trigger);
    }

    @Override
    public void unregisterTemplate(@NotNull String id) {
        registry.remove(id);
    }

    @Override
    public DisplayTrigger getTemplate(@NotNull String id) {
        return registry.get(id);
    }

}
