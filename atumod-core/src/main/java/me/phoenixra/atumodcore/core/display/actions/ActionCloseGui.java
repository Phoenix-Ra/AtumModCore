package me.phoenixra.atumodcore.core.display.actions;

import me.phoenixra.atumodcore.api.display.actions.ActionData;
import me.phoenixra.atumodcore.api.display.actions.DisplayAction;
import me.phoenixra.atumodcore.api.display.annotations.RegisterDisplayAction;
import net.minecraft.client.Minecraft;

@RegisterDisplayAction(templateId = "close_gui")
public class ActionCloseGui implements DisplayAction {
    @Override
    public void perform(ActionData data) {
        Minecraft.getMinecraft().displayGuiScreen(null);
    }
}
