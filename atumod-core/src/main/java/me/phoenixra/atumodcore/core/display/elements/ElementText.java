package me.phoenixra.atumodcore.core.display.elements;

import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.config.Config;
import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.font.DisplayFont;
import me.phoenixra.atumodcore.api.display.font.Fonts;
import me.phoenixra.atumodcore.api.display.impl.BaseElement;
import me.phoenixra.atumodcore.api.display.misc.DisplayResolution;
import me.phoenixra.atumodcore.api.misc.AtumColor;
import me.phoenixra.atumodcore.api.placeholders.context.PlaceholderContext;
import me.phoenixra.atumodcore.api.utils.StringUtils;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.util.HashMap;

public class ElementText extends BaseElement {
    private DisplayFont font;
    private String text = "EMPTY TEXT";

    private HashMap<DisplayResolution, DisplayFont> optimizedFont = new HashMap<>();


    public ElementText(@NotNull AtumMod atumMod, @NotNull DisplayCanvas elementOwner) {
        super(atumMod, elementOwner);
    }

    @Override
    protected void onDraw(DisplayResolution resolution, float scaleFactor, int mouseX, int mouseY) {
        String text = StringUtils.formatWithPlaceholders(
                getAtumMod(),
                this.text,
                PlaceholderContext.of(getElementOwner().getDisplayRenderer())
        );
        int textWidth =  Minecraft.getMinecraft().fontRenderer.getStringWidth(
                StringUtils.removeColorCodes(text)
        ) - 2;
        int localX = getX();
        if(textWidth>getWidth()){
            localX = getX() - (textWidth - getWidth())/2;
        }
        DisplayFont font = optimizedFont.getOrDefault(resolution,this.font);
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
    public void applyResolutionOptimizer(@NotNull DisplayResolution resolution, @NotNull Config config) {
        super.applyResolutionOptimizer(resolution, config);
        String fontName = config.getStringOrNull("settings.font");
        if(fontName == null) return;
        config.getIntOrDefault("settings.fontSize",20);
        DisplayFont font1 = null;
        try(InputStream stream = getAtumMod().getClass().getResourceAsStream(
                "/assets/"+getAtumMod().getModID()+"/fonts/"+fontName)) {
            font1 = Fonts.registerFont(
                    fontName,
                    config.getIntOrDefault("settings.fontSize",20),
                    stream
            );
        }catch (Exception e){
            getAtumMod().getLogger().error("Failed to load font: "+fontName+" for resolution: "+resolution.name());
        }

        optimizedFont.put(resolution,
                font1==null? font : font1
        );
    }

    @Override
    protected BaseElement onClone(BaseElement clone) {
        ElementText cloneText = (ElementText) clone;
        return cloneText;
    }


}
