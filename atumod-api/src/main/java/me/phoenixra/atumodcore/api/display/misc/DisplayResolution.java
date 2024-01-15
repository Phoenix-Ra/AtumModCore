package me.phoenixra.atumodcore.api.display.misc;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.Display;

public enum DisplayResolution {

    UNRECOGNIZED(-1,0,0),
    RES_1024x728(0,1024, 728),
    RES_1280x720(1,1280, 720),
    RES_1366x768(2,1366, 768),
    RES_1600x900(3,1600, 900),
    RES_1920x1080(4,1920, 1080),
    RES_2560x1080(5,2560, 1080);
    @Getter
    private final int index;
    @Getter
    private final int width;
    @Getter
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
    public static DisplayResolution getCurrentResolution() {
        return from(Display.getWidth(), Display.getHeight());
    }



}
