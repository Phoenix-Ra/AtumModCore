package me.phoenixra.atumodcore.core.display.actions.server;

import me.phoenixra.atumodcore.api.display.DisplayRenderer;
import me.phoenixra.atumodcore.api.display.actions.ActionData;
import me.phoenixra.atumodcore.api.display.actions.DisplayAction;
import me.phoenixra.atumodcore.api.display.annotations.RegisterDisplayAction;
import me.phoenixra.atumodcore.api.network.data.DisplayEventData;

/**
 * Action that sends a display event to the server.
 * <br>
 * Usage Example: 'send_display_event@integer'
 */
@RegisterDisplayAction(templateId = "send_display_event")
public class ActionSendDisplayEvent implements DisplayAction {
    @Override
    public void perform(ActionData data) {
        if(data.getAttachedElement()==null || data.getActionArgs() == null) return;
        DisplayRenderer renderer = data.getAttachedElement().getElementOwner().getDisplayRenderer();
        if(renderer == null) return;
        renderer.sendDisplayEvent(new DisplayEventData(
                        data.getAtumMod().getModID(),
                        data.getAttachedElement().getId(),
                        renderer.getId(),
                        Integer.parseInt(
                                data.getActionArgs().getArgs()[0]
                        )

                ));
    }
}
