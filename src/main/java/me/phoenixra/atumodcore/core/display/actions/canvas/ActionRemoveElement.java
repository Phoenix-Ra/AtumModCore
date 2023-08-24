package me.phoenixra.atumodcore.core.display.actions.canvas;

import me.phoenixra.atumodcore.api.display.actions.ActionData;
import me.phoenixra.atumodcore.api.display.actions.DisplayAction;

public class ActionRemoveElement implements DisplayAction {
    @Override
    public void perform(ActionData data) {
        if(data.getAttachedCanvas() == null || data.getAttachedElement() == null) return;
        data.getAttachedCanvas().removeElement(data.getAttachedElement());
    }
}
