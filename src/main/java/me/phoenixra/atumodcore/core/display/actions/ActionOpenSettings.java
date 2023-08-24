package me.phoenixra.atumodcore.core.display.actions;

import me.phoenixra.atumodcore.api.display.actions.ActionData;
import me.phoenixra.atumodcore.api.display.actions.DisplayAction;
import net.minecraft.client.gui.GuiOptions;

public class ActionOpenSettings implements DisplayAction {
    @Override
    public void perform(ActionData data) {
        if(data.getAttachedGuiScreen() == null) return;
        data.getMinecraft().displayGuiScreen(
                new GuiOptions(
                        data.getAttachedGuiScreen(),
                        data.getMinecraft().gameSettings
                )
        );
    }
}
