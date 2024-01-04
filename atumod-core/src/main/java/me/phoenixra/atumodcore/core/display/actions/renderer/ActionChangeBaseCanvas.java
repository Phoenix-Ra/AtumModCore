package me.phoenixra.atumodcore.core.display.actions.renderer;

import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.actions.ActionData;
import me.phoenixra.atumodcore.api.display.actions.DisplayAction;

public class ActionChangeBaseCanvas implements DisplayAction {
    @Override
    public void perform(ActionData data) {
        if(data.getAttachedElement()==null) return;
        String canvasTemplateId = data.getArgs()[0];
        if(canvasTemplateId==null) return;
        DisplayCanvas canvas = data.getAtumMod().getDisplayManager().
                getElementRegistry().getDrawableCanvas(canvasTemplateId);
        if(canvas==null) return;
        data.getAttachedElement().getElementOwner().
                getDisplayRenderer()
                .setBaseCanvas(
                        canvas
                );
    }
}
