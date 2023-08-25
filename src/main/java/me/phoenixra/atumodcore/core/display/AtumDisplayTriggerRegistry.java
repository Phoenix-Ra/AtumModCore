package me.phoenixra.atumodcore.core.display;

import lombok.Getter;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.config.Config;
import me.phoenixra.atumodcore.api.display.actions.DisplayAction;
import me.phoenixra.atumodcore.api.display.triggers.DisplayTrigger;
import me.phoenixra.atumodcore.api.display.triggers.DisplayTriggerRegistry;
import me.phoenixra.atumodcore.core.display.triggers.TriggerKeyPressed;
import me.phoenixra.atumodcore.core.display.triggers.TriggerKeyReleased;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AtumDisplayTriggerRegistry implements DisplayTriggerRegistry{
    @Getter
    private final AtumMod atumMod;

    private HashMap<String, DisplayTrigger> registry = new HashMap<>();
    public AtumDisplayTriggerRegistry(AtumMod atumMod) {
        this.atumMod = atumMod;
        register("key_press", new TriggerKeyPressed());
        register("key_release", new TriggerKeyReleased());
    }

    @Override
    public List<DisplayTrigger> compile(@NotNull Config config) {
        List<DisplayTrigger> triggers = new ArrayList<>();
        for(String key : config.getKeys(false)){
            DisplayTrigger trigger = getTriggerById(key);
            if(trigger == null){
                continue;
            }
            //@TODO: Add support for multiple actions
            trigger = trigger.clone();
            MinecraftForge.EVENT_BUS.register(trigger);
            DisplayAction action = atumMod.getDisplayActionRegistry().getActionById(config.getString(key));
            if(action != null){
                trigger.addAction(action);
            }
            triggers.add(trigger);
        }

        return triggers;
    }

    @Override
    public @Nullable DisplayTrigger getTriggerById(@NotNull String id) {
        return registry.get(id.toLowerCase());
    }

    @Override
    public void register(@NotNull String id, @NotNull DisplayTrigger action) {
        registry.put(id.toLowerCase(), action);
    }
}
