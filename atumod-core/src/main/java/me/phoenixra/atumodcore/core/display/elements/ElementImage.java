package me.phoenixra.atumodcore.core.display.elements;

import me.phoenixra.atumconfig.api.placeholders.context.PlaceholderContext;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumconfig.api.config.Config;
import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.annotations.RegisterDisplayElement;
import me.phoenixra.atumodcore.api.display.impl.BaseElement;
import me.phoenixra.atumodcore.api.display.misc.DisplayResolution;
import me.phoenixra.atumodcore.api.display.misc.resources.BufferTextureResource;
import me.phoenixra.atumodcore.api.misc.AtumColor;
import me.phoenixra.atumodcore.api.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.net.URL;
import java.util.Arrays;

/**
 * Image element.
 * <br> <br>
 * Settings:
 * <ul>
 *     <li>image - image resource location</li>
 *     <li>color - color (RGB)</li>
 *     <li>textureX - texture X</li>
 *     <li>textureY - texture Y</li>
 *     <li>textureWidth - texture width</li>
 *     <li>textureHeight - texture height</li>
 * </ul>
 */
@RegisterDisplayElement(templateId = "image")
public class ElementImage extends BaseElement {
    private Runnable imageBinder;


    private AtumColor color = AtumColor.WHITE;
    private int textureX;
    private int textureY;
    private int textureWidth;
    private int textureHeight;


    public ElementImage(@NotNull AtumMod atumMod,
                        @Nullable DisplayCanvas elementOwner) {
        super(atumMod,elementOwner);

    }

    @Override
    protected void onDraw(DisplayResolution resolution,
                          float scaleFactor, int mouseX, int mouseY) {
        if(imageBinder!=null) {
            imageBinder.run();
        }else{
            //empty image
            Minecraft.getMinecraft().getTextureManager().bindTexture(
                    new ResourceLocation("sss:sss")
            );
        }
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
    public void updateElementVariables(@NotNull Config config) {
        TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
        PlaceholderContext context = PlaceholderContext.of(getElementOwner().getDisplayRenderer());
        String image = config.getString("image");
        if (image.startsWith("file:")){
            File textureFile = new File(image.substring(5));
            try {
                BufferTextureResource bufferTextureResource = new BufferTextureResource(
                        textureFile
                );
                bufferTextureResource.loadTexture();
                imageBinder = () -> textureManager.bindTexture(
                        bufferTextureResource.getResourceLocation()
                );
            }catch (Exception exception){
                getAtumMod().getLogger().error("Something went wrong trying " +
                        "to load texture from file: "+ image.substring(5),exception
                );
            }
        }else if(image.startsWith("web:")){
            try {
                URL url = new URL(image.substring(4));
                BufferTextureResource bufferTextureResource = new BufferTextureResource(
                        url
                );
                bufferTextureResource.loadTexture();
                imageBinder = () -> textureManager.bindTexture(
                        bufferTextureResource.getResourceLocation()
                );
            }catch (Exception exception){
                getAtumMod().getLogger().error("Something went wrong trying " +
                        "to load texture from url: "+ image.substring(4), exception
                );
            }
        }else {
            ResourceLocation imageLocation = new ResourceLocation(image);
            this.imageBinder = () -> textureManager.bindTexture(imageLocation);
        }


        String color = config.getStringOrNull("color");
        if(color!=null){
            this.color = AtumColor.fromHex(color);
        }
        String textureX = config.getStringOrNull("textureX");
        if(textureX!=null){
            this.textureX = (int)  config.getEvaluated(
                    "textureX",
                    context
            );
        }
        String textureY = config.getStringOrNull("textureY");
        if(textureY!=null){
            this.textureY = (int)  config.getEvaluated(
                    "textureY",
                    context
            );
        }
        String textureWidth = config.getStringOrNull("textureWidth");
        if(textureWidth!=null){
            this.textureWidth = (int)  config.getEvaluated(
                    "textureWidth",
                    context
            );
        }
        String textureHeight = config.getStringOrNull("textureHeight");
        if(textureHeight!=null){
            this.textureHeight = (int)  config.getEvaluated(
                    "textureHeight",
                    context
            );
        }

    }

    @Override
    protected BaseElement onClone(BaseElement clone) {
        ElementImage cloneImage = (ElementImage) clone;
        try {
            if (cloneImage.color != null) {
                cloneImage.color = color.clone();
            }
        }catch (CloneNotSupportedException ignored){}
        return cloneImage;
    }
}
