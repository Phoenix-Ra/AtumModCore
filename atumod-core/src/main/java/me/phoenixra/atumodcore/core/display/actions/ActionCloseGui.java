package me.phoenixra.atumodcore.core.display.actions;

import me.phoenixra.atumodcore.api.display.actions.ActionData;
import me.phoenixra.atumodcore.api.display.actions.DisplayAction;
import me.phoenixra.atumodcore.api.display.annotations.RegisterDisplayAction;
import net.minecraft.client.Minecraft;

/**
 * Action that closes the current GUI.
 * <br>
 * Usage Example: 'close_gui'
 */
@RegisterDisplayAction(templateId = "close_gui")
public class ActionCloseGui implements DisplayAction {
    @Override
    public void perform(ActionData data) {
        Minecraft.getMinecraft().displayGuiScreen(null);
    }
}
