package me.phoenixra.atumodcore.core.display.actions.canvas;

import me.phoenixra.atumodcore.api.display.actions.ActionData;
import me.phoenixra.atumodcore.api.display.actions.DisplayAction;
import me.phoenixra.atumodcore.api.display.annotations.RegisterDisplayAction;

/**
 * Action that removes an element from the display.
 * <br>
 * OUTDATED, DO NOT USE.
 */
@RegisterDisplayAction(templateId = "remove_element")
public class ActionRemoveElement implements DisplayAction {
    @Override
    public void perform(ActionData data) {
        if(data.getAttachedElement() == null) return;
        data.getAttachedElement().getElementOwner().removeElement(data.getAttachedElement());
    }
}
