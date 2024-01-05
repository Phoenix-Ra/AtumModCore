package me.phoenixra.atumodcore.api.display.impl;

import lombok.Getter;
import lombok.Setter;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.config.Config;
import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.DisplayElement;
import me.phoenixra.atumodcore.api.display.DisplayLayer;
import me.phoenixra.atumodcore.api.display.actions.ActionData;
import me.phoenixra.atumodcore.api.display.actions.DisplayAction;
import me.phoenixra.atumodcore.api.misc.AtumColor;
import me.phoenixra.atumodcore.api.placeholders.context.PlaceholderContext;
import me.phoenixra.atumodcore.api.utils.RenderUtils;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public abstract class BaseElement implements DisplayElement, Cloneable {
    @Getter
    private String id = UUID.randomUUID().toString();
    @Getter
    private String configKey = null;
    @Getter
    private String templateId = null;
    @Getter
    private final AtumMod atumMod;
    @Getter
    private DisplayLayer layer;

    @Getter @Setter
    private int originX;
    @Getter @Setter
    private int originY;
    @Getter @Setter
    private int originWidth;
    @Getter @Setter
    private int originHeight;

    @Getter
    private int x;
    @Getter
    private int y;
    @Getter
    private int width;
    @Getter
    private int height;

    @Getter
    private int lastMouseX;
    @Getter
    private int lastMouseY;

    @Getter
    private boolean fixRatio = false;
    @Getter
    private boolean active = true;

    @Setter
    private boolean outline_selected;

    protected boolean hasOutline = false;
    private AtumColor outlineColor = AtumColor.WHITE;
    private int outlineSize = 1;

    @Getter @Setter
    private DisplayCanvas elementOwner;

    private boolean initialized;

    public BaseElement(@NotNull AtumMod atumMod,
                       @NotNull DisplayLayer layer,
                       int x,
                       int y,
                       int width,
                       int height,
                       @Nullable DisplayCanvas elementOwner){
        this.atumMod = atumMod;
        this.layer = layer;
        this.x = this.originX = x;
        this.y = this.originY = y;
        this.width = this.originWidth = width;
        this.height = this.originHeight = height;

        this.elementOwner = elementOwner;
    }
    public BaseElement(@NotNull AtumMod atumMod, @NotNull DisplayCanvas elementOwner){
        this(atumMod,DisplayLayer.MIDDLE, 0, 0, 0, 0, elementOwner);
    }


    @Override
    public void draw(float scaleFactor, float scaleX, float scaleY, int mouseX, int mouseY) {
        if(!initialized){
            MinecraftForge.EVENT_BUS.register(this);
            initialized = true;
        }
        lastMouseX = mouseX;
        lastMouseY = mouseY;
        int[] coords = RenderUtils.fixCoordinates(originX,originY,originWidth,originHeight,fixRatio);
        x = coords[0];
        y = coords[1];
        width = coords[2];
        height = coords[3];
        onDraw(scaleFactor,scaleX,scaleY,mouseX,mouseY);
        if(outline_selected){
            RenderUtils.drawDashedOutline(
                    getX(),
                    getY(),
                    getWidth(),
                    getHeight(),
                    AtumColor.LIME
            );
        }else if(hasOutline){
            RenderUtils.drawOutline(
                    getX(),
                    getY(),
                    getWidth(),
                    getHeight(),
                    outlineSize,
                    outlineColor
            );
        }
    }
    protected abstract void onDraw(float scaleFactor, float scaleX, float scaleY, int mouseX, int mouseY);

    @Override
    public void updateVariables(@NotNull Config config, @Nullable String configKey) {
        this.configKey = configKey;
        String layer = config.getStringOrNull("layer");
        if(layer != null){
            this.layer = DisplayLayer.valueOf(layer.toUpperCase());
        }
        String x = config.getStringOrNull("posX");
        if(x != null){
            this.x = this.originX = (int) config.getAtumMod().getApi().evaluate(
                    config.getAtumMod(),
                    x,
                    PlaceholderContext.of(config)
            );
        }
        String y = config.getStringOrNull("posY");
        if(y != null){
            this.y = this.originY = (int) config.getAtumMod().getApi().evaluate(
                    config.getAtumMod(),
                    y,
                    PlaceholderContext.of(config)
            );
        }
        String width = config.getStringOrNull("width");
        if(width != null){
            this.width = this.originWidth = (int) config.getAtumMod().getApi().evaluate(
                    config.getAtumMod(),
                    width,
                    PlaceholderContext.of(config)
            );
        }
        String height = config.getStringOrNull("height");
        if(height != null){
            this.height = this.originHeight = (int) config.getAtumMod().getApi().evaluate(
                    config.getAtumMod(),
                    height,
                    PlaceholderContext.of(config)
            );
        }
        Boolean fixRatio = config.getBoolOrNull("fixRatio");
        if(fixRatio != null){
            this.fixRatio = fixRatio;
        }
        if(config.hasPath("outline.color")){
            this.outlineColor = AtumColor.fromHex(config.getString("outline.color"));
            this.outlineSize = config.getIntOrDefault("outline.size",1);
            this.hasOutline = true;
        }

    }


    @Override
    public void onRemove() {
        MinecraftForge.EVENT_BUS.unregister(this);
        //empty to not implement it everywhere
    }


    @Override
    public void performAction(@NotNull String actionId, @NotNull ActionData actionData) {
        DisplayAction action = getAtumMod().getDisplayManager().getActionRegistry()
                .getActionById(actionId);
        if(action == null) return;
        action.perform(actionData);
    }

    @Override
    public boolean isHovered(int mouseX, int mouseY) {
        return getElementOwner().getHoveredElement(mouseX, mouseY) == this;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
        getElementOwner().getDisplayRenderer().getDisplayData()
                .setElementEnabled(getConfigKey(),active);
    }



    @Override
    public DisplayElement cloneWithNewVariables(@NotNull String id,
                                                @NotNull Config config,
                                                @Nullable String configKey,
                                                @Nullable DisplayCanvas elementOwner) {
        DisplayElement clone = clone();
        ((BaseElement)clone).id = id;
        ((BaseElement)clone).templateId = id;
        if(elementOwner != null) {
            clone.setElementOwner(elementOwner);
        }
        clone.updateVariables(config,configKey);
        return clone;
    }

    @Override
    public DisplayElement cloneWithRandomId() {
        DisplayElement clone = clone();
        ((BaseElement)clone).id = id.split("@")[0]+"@"+UUID.randomUUID().toString();
        return clone;
    }

    @Override
    public DisplayElement clone() {
        try {
            BaseElement clone = (BaseElement) super.clone();
            clone.id = UUID.randomUUID().toString();
            clone.initialized = false;
            clone.active = true;
            return onClone(clone);
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
    protected abstract BaseElement onClone(BaseElement clone);


    @Override
    public boolean equals(Object obj) {
        if(obj instanceof BaseElement){
            BaseElement element = (BaseElement) obj;
            return element.getId().equals(getId());
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
