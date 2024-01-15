package me.phoenixra.atumodcore.api.display;

import me.phoenixra.atumodcore.api.AtumAPI;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.config.Config;
import me.phoenixra.atumodcore.api.config.LoadableConfig;
import me.phoenixra.atumodcore.api.display.actions.DisplayActionRegistry;
import me.phoenixra.atumodcore.api.display.triggers.DisplayTriggerRegistry;
import me.phoenixra.atumodcore.api.tuples.PairRecord;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface DisplayManager {
    List<PairRecord<Integer, Integer>> SUPPORTED_RESOLUTIONS =
            Arrays.asList(
                    new PairRecord<>(1024, 728),
                    new PairRecord<>(1280, 720),
                    new PairRecord<>(1366, 768),
                    new PairRecord<>(1600, 900),
                    new PairRecord<>(1920, 1080),
                    new PairRecord<>(2560, 1080)
            );

    static int getCurrentResolutionIndex() {
        int width = Display.getWidth();
        int height = Display.getHeight();
        int i = 0;
        for (PairRecord<Integer, Integer> entry : SUPPORTED_RESOLUTIONS) {
            if (width == entry.getFirst() || height == entry.getSecond()) {
                return i;
            }
            i++;
        }
        return -1;
    }

    default void changeResolution(int index) {
        if (Display.isResizable()) {
            Display.setResizable(false);
        }
        int newWidth = SUPPORTED_RESOLUTIONS.get(index).getFirst();
        int newHeight = SUPPORTED_RESOLUTIONS.get(index).getSecond();
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
