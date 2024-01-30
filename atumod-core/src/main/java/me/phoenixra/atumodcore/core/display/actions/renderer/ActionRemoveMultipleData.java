package me.phoenixra.atumodcore.core.display.actions.renderer;

import me.phoenixra.atumodcore.api.display.DisplayRenderer;
import me.phoenixra.atumodcore.api.display.actions.ActionData;
import me.phoenixra.atumodcore.api.display.actions.DisplayAction;
import me.phoenixra.atumodcore.api.display.annotations.RegisterDisplayAction;

/**
 * Action that removes multiple data from the display.
 * <br>
 * Usage Example: 'remove_multiple_data@data1,data2,data3'
 */
@RegisterDisplayAction(templateId = "remove_multiple_data")
public class ActionRemoveMultipleData implements DisplayAction {
    @Override
    public void perform(ActionData data) {
        if (data.getAttachedElement() == null || data.getActionArgs() == null) return;
        DisplayRenderer renderer = data.getAttachedElement().getElementOwner().
                getDisplayRenderer();
        if (renderer == null) return;
        for (String line : data.getActionArgs()
                .getRawArgs().split(",")) {
            renderer.getDisplayData().removeData(line);
        }
    }
}
