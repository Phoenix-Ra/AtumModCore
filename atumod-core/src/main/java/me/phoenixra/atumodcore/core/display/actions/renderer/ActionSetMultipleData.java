package me.phoenixra.atumodcore.core.display.actions.renderer;

import me.phoenixra.atumodcore.api.display.actions.ActionData;
import me.phoenixra.atumodcore.api.display.actions.DisplayAction;

public class ActionSetMultipleData implements DisplayAction {
    @Override
    public void perform(ActionData data) {
        if(data.getAttachedElement()==null || data.getActionArgs() == null) return;
        for(String renderData : data.getActionArgs().getRawArgs().split(",")){
            String[] split = renderData.split(":");
            if(split.length == 2){
                data.getAttachedElement().getElementOwner().
                        getDisplayRenderer().getDisplayData()
                        .setData(
                                split[0],
                                split[1]
                        );
            }
        }
    }
}
