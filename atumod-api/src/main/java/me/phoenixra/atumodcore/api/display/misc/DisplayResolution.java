package me.phoenixra.atumodcore.api.display.misc;

import lombok.Getter;
import me.phoenixra.atumodcore.api.AtumAPI;
import me.phoenixra.atumconfig.api.config.LoadableConfig;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

/**
 * The display resolution.
 * <p>
 *      <br>
 *     This enum contains all the display resolutions that are supported by the mod.
 *     <br> <br>
 *     The enum also contains a static variable that represents the current resolution.
 *     <br> <br>
 *     The current resolution can be changed by calling {@link #changeResolution(DisplayResolution)}.
 * </p>
 */
@Getter
public enum DisplayResolution {

    UNRECOGNIZED(-1,0,0),
    RES_1024x728(0,1024, 728),
    RES_1280x720(1,1280, 720),
    RES_1366x768(2,1366, 768),
    RES_1600x900(3,1600, 900),
    RES_1920x1080(4,1920, 1080),
    RES_2560x1080(5,2560, 1080);


    private static DisplayResolution CURRENT_RESOLUTION
            = DisplayResolution.UNRECOGNIZED;
    private final int index;
    private final int width;
    private final int height;

    DisplayResolution(int index, int width, int height) {
        this.index = index;
        this.width = width;
        this.height = height;
    }

    @NotNull
    public static DisplayResolution from(int width, int height) {
        for (DisplayResolution value : values()) {
            if (value.width == width && value.height == height) {
                return value;
            }
        }
        return DisplayResolution.UNRECOGNIZED;
    }
    @NotNull
    public static DisplayResolution from(int index) {
        for (DisplayResolution value : values()) {
            if (value.index == index) {
                return value;
            }
        }
        return DisplayResolution.UNRECOGNIZED;
    }


    @NotNull
    public static DisplayResolution getCurrentResolution() {
        return CURRENT_RESOLUTION;
    }
    public static void changeResolution(@NotNull DisplayResolution newResolution) {
        if (Display.isResizable()) {
            Display.setResizable(false);
        }
        if (newResolution == DisplayResolution.UNRECOGNIZED) {
            AtumAPI.getInstance().getCoreMod().getLogger().warn("Tried to change resolution to Unrecognized!");
            return;
        }
        CURRENT_RESOLUTION = newResolution;
        int newWidth = newResolution.getWidth();
        int newHeight = newResolution.getHeight();
        if (newWidth == Display.getWidth() && newHeight == Display.getHeight()) {
            return;
        }
        try {
            Display.setDisplayMode(new DisplayMode(newWidth, newHeight));
            Display.setResizable(false);

            //should not be null anyway
            LoadableConfig settings = AtumAPI.getInstance().getCoreMod().getConfigManager()
                    .getConfig("settings");
            settings.set("resolution", newResolution.index);
            settings.save();

        } catch (Exception e) {
            AtumAPI.getInstance().getCoreMod().getLogger().error("Failed to change resolution to " + newWidth + "x" + newHeight, e);
        }
    }
    public static void changeResolution(int index) {
        changeResolution(from(index));
    }



}
