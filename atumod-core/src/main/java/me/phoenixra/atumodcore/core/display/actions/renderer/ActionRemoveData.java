package me.phoenixra.atumodcore.core.display.actions.renderer;

import me.phoenixra.atumodcore.api.display.DisplayRenderer;
import me.phoenixra.atumodcore.api.display.actions.ActionData;
import me.phoenixra.atumodcore.api.display.actions.DisplayAction;
import me.phoenixra.atumodcore.api.display.annotations.RegisterDisplayAction;

/**
 * Action that removes data from the display.
 * <br>
 * Usage Example: 'remove_data@data'
 */
@RegisterDisplayAction(templateId = "remove_data")
public class ActionRemoveData implements DisplayAction {
    @Override
    public void perform(ActionData data) {
        if(data.getAttachedElement()==null || data.getActionArgs() == null) return;
        DisplayRenderer renderer = data.getAttachedElement().getElementOwner().
                getDisplayRenderer();
        if(renderer == null) return;
        renderer.getDisplayData()
                .removeData(
                        data.getActionArgs().getArgs()[0]
                );
    }
}
