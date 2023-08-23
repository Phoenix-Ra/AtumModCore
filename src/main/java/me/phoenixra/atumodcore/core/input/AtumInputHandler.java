package me.phoenixra.atumodcore.core.input;

import lombok.Getter;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.input.CursorType;
import me.phoenixra.atumodcore.api.input.InputHandler;
import me.phoenixra.atumodcore.api.input.InputType;
import me.phoenixra.atumodcore.api.input.event.InputPressEvent;
import me.phoenixra.atumodcore.api.input.event.InputReleaseEvent;
import me.phoenixra.atumodcore.api.utils.RenderUtils;
import me.phoenixra.atumodcore.api.utils.ResourceUtils;
import me.phoenixra.atumodcore.api.utils.TextureUtils;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.ResourceLocation;
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
import java.util.List;
import java.util.function.Consumer;

public class AtumInputHandler implements InputHandler {
    private final AtumMod atumMod;
    private static Cursor RESIZE_VERTICAL_CURSOR;
    private static Cursor RESIZE_HORIZONTAL_CURSOR;

    private List<Consumer<InputPressEvent>> onPressListeners = new ArrayList<>();
    private List<Consumer<InputReleaseEvent>> onReleaseListeners = new ArrayList<>();

    @Getter
    private boolean inputBlocked = false;

    public AtumInputHandler(AtumMod atumMod) {
        this.atumMod = atumMod;
        init();
    }

    private void init() {
        RESIZE_VERTICAL_CURSOR = loadCursor(getClass().getResourceAsStream("/assets/atumodcore/cursor/vresize.png"), 32, 32, 16, 16);
        RESIZE_HORIZONTAL_CURSOR = loadCursor(getClass().getResourceAsStream("/assets/atumodcore/cursor/hresize.png"), 32, 32, 16, 16);

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
            callOnPressListeners(new InputPressEvent(InputType.KEYBOARD, keycode, typedChar));
        }else{
            callOnReleaseListeners(new InputReleaseEvent(InputType.KEYBOARD, keycode, typedChar));
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
        int[] pos = RenderUtils.fixCoordinates(Mouse.getX(), Mouse.getY());

        if(Mouse.getEventButtonState()){
            if(i == 0){
                callOnPressListeners(
                        new InputPressEvent(
                                InputType.MOUSE_LEFT,
                                pos[0],
                                pos[1],
                                Mouse.getEventDX(),
                                Mouse.getEventDY(),
                                Mouse.getEventDWheel()
                        )
                );
            } else if (i == 1) {
                callOnPressListeners(
                        new InputPressEvent(
                                InputType.MOUSE_RIGHT,
                                pos[0],
                                pos[1],
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
                                pos[0],
                                pos[1],
                                Mouse.getEventDX(),
                                Mouse.getEventDY(),
                                Mouse.getEventDWheel()
                        )
                );
            } else if (i == 1) {
                callOnReleaseListeners(
                        new InputReleaseEvent(
                                InputType.MOUSE_RIGHT,
                                pos[0],
                                pos[1],
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
        for (Consumer<InputPressEvent> c : onPressListeners) {
            c.accept(e);
        }
    }
    private void callOnReleaseListeners(InputReleaseEvent e) {
        for (Consumer<InputReleaseEvent> c : onReleaseListeners) {
            c.accept(e);
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
            }
        }catch (LWJGLException e) {
            e.printStackTrace();
        }
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
    public void addListenerOnPress(Consumer<InputPressEvent> listener) {
        onPressListeners.add(listener);
    }

    @Override
    public void addListenerOnRelease(Consumer<InputReleaseEvent> listener) {
        onReleaseListeners.add(listener);
    }

}
