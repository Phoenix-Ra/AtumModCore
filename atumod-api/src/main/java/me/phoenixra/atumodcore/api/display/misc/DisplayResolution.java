package me.phoenixra.atumodcore.api.display.misc;

import lombok.Getter;
import me.phoenixra.atumconfig.api.config.Config;
import me.phoenixra.atumconfig.api.tuples.PairRecord;
import me.phoenixra.atumodcore.api.AtumAPI;
import me.phoenixra.atumconfig.api.config.LoadableConfig;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * TEMPORARILY DISABLED. USED ONLY 1920x1080
 * The display resolution.
 * <p>
 * <br>
 * This enum contains all the display resolutions that are supported by the mod.
 * <br> <br>
 * The enum also contains a static variable that represents the current resolution.
 * <br>
 * </p>
 */
@Getter
public enum DisplayResolution {

    _16x9(0,
            new PairRecord<>(1280, 720),
            new PairRecord<>(1366, 768),
            new PairRecord<>(1600, 900),
            new PairRecord<>(1920, 1080)
    ),
    _16x10(1,
            new PairRecord<>(1280, 800),
            new PairRecord<>(1440, 900),
            new PairRecord<>(1680, 1050),
            new PairRecord<>(1920, 1200),
            new PairRecord<>(2560, 1600)
    ),
    _4x3(2,
            new PairRecord<>(800, 600),
            new PairRecord<>(1024, 768)
    ),

    UNRECOGNIZED(-1);

    private static DisplayResolution CURRENT_RESOLUTION
            = DisplayResolution.UNRECOGNIZED;
    private static boolean init;
    private static List<PairRecord<DisplayResolution, PairRecord<Integer,Integer>>> resolutionsCache;

    private static int usableWidth;
    private static int usableHeight;

    private final int index;
    private final List<PairRecord<Integer, Integer>> resolutionsSupported;


    @SafeVarargs
    DisplayResolution(int index, PairRecord<Integer, Integer>... resolutionsSupported) {
        this.index = index;
        this.resolutionsSupported = new ArrayList<>();
        this.resolutionsSupported.addAll(Arrays.asList(resolutionsSupported));

    }

    @NotNull
    public static DisplayResolution from(int width, int height) {
        for (DisplayResolution value : values()) {
            for (PairRecord<Integer, Integer> entry : value.resolutionsSupported) {
                if (entry.getFirst() == width && entry.getSecond() == height) {
                    return value;
                }
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

    public static void updateResolution() {
        if(usableWidth==0){
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice gd = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gd.getDefaultConfiguration();
            // Get the usable screen size
            Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(gc);
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            usableWidth = screenSize.width - screenInsets.left - screenInsets.right;
            usableHeight = screenSize.height - screenInsets.top - screenInsets.bottom;
            System.out.println("Usable window size: " + usableWidth + "x" + usableHeight);
            if(!init){
                try {
                    init = true;
                    Config config = AtumAPI.getInstance().getCoreMod().getConfigManager()
                            .getConfig("settings");
                    String resolutionRaw = config.getStringOrNull("resolution.name");
                    if(resolutionRaw != null) {
                        DisplayResolution resolution = DisplayResolution.valueOf(
                                config.getString("resolution.name")
                        );
                        Integer index = config.getIntOrNull("resolution.index");
                        if (index != null) {
                            setResolution(resolution, index);
                        }
                        return;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        DisplayResolution resolution = from(Display.getWidth(),Display.getHeight());
        if(resolution==CURRENT_RESOLUTION && CURRENT_RESOLUTION != UNRECOGNIZED) {
            return;
        }
        if(resolution!=UNRECOGNIZED){
            CURRENT_RESOLUTION = resolution;
            return;
        }
        if(Display.isFullscreen()){
            CURRENT_RESOLUTION = _16x9; //just use default in this case (temp solution)
            return;
        }

        int newWidth = 0;
        int newHeight = 0;
        int currentWidth = Display.getWidth();
        int currentHeight = Display.getHeight();
        int difference = 100000; //just a random big value at the start to be replaced on 1st iteration
        for (PairRecord<DisplayResolution,PairRecord<Integer,Integer>> entry : getSupportedResolutions()){
            int width = entry.getSecond().getFirst();
            int height = entry.getSecond().getSecond();
            if(usableWidth<width||usableHeight<height) continue;
            int localDifference = Math.abs(currentWidth-width)
                    +Math.abs(currentHeight-height);
            if(localDifference<difference){
                difference = localDifference;
                newWidth = width;
                newHeight = height;
                resolution = entry.getFirst();
            }
        }
        try {
            Display.setDisplayMode(new DisplayMode(newWidth, newHeight));
            Display.setResizable(false);
            Display.setResizable(true);
            CURRENT_RESOLUTION = resolution;
            Display.update();
            Minecraft.getMinecraft().resize(newWidth,newHeight);
            System.out.println("Changing window to: " + newWidth + "x" + newHeight);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    public static void setResolution(DisplayResolution resolution, int index) {
        if(Display.isFullscreen()) return;
        try {
            PairRecord<Integer,Integer> entry = resolution.resolutionsSupported.get(index);
            if(Display.getWidth()==entry.getFirst()
                    && Display.getHeight() == entry.getSecond()) return;

            Display.setDisplayMode(new DisplayMode(entry.getFirst(), entry.getSecond()));
            Display.setResizable(false);
            Display.setResizable(true);
            CURRENT_RESOLUTION = resolution;
            Display.update();
            Minecraft.getMinecraft().resize(entry.getFirst(),entry.getSecond());

            LoadableConfig config = AtumAPI.getInstance().getCoreMod().getConfigManager()
                    .getConfig("settings");
            config.set("resolution.name", CURRENT_RESOLUTION.name());
            config.set("resolution.index", index);
            config.save();
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    public static void setResolution(int globalIndex) {
        if(Display.isFullscreen()) return;
        try {
            PairRecord<Integer,Integer> entry = getSupportedResolutions().get(globalIndex).getSecond();
            if(Display.getWidth()==entry.getFirst()
                    && Display.getHeight() == entry.getSecond()) return;
            Display.setDisplayMode(new DisplayMode(entry.getFirst(), entry.getSecond()));
            Display.setResizable(false);
            Display.setResizable(true);
            CURRENT_RESOLUTION = getSupportedResolutions().get(globalIndex).getFirst();
            Display.update();
            Minecraft.getMinecraft().resize(entry.getFirst(),entry.getSecond());

            LoadableConfig config = AtumAPI.getInstance().getCoreMod().getConfigManager()
                    .getConfig("settings");
            config.set("resolution.name", CURRENT_RESOLUTION.name());
            config.set("resolution.index", getResolutionIndex(
                    CURRENT_RESOLUTION,
                    entry.getFirst(),entry.getSecond())
            );
            config.save();
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private static int getResolutionIndex(DisplayResolution resolution, int width, int height){
        int index = 0;
        for(PairRecord<Integer,Integer> entry : resolution.getResolutionsSupported()){
            if(width == entry.getFirst() && height == entry.getSecond()){
                return index;
            }
            index++;
        }
        return 0;
    }

    public static List<PairRecord<DisplayResolution, PairRecord<Integer,Integer>>>
            getSupportedResolutions(){
        if(usableWidth==0){
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice gd = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gd.getDefaultConfiguration();
            // Get the usable screen size
            Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(gc);
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            usableWidth = screenSize.width - screenInsets.left - screenInsets.right;
            usableHeight = screenSize.height - screenInsets.top - screenInsets.bottom;
            System.out.println("Usable window size: " + usableWidth + "x" + usableHeight);
        }

        if(resolutionsCache == null){
            resolutionsCache = new ArrayList<>();
            for(DisplayResolution value : values()){
                for (PairRecord<Integer,Integer> entry : value.resolutionsSupported){
                    if(usableWidth<entry.getFirst()||usableHeight<entry.getSecond()) continue;
                    resolutionsCache.add(
                            new PairRecord<>(value, entry)
                    );
                }
            }
        }
        return resolutionsCache;
    }
    public static int getCurrentGlobalResolutionIndex(){
        int index = 0;
        for(PairRecord<DisplayResolution, PairRecord<Integer,Integer>> entry : getSupportedResolutions()){
            if(entry.getSecond().getFirst()==Display.getWidth()
            && entry.getSecond().getSecond()==Display.getHeight()){
                return index;
            }
            index++;
        }
        return 0;
    }

}
