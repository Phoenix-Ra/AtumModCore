package me.phoenixra.atumodcore.core.display.actions.renderer;

import me.phoenixra.atumodcore.api.display.actions.ActionData;
import me.phoenixra.atumodcore.api.display.actions.DisplayAction;

public class ActionRemoveMultipleData implements DisplayAction {
    @Override
    public void perform(ActionData data) {
        if (data.getAttachedElement() == null || data.getActionArgs() == null) return;
        for (String renderData : data.getActionArgs().getRawArgs().split(",")) {
            data.getAttachedElement().getElementOwner().
                    getDisplayRenderer().getDisplayData()
                    .removeData(
                            renderData
                    );
        }
    }
}
