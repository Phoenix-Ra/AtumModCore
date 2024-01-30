package me.phoenixra.atumodcore.core.display.actions.renderer;

import me.phoenixra.atumodcore.api.display.DisplayRenderer;
import me.phoenixra.atumodcore.api.display.actions.ActionData;
import me.phoenixra.atumodcore.api.display.actions.DisplayAction;
import me.phoenixra.atumodcore.api.display.annotations.RegisterDisplayAction;

/**
 * Action that sets data to the renderer. <br>
 * <br>
 * Usage Example: 'setData@dataId;dataValue'
 * <br>
 * Temporary Usage Example: 'setData@dataId;dataValue;temp;timeInMilliseconds'
 * <br>
 * Temporary Queued Usage Example: 'setData@dataId;dataValue;tempQueued;timeInMilliseconds'
 *
 */
@RegisterDisplayAction(templateId = "set_data")
public class ActionSetData implements DisplayAction {
    @Override
    public void perform(ActionData data) {
        if(data.getAttachedElement()==null || data.getActionArgs() == null) return;
        String[] args = data.getActionArgs().getArgs();
        DisplayRenderer renderer = data.getAttachedElement().getElementOwner().getDisplayRenderer();
        if(renderer == null) return;
        if(args.length<2) return;
        if(args.length>2 && data.getActionArgs()
                .getArgs()[2].equalsIgnoreCase("temp")){
            renderer.getDisplayData()
                    .setTemporaryData(
                            args[0],
                            args[1],
                            Long.parseLong(args[3]),
                            false
                    );
            return;
        }
        if(args.length>2 && data.getActionArgs()
                .getArgs()[2].equalsIgnoreCase("tempQueued")){
            renderer.getDisplayData()
                    .setTemporaryData(
                            args[0],
                            args[1],
                            Long.parseLong(args[3]),
                            true
                    );
            return;
        }

        renderer.getDisplayData().setData(
                        args[0],
                        args[1]
                );
    }
}
