package me.phoenixra.atumodcore.core.display.elements;

import me.phoenixra.atumodcore.api.config.Config;
import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.impl.BaseElement;
import me.phoenixra.atumodcore.api.input.event.InputPressEvent;
import me.phoenixra.atumodcore.api.input.event.InputReleaseEvent;
import me.phoenixra.atumodcore.api.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class DisplayElementButton extends BaseElement {

    private Runnable imageBinder;
    private float[] brightnessDefault = new float[]{1.0f,1.0f,1.0f};
    private float[] brightnessOnHover = new float[0];
    private float[] brightnessOnClick = new float[0];
    private int textureX;
    private int textureY;
    private int textureWidth;
    private int textureHeight;

    private boolean clicked;
    public DisplayElementButton(@NotNull DisplayCanvas elementOwner) {
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
        this.textureX = config.getInt("settings.textureX");
        this.textureY = config.getInt("settings.textureY");
        this.textureWidth = config.getInt("settings.textureWidth");
        this.textureHeight = config.getInt("settings.textureHeight");
    }

    @Override
    protected BaseElement onClone(BaseElement clone) {
        DisplayElementButton cloneImage = (DisplayElementButton) clone;
        return cloneImage;
    }

    @Override
    public void onPress(InputPressEvent event) {
        super.onPress(event);
        clicked = true;
        System.out.println("Pressed");
    }

    @Override
    public void onRelease(InputReleaseEvent event) {
        super.onRelease(event);
        clicked = false;
    }
}
