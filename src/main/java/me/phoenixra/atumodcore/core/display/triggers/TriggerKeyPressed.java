package me.phoenixra.atumodcore.core.display.triggers;

import me.phoenixra.atumodcore.api.display.triggers.DisplayTrigger;
import me.phoenixra.atumodcore.api.display.triggers.TriggerData;
import me.phoenixra.atumodcore.api.events.display.ElementInputPressEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TriggerKeyPressed extends DisplayTrigger {

    @SubscribeEvent
    public void onKeyPressed(ElementInputPressEvent e) {
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
