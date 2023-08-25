package me.phoenixra.atumodcore.api.display.actions;

import me.phoenixra.atumodcore.api.display.triggers.TriggerData;

@FunctionalInterface
public interface DisplayAction {
    void perform(TriggerData data);
}
