package me.phoenixra.atumodcore.core.display.elements;

import me.phoenixra.atumodcore.api.config.Config;
import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.triggers.TriggerData;
import me.phoenixra.atumodcore.api.display.actions.DisplayAction;
import me.phoenixra.atumodcore.api.display.impl.BaseElement;
import me.phoenixra.atumodcore.api.input.event.InputPressEvent;
import me.phoenixra.atumodcore.api.input.event.InputReleaseEvent;
import me.phoenixra.atumodcore.api.placeholders.context.PlaceholderContext;
import me.phoenixra.atumodcore.api.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class ElementButton extends BaseElement {

    private Runnable imageBinder;
    private float[] brightnessDefault = new float[]{1.0f,1.0f,1.0f};
    private float[] brightnessOnHover = new float[0];
    private float[] brightnessOnClick = new float[0];
    private int textureX;
    private int textureY;
    private int textureWidth;
    private int textureHeight;
    private boolean clicked;
    public ElementButton(@NotNull DisplayCanvas elementOwner) {
        super(elementOwner);
    }

    @Override
    public void draw(float scaleFactor, float scaleX, float scaleY, int mouseX, int mouseY) {
        super.draw(scaleFactor, scaleX, scaleY, mouseX, mouseY);
        imageBinder.run();
        if(clicked && brightnessOnClick.length==3){
            GlStateManager.color(
                    brightnessOnClick[0],
                    brightnessOnClick[1],
                    brightnessOnClick[2],
                    1.0f
            );
        }else if(isHovered(mouseX,mouseY) && brightnessOnHover.length == 3){
            GlStateManager.color(
                    brightnessOnHover[0],
                    brightnessOnHover[1],
                    brightnessOnHover[2],
                    1.0f
            );
        }else {
            GlStateManager.color(
                    brightnessDefault[0],
                    brightnessDefault[1],
                    brightnessDefault[2],
                    1.0f
            );
        }
        if(textureHeight==0||textureWidth==0){
            RenderUtils.drawCompleteImage(
                    getX(),
                    getY(),
                    getWidth(),
                    getHeight()
            );
        }else {
            RenderUtils.drawPartialImage(
                    getX(),
                    getY(),
                    getWidth(),
                    getHeight(),
                    textureX,
                    textureY,
                    textureWidth,
                    textureHeight
            );
        }
    }


    @Override
    public void updateVariables(@NotNull Config config) {
        super.updateVariables(config);
        TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
        String image = config.getStringOrNull("settings.image");
        if(image!=null){
            ResourceLocation imageLocation = new ResourceLocation(image);
            this.imageBinder = ()-> textureManager.bindTexture(imageLocation);
        }
        String brightness = config.getStringOrNull("settings.brightness");
        if(brightness!=null){
            this.brightnessDefault = new float[]{
                        Float.parseFloat(brightness.split(";")[0]),
                        Float.parseFloat(brightness.split(";")[1]),
                        Float.parseFloat(brightness.split(";")[2])
            };
        }
        brightness = config.getStringOrNull("settings.brightness-onHover");
        if(brightness!=null){
            this.brightnessOnHover = new float[]{
                    Float.parseFloat(brightness.split(";")[0]),
                    Float.parseFloat(brightness.split(";")[1]),
                    Float.parseFloat(brightness.split(";")[2])
            };
        }
        brightness = config.getStringOrNull("settings.brightness-onClick");
        if(brightness!=null){
            this.brightnessOnClick = new float[]{
                    Float.parseFloat(brightness.split(";")[0]),
                    Float.parseFloat(brightness.split(";")[1]),
                    Float.parseFloat(brightness.split(";")[2])
            };
        }
        String textureX = config.getStringOrNull("settings.textureX");
        if(textureX!=null){
            this.textureX = (int) config.getAtumMod().getApi().evaluate(
                    config.getAtumMod(),
                    textureX,
                    PlaceholderContext.of(config)
            );
        }
        String textureY = config.getStringOrNull("settings.textureY");
        if(textureY!=null){
            this.textureY = (int) config.getAtumMod().getApi().evaluate(
                    config.getAtumMod(),
                    textureY,
                    PlaceholderContext.of(config)
            );
        }
        String textureWidth = config.getStringOrNull("settings.textureWidth");
        if(textureWidth!=null){
            this.textureWidth = (int) config.getAtumMod().getApi().evaluate(
                    config.getAtumMod(),
                    textureWidth,
                    PlaceholderContext.of(config)
            );
        }
        String textureHeight = config.getStringOrNull("settings.textureHeight");
        if(textureHeight!=null){
            this.textureHeight = (int) config.getAtumMod().getApi().evaluate(
                    config.getAtumMod(),
                    textureHeight,
                    PlaceholderContext.of(config)
            );
        }
    }

    @Override
    protected BaseElement onClone(BaseElement clone) {
        ElementButton cloneImage = (ElementButton) clone;
        return cloneImage;
    }
}
