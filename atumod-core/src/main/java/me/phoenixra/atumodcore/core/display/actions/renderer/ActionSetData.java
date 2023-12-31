package me.phoenixra.atumodcore.core.display.actions.renderer;

import me.phoenixra.atumodcore.api.display.actions.ActionData;
import me.phoenixra.atumodcore.api.display.actions.DisplayAction;

public class ActionSetData implements DisplayAction {
    @Override
    public void perform(ActionData data) {
        if(data.getAttachedElement()==null || data.getActionArgs() == null) return;
        if(data.getActionArgs().getArgs().length<2) return;
        if(data.getActionArgs().getArgs().length>2
                && data.getActionArgs()
                .getArgs()[2].equalsIgnoreCase("temp")){
            data.getAttachedElement().getElementOwner().getDisplayRenderer().getDisplayData()
                    .setTemporaryData(
                            data.getActionArgs().getArgs()[0],
                            data.getActionArgs().getArgs()[1],
                            Long.parseLong(data.getActionArgs().getArgs()[3])
                    );
            return;
        }

        data.getAttachedElement().getElementOwner().
                getDisplayRenderer().getDisplayData()
                .setData(
                        data.getActionArgs().getArgs()[0],
                        data.getActionArgs().getArgs()[1]
                );
    }
}
