package me.phoenixra.atumodcore.core.display.actions;

import me.phoenixra.atumodcore.api.display.actions.ActionData;
import me.phoenixra.atumodcore.api.display.actions.DisplayAction;
import me.phoenixra.atumodcore.api.display.impl.BaseScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiWorldSelection;

public class ActionOpenSingleplayer implements DisplayAction {
    @Override
    public void perform(ActionData data) {
        if(data.getAttachedElement() == null) return;
        BaseScreen attachedGuiScreen=data.getAttachedElement().getElementOwner()
                .getDisplayRenderer().getAttachedGuiScreen();
        if(attachedGuiScreen == null) return;
        Minecraft.getMinecraft().displayGuiScreen(
                new GuiWorldSelection(
                        attachedGuiScreen
                )
        );
    }
}
