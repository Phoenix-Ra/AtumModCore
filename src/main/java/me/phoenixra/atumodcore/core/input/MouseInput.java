package me.phoenixra.atumodcore.core.input;

import me.phoenixra.atumodcore.api.utils.ResourceUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Mouse;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
public class MouseInput {
    private static Cursor RESIZE_VERTICAL_CURSOR;
    private static Cursor RESIZE_HORIZONTAL_CURSOR;

    private static Map<String, Boolean> vanillainput = new HashMap<>();

    private static List<Consumer<MouseData>> listeners = new ArrayList<Consumer<MouseData>>();

    public static void init() {
        RESIZE_VERTICAL_CURSOR = loadCursor(new ResourceLocation("atumodcore", "cursor/vresize.png"), 32, 32, 16, 16);
        RESIZE_HORIZONTAL_CURSOR = loadCursor(new ResourceLocation("atumodcore", "cursor/hresize.png"), 32, 32, 16, 16);

        MinecraftForge.EVENT_BUS.register(new MouseInput());
    }


    public static int getMouseX() {
        ScaledResolution scaledresolution = new ScaledResolution(Minecraft.getMinecraft());
        int i1 = scaledresolution.getScaledWidth();
        int x = Mouse.getX() * i1 / Minecraft.getMinecraft().displayWidth;

      /*  if (useRenderScale) {
            return (int)(x / renderScale);
        } else {
            return x;
        }*/
        return x;
    }

    public static int getMouseY() {
        ScaledResolution scaledresolution = new ScaledResolution(Minecraft.getMinecraft());
        int j1 = scaledresolution.getScaledHeight();
        int y = j1 - Mouse.getY() * j1 / Minecraft.getMinecraft().displayHeight - 1;

        /*if (useRenderScale) {
            return (int)(y / renderScale);
        } else {
            return y;
        }*/
        return y;
    }

    public static void setRenderScale(float scale) {
        /*renderScale = scale;
        useRenderScale = true;*/
    }

    public static void resetRenderScale() {
        //useRenderScale = false;
    }

    public static void setCursor(@NotNull CursorType cursor) {
        try {
            switch (cursor) {
                case DEFAULT:
                    resetCursor();
                    break;
                case VRESIZE:
                    Mouse.setNativeCursor(RESIZE_VERTICAL_CURSOR);
                    break;
                case HRESIZE:
                    Mouse.setNativeCursor(RESIZE_HORIZONTAL_CURSOR);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void resetCursor() {
        try {
            Mouse.setNativeCursor(null);
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
    }

    private static Cursor loadCursor(ResourceLocation r, int width, int height, int xHotspot, int yHotspot) {
        try {
            BufferedImage i = ResourceUtils.getImageResourceAsStream(r);
            if (i.getType() != BufferedImage.TYPE_INT_ARGB) {
                BufferedImage tmp = new BufferedImage(i.getWidth(), i.getHeight(), BufferedImage.TYPE_INT_ARGB);
                tmp.getGraphics().drawImage(i, 0, 0, null);
                i = tmp;
            }
            int[] srcPixels = ((DataBufferInt)i.getRaster().getDataBuffer()).getData();
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(srcPixels.length * 4);
            byteBuffer.order(ByteOrder.nativeOrder());
            IntBuffer intBuffer = byteBuffer.asIntBuffer();
            intBuffer.put(srcPixels);
            intBuffer.position(0);

            return new Cursor(width, height, xHotspot, yHotspot, 1, intBuffer, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static boolean isLeftMouseDown() {
        return false;
    }

    public static boolean isRightMouseDown() {
        return false;
    }
    public static void blockVanillaInput(String category) {
        vanillainput.put(category, true);
    }

    public static void unblockVanillaInput(String category) {
        vanillainput.put(category, false);
    }

    public static boolean isVanillaInputBlocked() {

        return vanillainput.containsValue(true);
    }

    public static void ignoreBlockedVanillaInput(boolean ignore) {
    }
    public static enum CursorType {
        DEFAULT,
        VRESIZE,
        HRESIZE;
    }

    public static void registerMouseListener(Consumer<MouseData> c) {
        listeners.add(c);
    }

    @SubscribeEvent
    public void onMouseClicked(GuiScreenEvent.MouseInputEvent.Pre e) {

        for (Consumer<MouseData> c : listeners) {
            c.accept(new MouseData(getMouseX(), getMouseY(), Mouse.getEventDX(), Mouse.getEventDY(), Mouse.getEventDWheel()));
        }

        /*if (isVanillaInputBlocked()) {
            e.setCanceled(true);
        }*/
    }

    @SubscribeEvent
    public void onScreenInit(GuiScreenEvent.InitGuiEvent.Pre e) {
        //Reset values on screen init

        vanillainput.clear();
    }

    @SubscribeEvent
    public void onTick(ClientTickEvent e) {
        if (Minecraft.getMinecraft().currentScreen == null) {
            vanillainput.clear();
        }
    }

    public static class MouseData {
        public int deltaX;
        public int deltaY;
        public int deltaZ;

        public int mouseX;
        public int mouseY;

        public MouseData(int mouseX, int mouseY, int deltaX, int deltaY, int deltaZ) {
            this.deltaX = deltaX;
            this.deltaY = deltaY;
            this.deltaZ = deltaZ;
            this.mouseX = mouseX;
            this.mouseY = mouseY;
        }
    }
}
