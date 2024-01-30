package me.phoenixra.atumodcore.core.display.actions.renderer;

import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.DisplayRenderer;
import me.phoenixra.atumodcore.api.display.actions.ActionData;
import me.phoenixra.atumodcore.api.display.actions.DisplayAction;
import me.phoenixra.atumodcore.api.display.annotations.RegisterDisplayAction;

/**
 * Action that sets multiple data.
 * <br>
 * Usage Example: 'set_multiple_data@dataId1;dataValue1,dataId2;dataValue2'
 * <br>
 * Divider: ","
 * <br>
 * Supports temporary data
 *
 */
@RegisterDisplayAction(templateId = "set_multiple_data")
public class ActionSetMultipleData implements DisplayAction {
    @Override
    public void perform(ActionData data) {
        if(data.getAttachedElement()==null || data.getActionArgs() == null) return;
        DisplayRenderer renderer = data.getAttachedElement()
                .getElementOwner().getDisplayRenderer();
        if(renderer == null) return;

        for(String line : data.getActionArgs().getRawArgs().split(",")){
            String[] lineArgs = line.split(";");
            if(lineArgs.length == 2){
                renderer.getDisplayData()
                        .setData(
                                lineArgs[0],
                                lineArgs[1]
                        );
                return;
            }
            else if(lineArgs.length>2
                    && lineArgs[2].equalsIgnoreCase("temp")){
                renderer.getDisplayData()
                        .setTemporaryData(
                                lineArgs[0],
                                lineArgs[1],
                                Long.parseLong(lineArgs[3]),
                                false
                        );
            }
            else if(lineArgs.length>2
                    && lineArgs[2].equalsIgnoreCase("tempQueued")){
                renderer.getDisplayData()
                        .setTemporaryData(
                                lineArgs[0],
                                lineArgs[1],
                                Long.parseLong(lineArgs[3]),
                                true
                        );
            }
        }
    }
}
