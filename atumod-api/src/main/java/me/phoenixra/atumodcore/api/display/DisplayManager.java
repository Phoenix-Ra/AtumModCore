package me.phoenixra.atumodcore.api.display;

import me.phoenixra.atumodcore.api.AtumAPI;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.config.Config;
import me.phoenixra.atumodcore.api.config.LoadableConfig;
import me.phoenixra.atumodcore.api.display.actions.DisplayActionRegistry;
import me.phoenixra.atumodcore.api.display.misc.DisplayResolution;
import me.phoenixra.atumodcore.api.display.triggers.DisplayTriggerRegistry;
import me.phoenixra.atumodcore.api.service.AtumModService;
import me.phoenixra.atumodcore.api.tuples.PairRecord;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface DisplayManager{

    default void changeResolution(int index) {
        if (Display.isResizable()) {
            Display.setResizable(false);
        }
        int newWidth = DisplayResolution.values()[index].getWidth();
        int newHeight = DisplayResolution.values()[index].getHeight();
        if (newWidth == Display.getWidth() && newHeight == Display.getHeight()) {
            return;
        }
        try {
            Display.setDisplayMode(new DisplayMode(newWidth, newHeight));
            Display.setResizable(false);

            //should not be null anyway
            LoadableConfig settings = AtumAPI.getInstance().getCoreMod().getConfigManager()
                    .getConfig("settings");
            settings.set("resolution", index);
            settings.save();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    DisplayElementRegistry getElementRegistry();

    DisplayActionRegistry getActionRegistry();

    DisplayTriggerRegistry getTriggerRegistry();


    void setHUDCanvas(@NotNull DisplayCanvas canvas);

    DisplayCanvas getHUDCanvas();


    DisplayCanvas getEnabledCanvas(@NotNull String id);

    void registerEnabledCanvas(@NotNull String id, @NotNull DisplayCanvas canvas);

    void unregisterEnabledCanvas(@NotNull String id);

    /**
     * Get the mod.
     *
     * @return The mod instance.
     */
    @NotNull
    AtumMod getAtumMod();
}
