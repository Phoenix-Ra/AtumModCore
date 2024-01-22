package me.phoenixra.atumodcore.core.display.actions.canvas;

import me.phoenixra.atumodcore.api.display.actions.ActionData;
import me.phoenixra.atumodcore.api.display.actions.DisplayAction;
import me.phoenixra.atumodcore.api.display.annotations.RegisterDisplayAction;

@RegisterDisplayAction(templateId = "add_element")
public class ActionAddElement implements DisplayAction {
    @Override
    public void perform(ActionData data) {
        if(data.getAttachedElement() == null) return;
        data.getAttachedElement().getElementOwner().addElement(data.getAttachedElement());
    }
}
