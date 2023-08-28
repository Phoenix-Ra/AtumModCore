package me.phoenixra.atumodcore.api.display.impl;

import lombok.Getter;
import lombok.Setter;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.config.Config;
import me.phoenixra.atumodcore.api.config.variables.ConfigVariable;
import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.DisplayElement;
import me.phoenixra.atumodcore.api.display.DisplayLayer;
import me.phoenixra.atumodcore.api.misc.AtumColor;
import me.phoenixra.atumodcore.api.placeholders.context.PlaceholderContext;
import me.phoenixra.atumodcore.api.utils.RenderUtils;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public abstract class BaseElement implements DisplayElement, Cloneable {
    @Getter
    private String id = UUID.randomUUID().toString();
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
    private boolean fixRatio = false;
    @Getter @Setter
    private boolean active = true;

    @Setter
    private boolean outline;

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
        int[] coords = RenderUtils.fixCoordinates(originX,originY,originWidth,originHeight,fixRatio);
        x = coords[0];
        y = coords[1];
        width = coords[2];
        height = coords[3];
        onDraw(scaleFactor,scaleX,scaleY,mouseX,mouseY);
        if(outline){
            RenderUtils.drawDashedOutline(
                    getX(),
                    getY(),
                    getWidth(),
                    getHeight(),
                    AtumColor.LIME
            );
        }
    }
    protected abstract void onDraw(float scaleFactor, float scaleX, float scaleY, int mouseX, int mouseY);

    @Override
    public void updateVariables(@NotNull HashMap<String, ConfigVariable<?>> variables) {
        ConfigVariable<?> layer = variables.get("layer");
        if(layer != null){
            this.layer = DisplayLayer.valueOf(layer.getValue().toString().toUpperCase());
        }
        ConfigVariable<?> x = variables.get("posX");
        if(x != null){
            this.x = this.originX = Integer.parseInt(x.getValue().toString());
        }
        ConfigVariable<?> y = variables.get("posY");
        if(y != null){
            this.y = this.originY = Integer.parseInt(y.getValue().toString());
        }
        ConfigVariable<?> width = variables.get("width");
        if(width != null){
            this.width = this.originWidth = Integer.parseInt(width.getValue().toString());
        }
        ConfigVariable<?> height = variables.get("height");
        if(height != null){
            this.height = this.originHeight = Integer.parseInt(height.getValue().toString());
        }
        ConfigVariable<?> fixRatio = variables.get("fixRatio");
        if(fixRatio != null){
            this.fixRatio = Boolean.parseBoolean(fixRatio.getValue().toString());
        }
    }

    @Override
    public void updateVariables(@NotNull Config config) {
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

    }
    @Override
    public boolean isHovered(int mouseX, int mouseY) {
        return getElementOwner().getHoveredElement(mouseX, mouseY) == this;
    }

    @Override
    public void onRemove() {
        MinecraftForge.EVENT_BUS.unregister(this);
        //empty to not implement it everywhere
    }

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

    @Override
    public DisplayElement clone() {
        try {
            BaseElement clone = (BaseElement) super.clone();
            clone.id = UUID.randomUUID().toString();
            clone.initialized = false;
            return onClone(clone);
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
    protected abstract BaseElement onClone(BaseElement clone);
}
