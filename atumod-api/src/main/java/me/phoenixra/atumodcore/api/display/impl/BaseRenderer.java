package me.phoenixra.atumodcore.api.display.impl;

import lombok.Getter;
import lombok.Setter;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.config.Config;
import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.DisplayRenderer;
import me.phoenixra.atumodcore.api.display.data.DisplayData;
import me.phoenixra.atumodcore.api.display.misc.DisplayResolution;
import me.phoenixra.atumodcore.api.display.triggers.DisplayTrigger;
import me.phoenixra.atumodcore.api.misc.AtumDebugger;
import me.phoenixra.atumodcore.api.network.data.DisplayEventData;
import me.phoenixra.atumodcore.api.placeholders.InjectablePlaceholder;
import me.phoenixra.atumodcore.api.utils.RenderUtils;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BaseRenderer implements DisplayRenderer {
    @Getter
    private final AtumMod atumMod;
    @Getter
    private final int id;
    @Getter
    private DisplayCanvas baseCanvas;

    private AtumDebugger renderDebugger;

    private BaseScreen attachedGuiScreen;

    @Getter
    private DisplayData displayData;

    private List<InjectablePlaceholder> injections = Collections.synchronizedList(new ArrayList<>());


    @Getter
    private List<DisplayTrigger> triggers = Collections.synchronizedList(new ArrayList<>());

    @Getter @Setter
    private boolean sendingPacketsAllowed = false;
    private boolean init = false;

    public BaseRenderer(@NotNull AtumMod atumMod,
                        @NotNull DisplayCanvas baseCanvas,
                        @Nullable BaseScreen attachedGuiScreen) {
        this.atumMod = atumMod;
        this.id = atumMod.getDisplayManager().generateRendererId();
        this.baseCanvas = baseCanvas;
        this.attachedGuiScreen = attachedGuiScreen;
        displayData = new BaseDisplayData(this);
        baseCanvas.setDisplayRenderer(this);

    }
    public BaseRenderer(@NotNull AtumMod atumMod, @NotNull DisplayCanvas baseCanvas) {
        this(atumMod,baseCanvas,null);
    }
    @Override
    public void initRenderer() {
        if(init){
            //double check to have only one init
            return;
        }
        renderDebugger =
        new AtumDebugger(atumMod,"testMenu-"+baseCanvas.getId(),"Drawing the menu");
        if(!baseCanvas.isActive()) {
            baseCanvas = atumMod.getDisplayManager()
                    .getElementRegistry().getDrawableCanvas(
                            baseCanvas.getTemplateId()
                    );
        }
        setBaseCanvas(baseCanvas);
        sendDisplayEvent(
                new DisplayEventData(
                        getAtumMod().getModID(),
                        baseCanvas.getId(),
                        getId(),
                        DisplayEventData.EVENT_OPENED
                )
        );
        getAtumMod().getDisplayManager().registerRenderer(
                getId(), this
        );
        init = true;
    }

    @Override
    public void render(int mouseX, int mouseY) {
        if(!init){
            initRenderer();
        }
        baseCanvas.draw(DisplayResolution.getCurrentResolution(), RenderUtils.getScaleFactor(),mouseX,mouseY);
    }


    @Override
    public void reloadRenderer() {
        if(baseCanvas.getTemplateId() == null) return;
        atumMod.getLogger().info("Reloading renderer for canvas: "+baseCanvas.getId());
        baseCanvas.onRemove();
        baseCanvas = atumMod.getDisplayManager()
                .getElementRegistry().getDrawableCanvas(
                        baseCanvas.getTemplateId()
                );
        if(baseCanvas == null){
            atumMod.getLogger().error("Failed to reload renderer for canvas: "+baseCanvas.getId());
            return;
        }
        setBaseCanvas(baseCanvas);
        onReload();
    }

    @Override
    public final void closeRenderer() {
        sendDisplayEvent(
                new DisplayEventData(
                        getAtumMod().getModID(),
                        baseCanvas.getId(),
                        getId(),
                        DisplayEventData.EVENT_CLOSED
                )
        );
        baseCanvas.onRemove();
        onRendererClosed();
        getAtumMod().getDisplayManager().unregisterRenderer(
                getId()
        );
    }

    @Override
    public void setBaseCanvas(@NotNull DisplayCanvas baseCanvas) {
        for(DisplayTrigger trigger : triggers){
            MinecraftForge.EVENT_BUS.unregister(trigger);
        }
        triggers.clear();
        displayData.clearData();

        this.baseCanvas = baseCanvas;
        baseCanvas.setDisplayRenderer(this);
        if(baseCanvas.getSettingsConfig() == null) {
            System.out.println("Settings config is null for canvas: " + baseCanvas.getId());
            return;
        }
        sendingPacketsAllowed = baseCanvas.getSettingsConfig()
                .getBool("allow_sending_packets");
        if(baseCanvas.getSettingsConfig().hasPath("default_data")){
            Config config = baseCanvas.getSettingsConfig().getSubsection("default_data");
            for(String key : config.getKeys(false)){
                getDisplayData().setData(key, config.getString(key));
                getDisplayData().setDefaultData(key, config.getString(key));
            }
        }
        if(baseCanvas.getSettingsConfig().hasPath("triggers")){
            Config config = baseCanvas.getSettingsConfig().getSubsection("triggers");
            for(String key : config.getKeys(false)){
                DisplayTrigger trigger = getAtumMod().getDisplayManager()
                        .getTriggerRegistry().getTemplate(config.getString(key+".template"));
                if(trigger == null){
                    continue;
                }
                trigger = trigger.cloneWithNewVariables(
                        config.getSubsection(key),
                        this
                );
                MinecraftForge.EVENT_BUS.register(trigger);
                triggers.add(trigger);
            }
        }
    }

    @Override
    public void sendDisplayEvent(DisplayEventData displayEventData) {
        if(!isSendingPacketsAllowed()) return;
        getAtumMod().getNetworkManager().sendDisplayEvent(
                displayEventData
        );
    }
    public void onRendererClosed() {
        //override if needed
    }
    public void onReload(){
        //override if needed
    }


    @Override
    public final @Nullable BaseScreen getAttachedGuiScreen() {
        return attachedGuiScreen;
    }


    @Override
    public void addInjectablePlaceholder(@NotNull Iterable<InjectablePlaceholder> placeholders) {
        for (InjectablePlaceholder placeholder : placeholders) {
            if (placeholder == null) {
                continue;
            }
            if (injections.contains(placeholder)) {
                continue;
            }
            injections.add(placeholder);
        }
    }

    @Override
    public void removeInjectablePlaceholder(@NotNull Iterable<InjectablePlaceholder> placeholders) {
        for (InjectablePlaceholder placeholder : placeholders) {
            if (placeholder == null) {
                continue;
            }
            injections.remove(placeholder);
        }
    }

    @Override
    public void clearInjectedPlaceholders() {
        injections.clear();
    }
    @Override
    public @NotNull List<InjectablePlaceholder> getPlaceholderInjections() {
        return injections;
    }

}
