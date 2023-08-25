package me.phoenixra.atumodcore.core.display.actions;

import me.phoenixra.atumodcore.api.display.triggers.TriggerData;
import me.phoenixra.atumodcore.api.display.actions.DisplayAction;
import net.minecraft.client.gui.GuiOptions;

public class ActionOpenSettings implements DisplayAction {
    @Override
    public void perform(TriggerData data) {
        if(data.getAttachedElement() == null || data.getAttachedElement().getElementOwner().getAttachedGuiScreen() == null) return;
        data.getMinecraft().displayGuiScreen(
                new GuiOptions(
                        data.getAttachedElement().getElementOwner().getAttachedGuiScreen(),
                        data.getMinecraft().gameSettings
                )
        );
    }
}
