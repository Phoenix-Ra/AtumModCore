package me.phoenixra.atumodcore.core.display.actions.renderer;

import me.phoenixra.atumodcore.api.display.DisplayRenderer;
import me.phoenixra.atumodcore.api.display.actions.ActionData;
import me.phoenixra.atumodcore.api.display.actions.DisplayAction;
import me.phoenixra.atumodcore.api.display.annotations.RegisterDisplayAction;

/**
 * Action that clears the display data.
 * <br>
 * Usage Example: 'clear_data'
 */
@RegisterDisplayAction(templateId = "clear_data")
public class ActionClearData implements DisplayAction {
    @Override
    public void perform(ActionData data) {
        if(data.getAttachedElement()==null) return;
        DisplayRenderer renderer = data.getAttachedElement().getElementOwner().
                getDisplayRenderer();
        if(renderer == null) return;
        renderer.getDisplayData()
                .clearData();
    }
}
