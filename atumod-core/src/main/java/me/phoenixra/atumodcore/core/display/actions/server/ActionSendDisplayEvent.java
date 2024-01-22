package me.phoenixra.atumodcore.core.display.actions.server;

import me.phoenixra.atumodcore.api.display.actions.ActionData;
import me.phoenixra.atumodcore.api.display.actions.DisplayAction;
import me.phoenixra.atumodcore.api.display.annotations.RegisterDisplayAction;
import me.phoenixra.atumodcore.api.network.data.DisplayEventData;

@RegisterDisplayAction(templateId = "send_display_event")
public class ActionSendDisplayEvent implements DisplayAction {
    @Override
    public void perform(ActionData data) {
        if(data.getAttachedElement()==null || data.getActionArgs() == null) return;
        data.getAtumMod().getNetworkManager()
                .sendDisplayEvent(new DisplayEventData(
                        data.getAtumMod().getModID(),
                        data.getAttachedElement().getElementOwner().getId(),
                        data.getAttachedElement().getId(),
                        Integer.parseInt(
                                data.getActionArgs().getArgs()[0]
                        )

                ));
    }
}
