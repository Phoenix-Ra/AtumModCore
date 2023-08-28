package me.phoenixra.atumodcore.core.display.actions;

import me.phoenixra.atumodcore.api.display.actions.ActionData;
import me.phoenixra.atumodcore.api.display.actions.DisplayAction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiOptions;

public class ActionOpenSettings implements DisplayAction {
    @Override
    public void perform(ActionData data) {
        if(data.getAttachedElement() == null || data.getAttachedElement().getElementOwner().getAttachedGuiScreen() == null) return;
        Minecraft.getMinecraft().displayGuiScreen(
                new GuiOptions(
                        data.getAttachedElement().getElementOwner().getAttachedGuiScreen(),
                        Minecraft.getMinecraft().gameSettings
                )
        );
    }
}
