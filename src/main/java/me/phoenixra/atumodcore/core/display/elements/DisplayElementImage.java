package me.phoenixra.atumodcore.core.display.elements;

import me.phoenixra.atumodcore.api.config.Config;
import me.phoenixra.atumodcore.api.config.variables.ConfigVariable;
import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.misc.AtumColor;
import me.phoenixra.atumodcore.api.display.impl.BaseElement;
import me.phoenixra.atumodcore.api.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class DisplayElementImage extends BaseElement {
    private Runnable imageBinder;
    private AtumColor color = AtumColor.WHITE;
    private int textureX;
    private int textureY;
    private int textureWidth;
    private int textureHeight;

    public DisplayElementImage(@NotNull DisplayCanvas elementOwner) {
        super(elementOwner);

    }

    @Override
    public void draw(float scaleFactor, float scaleX, float scaleY, int mouseX, int mouseY) {
        super.draw(scaleFactor, scaleX, scaleY, mouseX, mouseY);
        imageBinder.run();
        color.useColor();
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
    public void updateVariables(@NotNull HashMap<String, ConfigVariable<?>> variables) {
        super.updateVariables(variables);
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
        String color = config.getStringOrNull("settings.color");
        if(color!=null){
            this.color = AtumColor.fromHex(color);
        }
        this.textureX = config.getInt("settings.textureX");
        this.textureY = config.getInt("settings.textureY");
        this.textureWidth = config.getInt("settings.textureWidth");
        this.textureHeight = config.getInt("settings.textureHeight");
    }

    @Override
    protected BaseElement onClone(BaseElement clone) {
        DisplayElementImage cloneImage = (DisplayElementImage) clone;
        if(cloneImage.color!=null) {
            cloneImage.color = new AtumColor(cloneImage.color.getRed(), cloneImage.color.getGreen(), cloneImage.color.getBlue(), cloneImage.color.getAlpha());
        }
        return cloneImage;
    }

}
