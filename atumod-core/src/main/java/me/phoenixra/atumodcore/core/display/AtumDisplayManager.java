package me.phoenixra.atumodcore.core.display;

import lombok.Getter;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.display.*;
import me.phoenixra.atumodcore.api.display.actions.DisplayActionRegistry;
import me.phoenixra.atumodcore.api.utils.RenderUtils;
import me.phoenixra.atumodcore.core.display.elements.canvas.DefaultCanvas;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class AtumDisplayManager implements DisplayManager {
    @Getter
    private final AtumMod atumMod;
    @Getter
    private final DisplayElementRegistry elementRegistry;
    @Getter
    private final DisplayActionRegistry actionRegistry;

    private final Map<String, DisplayCanvas> enabledCanvases;

    private final DisplayCanvas canvasHUD;
    public AtumDisplayManager(AtumMod atumMod) {
        this.atumMod = atumMod;
        this.elementRegistry = new AtumDisplayElementRegistry(atumMod);
        this.actionRegistry = new AtumDisplayActionRegistry(atumMod);

        enabledCanvases = new ConcurrentHashMap<>();
        canvasHUD = new DefaultCanvas(
                atumMod,
                null
        );
        canvasHUD.setOriginX(0);
        canvasHUD.setOriginY(0);
        canvasHUD.setOriginWidth(1920);
        canvasHUD.setOriginHeight(1080);

        MinecraftForge.EVENT_BUS.register(this);

    }


    @SubscribeEvent
    public void onGameOverlayRender(RenderGameOverlayEvent event) {
        RenderGameOverlayEvent.ElementType type = event.getType();
        if (Objects.requireNonNull(type) ==
                RenderGameOverlayEvent.ElementType.ALL) {

            if (event instanceof RenderGameOverlayEvent.Pre) return;
            //to have the correct value cached for scale factor before
            //getting mouse position
            float scaleFactor = RenderUtils.getScaleFactor();

            int[] mousePos = RenderUtils.getMousePos();

            canvasHUD.draw(scaleFactor, 1, 1,
                    mousePos[0], mousePos[1]
            );
        }
    }



    @Override
    public void addElementForHUD(@NotNull DisplayElement element) {
        canvasHUD.addElement(element);

    }
    @Override
    public void removeElementFromHUD(@NotNull DisplayElement element) {
        canvasHUD.removeElement(element);
    }



    @Override
    public DisplayCanvas getEnabledCanvas(@NotNull String id) {
        return enabledCanvases.get(id);
    }

    @Override
    public void registerEnabledCanvas(@NotNull String id, @NotNull DisplayCanvas canvas) {
        enabledCanvases.put(id, canvas);
    }

    @Override
    public void unregisterEnabledCanvas(@NotNull String id) {
        enabledCanvases.remove(id);
    }

}
