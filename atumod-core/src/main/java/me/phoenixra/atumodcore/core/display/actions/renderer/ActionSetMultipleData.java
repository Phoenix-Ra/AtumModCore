package me.phoenixra.atumodcore.core.display.actions.renderer;

import me.phoenixra.atumodcore.api.display.actions.ActionData;
import me.phoenixra.atumodcore.api.display.actions.DisplayAction;
import me.phoenixra.atumodcore.api.display.annotations.RegisterDisplayAction;

@RegisterDisplayAction(templateId = "set_multiple_data")
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
            else if(data.getActionArgs().getArgs().length>2
                    && data.getActionArgs()
                    .getArgs()[2].equalsIgnoreCase("temp")){
                data.getAttachedElement().getElementOwner().getDisplayRenderer().getDisplayData()
                        .setTemporaryData(
                                data.getActionArgs().getArgs()[0],
                                data.getActionArgs().getArgs()[1],
                                Long.parseLong(data.getActionArgs().getArgs()[3]),
                                false
                        );
            }
            else if(data.getActionArgs().getArgs().length>2
                    && data.getActionArgs()
                    .getArgs()[2].equalsIgnoreCase("tempQueued")){
                data.getAttachedElement().getElementOwner().getDisplayRenderer().getDisplayData()
                        .setTemporaryData(
                                data.getActionArgs().getArgs()[0],
                                data.getActionArgs().getArgs()[1],
                                Long.parseLong(data.getActionArgs().getArgs()[3]),
                                true
                        );
            }
        }
    }
}
