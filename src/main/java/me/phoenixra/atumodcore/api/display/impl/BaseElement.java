package me.phoenixra.atumodcore.api.display.impl;

import lombok.Getter;
import lombok.Setter;
import me.phoenixra.atumodcore.api.config.Config;
import me.phoenixra.atumodcore.api.config.variables.ConfigVariable;
import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.DisplayElement;
import me.phoenixra.atumodcore.api.display.DisplayLayer;
import me.phoenixra.atumodcore.api.placeholders.context.PlaceholderContext;
import me.phoenixra.atumodcore.api.tuples.PairRecord;
import me.phoenixra.atumodcore.api.utils.RenderUtils;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

public abstract class BaseElement implements DisplayElement, Cloneable {
    @Getter
    private String id = UUID.randomUUID().toString();
    @Getter
    private DisplayLayer layer;
    private int originX;
    private int originY;
    private int originWidth;
    private int originHeight;

    @Getter
    private int x;
    @Getter
    private int y;
    @Getter
    private int width;
    @Getter
    private int height;

    private boolean fixRatio = false;

    @Getter @Setter
    private DisplayCanvas elementOwner;

    public BaseElement(@NotNull DisplayLayer layer,
                       int x,
                       int y,
                       int width,
                       int height,
                       @NotNull DisplayCanvas elementOwner){
        this.layer = layer;
        this.x = this.originX = x;
        this.y = this.originY = y;
        this.width = this.originWidth = width;
        this.height = this.originHeight = height;

        this.elementOwner = elementOwner;
    }
    public BaseElement(@NotNull DisplayCanvas elementOwner){
        this(DisplayLayer.MIDDLE, 0, 0, 0, 0, elementOwner);
    }


    @Override
    public void draw(float scaleFactor, float scaleX, float scaleY, int mouseX, int mouseY) {
        int[] coords = RenderUtils.fixCoordinates(originX,originY,originWidth,originHeight,fixRatio);
        x = coords[0];
        y = coords[1];
        width = coords[2];
        height = coords[3];
    }

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
        //empty to not implement it everywhere
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof BaseElement){
            BaseElement canvas = (BaseElement) obj;
            return canvas.getId().equals(getId());
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
            return onClone(clone);
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
    protected abstract BaseElement onClone(BaseElement clone);
}
