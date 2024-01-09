package me.phoenixra.atumodcore.api.display.impl;

import lombok.Getter;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.config.Config;
import me.phoenixra.atumodcore.api.display.DisplayRenderer;
import me.phoenixra.atumodcore.api.display.actions.ActionArgs;
import me.phoenixra.atumodcore.api.display.actions.ActionData;
import me.phoenixra.atumodcore.api.display.actions.DisplayAction;
import me.phoenixra.atumodcore.api.display.triggers.DisplayTrigger;
import me.phoenixra.atumodcore.api.display.triggers.DisplayTriggerData;
import me.phoenixra.atumodcore.api.tuples.Pair;
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

    @Override
    public DisplayTrigger cloneWithNewVariables(@NotNull Config config, @Nullable DisplayRenderer owner) {
        DisplayTrigger clone = clone();
        if(clone == null){
            return null;
        }
        ((BaseTrigger)clone).owner = owner;

        for(String key : config.getSubsection("actions").getKeys(false)){
            Pair<DisplayAction, ActionArgs> action = DisplayAction.parseActionFromString(
                    getOwner().getAtumMod(),
                    config.getString("actions."+key)
            );
            if(action == null){
                continue;
            }
            ((BaseTrigger)clone).actions.add(action);

        }

        return clone;
    }

    @Override
    public DisplayTrigger clone() {
        try {
            DisplayTrigger clone = (DisplayTrigger) super.clone();
            ((BaseTrigger)clone).actions = new ArrayList<>();
            return clone;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
