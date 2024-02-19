package me.phoenixra.atumodcore.api.display.impl;

import lombok.Getter;
import me.phoenixra.atumconfig.api.config.Config;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.display.DisplayRenderer;
import me.phoenixra.atumodcore.api.display.actions.ActionArgs;
import me.phoenixra.atumodcore.api.display.actions.ActionData;
import me.phoenixra.atumodcore.api.display.actions.DisplayAction;
import me.phoenixra.atumodcore.api.display.triggers.DisplayTrigger;
import me.phoenixra.atumodcore.api.display.triggers.DisplayTriggerData;
import me.phoenixra.atumconfig.api.tuples.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseTrigger implements DisplayTrigger {

    @Getter
    private final AtumMod atumMod;
    @Getter
    private DisplayRenderer owner;
    private List<Pair<DisplayAction, ActionArgs>> actions;

    public BaseTrigger(AtumMod atumMod, DisplayRenderer renderer) {
        this.actions = new ArrayList<>();
        this.owner = renderer;
        this.atumMod = atumMod;
    }
    public BaseTrigger(AtumMod atumMod){
        this(atumMod,null);
    }

    @Override
    public void trigger(DisplayTriggerData triggerData) {
        if(!filter(triggerData)){
            return;
        }
        actions.forEach(it->it.getFirst().perform(
                ActionData.builder().atumMod(getOwner().getAtumMod())
                        .attachedElement(owner.getBaseCanvas())
                        .actionArgs(it.getSecond())
                        .build()
        ));
    }

    public abstract boolean filter(DisplayTriggerData triggerData);

    public abstract @NotNull DisplayTrigger updateVariables(@NotNull Config config);

    public abstract @NotNull DisplayTrigger onClone(@NotNull DisplayTrigger cloned);

    @Override
    public @NotNull DisplayTrigger cloneWithNewVariables(@NotNull Config config, @Nullable DisplayRenderer owner) {
        BaseTrigger clone = (BaseTrigger) clone();
        clone.owner = owner;

        for(String key : config.getSubsection("actions").getKeys(false)){
            Pair<DisplayAction, ActionArgs> action = DisplayAction.parseActionFromString(
                    getAtumMod(),
                    config.getString("actions."+key)
            );
            if(action == null){
                continue;
            }
            clone.actions.add(action);

        }

        return clone.updateVariables(config);
    }

    @Override
    public @NotNull DisplayTrigger clone() {
        try {
            DisplayTrigger clone = (DisplayTrigger) super.clone();
            ((BaseTrigger)clone).actions = new ArrayList<>();
            return onClone(clone);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
