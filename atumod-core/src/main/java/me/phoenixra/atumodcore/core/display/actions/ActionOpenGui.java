package me.phoenixra.atumodcore.core.display.actions;

import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.actions.ActionData;
import me.phoenixra.atumodcore.api.display.actions.DisplayAction;
import me.phoenixra.atumodcore.api.display.annotations.RegisterDisplayAction;
import me.phoenixra.atumodcore.api.display.impl.BaseScreen;
import net.minecraft.client.Minecraft;

/**
 * Action that opens a canvas as a gui.
 * <br>
 * Usage Example: 'open_gui@place_canvasId_here'
 */
@RegisterDisplayAction(templateId = "open_gui")
public class ActionOpenGui implements DisplayAction {
    @Override
    public void perform(ActionData data) {
        if(data.getActionArgs() == null) return;

        String guiId = data.getActionArgs().getArgs()[0];
        if(guiId == null) return;
        DisplayCanvas canvas = data.getAtumMod().getDisplayManager().getElementRegistry()
                .getDrawableCanvas(guiId);
        if(canvas == null) return;
        Minecraft.getMinecraft().displayGuiScreen(
                new BaseScreen(
                        data.getAtumMod(),
                        canvas
                )
        );
    }
}
