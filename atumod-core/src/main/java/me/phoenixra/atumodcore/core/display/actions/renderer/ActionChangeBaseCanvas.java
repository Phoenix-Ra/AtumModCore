package me.phoenixra.atumodcore.core.display.actions.renderer;

import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.DisplayRenderer;
import me.phoenixra.atumodcore.api.display.actions.ActionData;
import me.phoenixra.atumodcore.api.display.actions.DisplayAction;
import me.phoenixra.atumodcore.api.display.annotations.RegisterDisplayAction;

/**
 * Action that changes the base canvas of the renderer.
 * <br>
 * Usage Example: 'change_base_canvas@canvas_template_id'
 */
@RegisterDisplayAction(templateId = "change_base_canvas")
public class ActionChangeBaseCanvas implements DisplayAction {
    @Override
    public void perform(ActionData data) {
        if(data.getAttachedElement()==null || data.getActionArgs() == null) return;
        String canvasTemplateId = data.getActionArgs().getArgs()[0];
        if(canvasTemplateId==null) return;
        DisplayCanvas canvas = data.getAtumMod().getDisplayManager().
                getElementRegistry().getDrawableCanvas(canvasTemplateId);
        if(canvas==null) return;
        DisplayRenderer renderer = data.getAttachedElement().getElementOwner().
                getDisplayRenderer();
        if(renderer == null) return;
        renderer.setBaseCanvas(
                        canvas
                );
    }
}
