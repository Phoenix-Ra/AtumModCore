package me.phoenixra.atumodcore.core.display.actions.renderer;

import me.phoenixra.atumodcore.api.display.actions.ActionData;
import me.phoenixra.atumodcore.api.display.actions.DisplayAction;

public class ActionRemoveData implements DisplayAction {
    @Override
    public void perform(ActionData data) {
        if(data.getAttachedElement()==null || data.getActionArgs() == null) return;
        data.getAttachedElement().getElementOwner().
                getDisplayRenderer().getDisplayData()
                .removeData(
                        data.getActionArgs().getArgs()[0]
                );
    }
}
