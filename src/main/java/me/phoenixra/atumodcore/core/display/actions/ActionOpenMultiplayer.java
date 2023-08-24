package me.phoenixra.atumodcore.core.display.actions;

import me.phoenixra.atumodcore.api.display.actions.ActionData;
import me.phoenixra.atumodcore.api.display.actions.DisplayAction;
import net.minecraft.client.gui.GuiMultiplayer;

public class ActionOpenMultiplayer implements DisplayAction{
    @Override
    public void perform(ActionData data) {
        if(data.getAttachedGuiScreen() == null) return;
        data.getMinecraft().displayGuiScreen(new GuiMultiplayer(data.getAttachedGuiScreen()));
    }
}
