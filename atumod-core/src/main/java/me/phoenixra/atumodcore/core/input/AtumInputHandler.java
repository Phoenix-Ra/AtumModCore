package me.phoenixra.atumodcore.core.input;

import lombok.Getter;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.input.CursorType;
import me.phoenixra.atumodcore.api.input.InputHandler;
import me.phoenixra.atumodcore.api.input.InputType;
import me.phoenixra.atumodcore.api.input.event.InputPressEvent;
import me.phoenixra.atumodcore.api.input.event.InputReleaseEvent;
import me.phoenixra.atumodcore.api.tuples.PairRecord;
import me.phoenixra.atumodcore.api.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

public class AtumInputHandler implements InputHandler {
    private final AtumMod atumMod;
    private static Cursor RESIZE_VERTICAL_CURSOR;
    private static Cursor RESIZE_HORIZONTAL_CURSOR;
    private static Cursor RESIZE_TOP_LEFT_CURSOR;
    private static Cursor RESIZE_TOP_RIGHT_CURSOR;
    private static Cursor POSITIONING_CURSOR;

    private HashMap<String,Consumer<InputPressEvent>> onPressListeners = new HashMap<>();
    private HashMap<String,Consumer<InputReleaseEvent>> onReleaseListeners = new HashMap<>();

    @Getter
    private boolean inputBlocked = false;


    private PairRecord<Integer, Integer> lastMousePos = new PairRecord<>(0, 0);
    private int mouseXCache, mouseYCache;
    private int scaleFactorCache;

    public AtumInputHandler(AtumMod atumMod) {
        this.atumMod = atumMod;
        init();
    }

    private void init() {
        RESIZE_VERTICAL_CURSOR = loadCursor(getClass().getResourceAsStream("/assets/atumodcore/cursor/vresize.png"), 32, 32, 16, 16);
        RESIZE_HORIZONTAL_CURSOR = loadCursor(getClass().getResourceAsStream("/assets/atumodcore/cursor/hresize.png"), 32, 32, 16, 16);
        //WHY top_left and top_right are inverted?
        // There is a weird issue with that, somehow the buffer
        // see the image top right as top left and vice versa.
        //So, just fixed it this way for now.
        RESIZE_TOP_LEFT_CURSOR = loadCursor(getClass().getResourceAsStream("/assets/atumodcore/cursor/resize_top_right.png"), 32, 32, 16, 16);
        RESIZE_TOP_RIGHT_CURSOR = loadCursor(getClass().getResourceAsStream("/assets/atumodcore/cursor/resize_top_left.png"), 32, 32, 16, 16);
        POSITIONING_CURSOR = loadCursor(getClass().getResourceAsStream("/assets/atumodcore/cursor/positioning.png"), 32, 32, 16, 16);

        MinecraftForge.EVENT_BUS.register(this);
    }




    private Cursor loadCursor(InputStream resource, int width, int height, int xHotspot, int yHotspot) {
        try {
            BufferedImage i = TextureUtil.readBufferedImage(resource);
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
    private void resetCursor() {
        try {
            Mouse.setNativeCursor(null);
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
    }
    @SubscribeEvent
    public void onKeyPressPost(GuiScreenEvent.KeyboardInputEvent.Post e) {
        int keycode = Keyboard.getEventKey();
        char typedChar = Keyboard.getEventCharacter();

        if(Keyboard.getEventKeyState()){
            callOnPressListeners(new InputPressEvent(InputType.KEYBOARD_KEY, keycode, typedChar));
        }else{
            callOnReleaseListeners(new InputReleaseEvent(InputType.KEYBOARD_KEY, keycode, typedChar));
        }

        if (inputBlocked) {
            e.setCanceled(true);
        }
    }
    @SubscribeEvent
    public void onMouseClicked(GuiScreenEvent.MouseInputEvent.Pre e) {
        int i = Mouse.getEventButton();
        //update cached scale factor
        RenderUtils.getScaleFactor();
        PairRecord<Integer,Integer> pos = getMousePosition();

        if(Mouse.getEventButtonState()){
            if(i == 0){
                callOnPressListeners(
                        new InputPressEvent(
                                InputType.MOUSE_LEFT,
                                pos.getFirst(),
                                pos.getSecond(),
                                Mouse.getEventDX(),
                                Mouse.getEventDY(),
                                Mouse.getEventDWheel()
                        )
                );
            } else if (i == 1) {
                callOnPressListeners(
                        new InputPressEvent(
                                InputType.MOUSE_RIGHT,
                                pos.getFirst(),
                                pos.getSecond(),
                                Mouse.getEventDX(),
                                Mouse.getEventDY(),
                                Mouse.getEventDWheel()
                        )
                );
            }else if (i == 2) {
                callOnPressListeners(
                        new InputPressEvent(
                                InputType.MOUSE_MIDDLE,
                                pos.getFirst(),
                                pos.getSecond(),
                                Mouse.getEventDX(),
                                Mouse.getEventDY(),
                                Mouse.getEventDWheel()
                        )
                );
            }

        }else{
            if(i == 0){
                callOnReleaseListeners(
                        new InputReleaseEvent(
                                InputType.MOUSE_LEFT,
                                pos.getFirst(),
                                pos.getSecond(),
                                Mouse.getEventDX(),
                                Mouse.getEventDY(),
                                Mouse.getEventDWheel()
                        )
                );
            } else if (i == 1) {
                callOnReleaseListeners(
                        new InputReleaseEvent(
                                InputType.MOUSE_RIGHT,
                                pos.getFirst(),
                                pos.getSecond(),
                                Mouse.getEventDX(),
                                Mouse.getEventDY(),
                                Mouse.getEventDWheel()
                        )
                );
            }else if (i == 2) {
                callOnReleaseListeners(
                        new InputReleaseEvent(
                                InputType.MOUSE_MIDDLE,
                                pos.getFirst(),
                                pos.getSecond(),
                                Mouse.getEventDX(),
                                Mouse.getEventDY(),
                                Mouse.getEventDWheel()
                        )
                );
            }
        }

        if (inputBlocked) {
            e.setCanceled(true);
        }
    }

    private void callOnPressListeners(InputPressEvent e) {
        for (Consumer<InputPressEvent> c : new ArrayList<>(onPressListeners.values())) {
            try {
                c.accept(e);
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }
    private void callOnReleaseListeners(InputReleaseEvent e) {
        for (Consumer<InputReleaseEvent> c : new ArrayList<>(onReleaseListeners.values())) {
            try {
                c.accept(e);
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void setCursorType(@NotNull CursorType cursor) {
        try {
            switch (cursor) {
                case DEFAULT:
                    resetCursor();
                    break;
                case RESIZE_VERTICAL:
                    Mouse.setNativeCursor(RESIZE_VERTICAL_CURSOR);
                    break;
                case RESIZE_HORIZONTAL:
                    Mouse.setNativeCursor(RESIZE_HORIZONTAL_CURSOR);
                    break;
                case POSITIONING:
                    Mouse.setNativeCursor(POSITIONING_CURSOR);
                    break;
                case RESIZE_TOP_LEFT:
                    Mouse.setNativeCursor(RESIZE_TOP_LEFT_CURSOR);
                    break;
                case RESIZE_TOP_RIGHT:
                    Mouse.setNativeCursor(RESIZE_TOP_RIGHT_CURSOR);
                    break;
            }
        }catch (LWJGLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public PairRecord<Integer, Integer> getMousePosition() {
        int newScaleFactor = RenderUtils.getScaleFactor();
        if(mouseXCache == Mouse.getX() && mouseYCache == Mouse.getY() && scaleFactorCache == newScaleFactor){
            return lastMousePos;
        }
        int scaledWidth = Minecraft.getMinecraft().displayWidth;
        int scaledHeight = Minecraft.getMinecraft().displayHeight;
        scaledWidth =  MathHelper.ceil((double) scaledWidth / (double) newScaleFactor);
        scaledHeight =  MathHelper.ceil((double) scaledHeight / (double) newScaleFactor);
        mouseXCache = Mouse.getX();
        mouseYCache = Mouse.getY();
        scaleFactorCache = newScaleFactor;
        lastMousePos = new PairRecord<>(
                mouseXCache * scaledWidth / Minecraft.getMinecraft().displayWidth,
                scaledHeight - mouseYCache * scaledHeight / Minecraft.getMinecraft().displayHeight - 1
        );
        return lastMousePos;
    }

    @Override
    public PairRecord<Integer, Integer> getMouseOriginPosition() {
        return new PairRecord<>(Mouse.getX(), Minecraft.getMinecraft().displayHeight - Mouse.getY());
    }

    @Override
    public void blockInput() {
        inputBlocked = true;
    }

    @Override
    public void unblockInput() {
        inputBlocked = false;
    }

    @Override
    public void addListenerOnPress(String id, Consumer<InputPressEvent> listener) {
        onPressListeners.put(id,listener);
    }
    @Override
    public void addListenerOnRelease(String id, Consumer<InputReleaseEvent> listener) {
        onReleaseListeners.put(id,listener);
    }

    @Override
    public void removeListenerOnPress(String id) {
        onPressListeners.remove(id);
    }
    @Override
    public void removeListenerOnRelease(String id) {
        onReleaseListeners.remove(id);
    }
}
