package me.phoenixra.atumodcore.core.display.actions.renderer;

import me.phoenixra.atumodcore.api.display.actions.ActionData;
import me.phoenixra.atumodcore.api.display.actions.DisplayAction;
import me.phoenixra.atumodcore.api.display.annotations.RegisterDisplayAction;

@RegisterDisplayAction(templateId = "remove_multiple_data")
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
