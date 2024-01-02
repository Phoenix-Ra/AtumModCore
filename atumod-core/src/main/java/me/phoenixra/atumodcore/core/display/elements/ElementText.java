package me.phoenixra.atumodcore.core.display.elements;

import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.config.Config;
import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.font.DisplayFont;
import me.phoenixra.atumodcore.api.display.font.Fonts;
import me.phoenixra.atumodcore.api.display.impl.BaseElement;
import me.phoenixra.atumodcore.api.misc.AtumColor;
import me.phoenixra.atumodcore.api.placeholders.context.PlaceholderContext;
import me.phoenixra.atumodcore.api.utils.StringUtils;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;

public class ElementText extends BaseElement {
    private DisplayFont font;
    private String text = "EMPTY TEXT";

    private int fontSize = 20;


    public ElementText(@NotNull AtumMod atumMod, @NotNull DisplayCanvas elementOwner) {
        super(atumMod, elementOwner);
    }

    @Override
    protected void onDraw(float scaleFactor, float scaleX, float scaleY, int mouseX, int mouseY) {
        String text = StringUtils.formatWithPlaceholders(getAtumMod(),this.text, PlaceholderContext.EMPTY);
        int textWidth =  Minecraft.getMinecraft().fontRenderer.getStringWidth(
                StringUtils.removeColorCodes(text)
        ) - 2;
        int localX = getX();
        if(textWidth>getWidth()){
            localX = getX() - (textWidth - getWidth())/2;
        }
        font.drawString(
                text,
                localX,
                getY(),
                AtumColor.WHITE
        );
    }

    @Override
    public void updateVariables(@NotNull Config config, @Nullable String configKey) {
        super.updateVariables(config, configKey);
        Integer fontSize = config.getIntOrNull("settings.fontSize");
        if(fontSize!=null){
            this.fontSize = fontSize;
        }
        String fontName = config.getStringOrNull("settings.font");
        if(fontName!=null){
            try(InputStream stream = getAtumMod().getClass().getResourceAsStream("/assets/"+getAtumMod().getModID()+"/fonts/"+fontName)) {
                font = Fonts.registerFont(fontName,fontSize,stream);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        String text = config.getStringOrNull("settings.text");
        if(text!=null){
            this.text = text;
        }
        if(getOriginWidth() == 0){
            setOriginWidth(100);
        }
        if(getOriginHeight() == 0){
            setOriginHeight(100);
        }
    }

    @Override
    protected BaseElement onClone(BaseElement clone) {
        ElementText cloneText = (ElementText) clone;
        return cloneText;
    }


}
