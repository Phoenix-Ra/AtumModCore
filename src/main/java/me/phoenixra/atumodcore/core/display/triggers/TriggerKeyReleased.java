package me.phoenixra.atumodcore.core.display.triggers;

import me.phoenixra.atumodcore.api.display.triggers.DisplayTrigger;
import me.phoenixra.atumodcore.api.display.triggers.TriggerData;
import me.phoenixra.atumodcore.api.events.display.ElementInputReleaseEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TriggerKeyReleased  extends DisplayTrigger {

    @SubscribeEvent
    public void onKeyReleased(ElementInputReleaseEvent e) {
        if(!e.getClickedElement().equals(getAttachedElement())) {
            return;
        }

        dispatch(
                TriggerData.builder()
                        .attachedElement(getAttachedElement())
                        .attachedEvent(e)
                        .mouseX(e.getParentEvent().getMouseX())
                        .mouseY(e.getParentEvent().getMouseY())
                        .build()
        );
    }

}
