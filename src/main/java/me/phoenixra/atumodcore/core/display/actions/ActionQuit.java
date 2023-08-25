package me.phoenixra.atumodcore.core.display.actions;

import me.phoenixra.atumodcore.api.display.triggers.TriggerData;
import me.phoenixra.atumodcore.api.display.actions.DisplayAction;

public class ActionQuit implements DisplayAction {
    @Override
    public void perform(TriggerData data) {
        data.getMinecraft().shutdown();
    }
}
