package me.phoenixra.atumodcore.core.display.actions;

import me.phoenixra.atumodcore.api.display.actions.ActionData;
import me.phoenixra.atumodcore.api.display.actions.DisplayAction;

import java.awt.*;
import java.net.URI;

public class ActionOpenLink implements DisplayAction {
    @Override
    public void perform(ActionData data) {
        if(data.getActionArgs() == null) {
            System.out.println("No args provided for an action open_link");
            return;
        }
        if(data.getActionArgs().getArgs().length == 0) {
            System.out.println("No args provided for an action open_link");
            return;
        }
        try {
            Desktop.getDesktop().browse(new URI(data.getActionArgs().getArgs()[0]));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
