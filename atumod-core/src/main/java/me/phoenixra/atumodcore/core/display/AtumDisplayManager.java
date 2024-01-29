package me.phoenixra.atumodcore.core.display;

import lombok.Getter;
import me.phoenixra.atumodcore.api.AtumAPI;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.display.*;
import me.phoenixra.atumodcore.api.display.actions.DisplayActionRegistry;
import me.phoenixra.atumodcore.api.display.impl.BaseRenderer;
import me.phoenixra.atumodcore.api.display.impl.BaseScreen;
import me.phoenixra.atumodcore.api.display.misc.DisplayResolution;
import me.phoenixra.atumodcore.api.display.triggers.DisplayTriggerRegistry;
import me.phoenixra.atumodcore.api.service.AtumModService;
import me.phoenixra.atumodcore.api.utils.RenderUtils;
import me.phoenixra.atumodcore.core.display.elements.canvas.DefaultCanvas;
import me.phoenixra.atumodcore.core.display.misc.GuiOptionsExtended;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiOptions;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.jetbrains.annotations.NotNull;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class AtumDisplayManager implements DisplayManager, AtumModService {
    @Getter
    private final AtumMod atumMod;
    @Getter
    private DisplayElementRegistry elementRegistry;
    @Getter
    private DisplayActionRegistry actionRegistry;
    @Getter
    private DisplayTriggerRegistry triggerRegistry;

    private Map<String, DisplayCanvas> enabledCanvases;

    private DisplayRenderer displayRenderer;

    private boolean initResolution;

    public AtumDisplayManager(AtumMod atumMod) {
        this.atumMod = atumMod;
        atumMod.provideModService(this);
    }

    @Override
    public void handleFmlEvent(@NotNull FMLEvent event) {
        if(event instanceof FMLPreInitializationEvent){
            this.elementRegistry = new AtumDisplayElementRegistry(atumMod);
            this.actionRegistry = new AtumDisplayActionRegistry(atumMod);
            this.triggerRegistry = new AtumDisplayTriggerRegistry(atumMod);

            enabledCanvases = new ConcurrentHashMap<>();

            DisplayCanvas canvasHUD = new DefaultCanvas(
                    atumMod,
                    null
            );
            canvasHUD.getOriginX().setDefaultValue(0);
            canvasHUD.getOriginY().setDefaultValue(0);
            canvasHUD.getOriginWidth().setDefaultValue(1920);
            canvasHUD.getOriginHeight().setDefaultValue(1080);
            displayRenderer = new BaseRenderer(
                    atumMod,
                    canvasHUD
            );

            MinecraftForge.EVENT_BUS.register(this);
        }
    }

    @Override
    public void onRemove() {
        //nothing, the mc closed
    }

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event){
        if(event.getGui() instanceof GuiOptions){
            event.setGui(new GuiOptionsExtended(
                    null,
                    Minecraft.getMinecraft().gameSettings
                    )
            );
        }else if(event.getGui() instanceof BaseScreen){
            if(initResolution) return;
            //resolution default
            initResolution = true;

            //config should not be null anyway
            DisplayResolution.changeResolution(AtumAPI.getInstance().getCoreMod().getConfigManager()
                    .getConfig("settings")
                    .getIntOrDefault("resolution",0)
            );
        }
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

            displayRenderer.render(
                    mousePos[0], mousePos[1]
            );
        }
    }

    @Override
    public void setHUDCanvas(@NotNull DisplayCanvas canvas) {
        displayRenderer.closeRenderer();
        displayRenderer = new BaseRenderer(
                atumMod,
                canvas
        );
    }
    @Override
    public @NotNull DisplayCanvas getHUDCanvas() {
        return displayRenderer.getBaseCanvas();
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
    @Override
    public @NotNull String getId() {
        return "display";
    }

}
