package me.phoenixra.atumodcore.core.display.actions;

import me.phoenixra.atumodcore.api.display.actions.ActionData;
import me.phoenixra.atumodcore.api.display.actions.DisplayAction;

public class ActionQuit implements DisplayAction {
    @Override
    public void perform(ActionData data) {
        data.getMinecraft().shutdown();
    }
}
