package me.phoenixra.atumodcore.core.display.actions;

import me.phoenixra.atumodcore.api.display.actions.ActionData;
import me.phoenixra.atumodcore.api.display.actions.DisplayAction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiWorldSelection;

public class ActionOpenSingleplayer implements DisplayAction {
    @Override
    public void perform(ActionData data) {
        if(data.getAttachedElement() == null ||
                data.getAttachedElement().getElementOwner().getAttachedGuiScreen() == null) return;
        Minecraft.getMinecraft().displayGuiScreen(
                new GuiWorldSelection(
                        data.getAttachedElement().getElementOwner().getAttachedGuiScreen()
                )
        );
    }
}
