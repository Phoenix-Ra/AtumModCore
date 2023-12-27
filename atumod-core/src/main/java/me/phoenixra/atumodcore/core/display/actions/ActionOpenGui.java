package me.phoenixra.atumodcore.core.display.actions;

import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.actions.ActionData;
import me.phoenixra.atumodcore.api.display.actions.DisplayAction;
import me.phoenixra.atumodcore.api.display.impl.BaseScreen;
import net.minecraft.client.Minecraft;

public class ActionOpenGui implements DisplayAction {
    @Override
    public void perform(ActionData data) {
        String guiId = data.getArgs()[0];
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
