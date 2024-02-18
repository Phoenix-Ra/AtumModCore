package me.phoenixra.atumodcore.core.display.elements;

import me.phoenixra.atumconfig.api.config.Config;
import me.phoenixra.atumconfig.api.placeholders.context.PlaceholderContext;
import me.phoenixra.atumconfig.api.utils.StringUtils;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.annotations.RegisterDisplayElement;
import me.phoenixra.atumodcore.api.display.annotations.RegisterOptimizedVariable;
import me.phoenixra.atumodcore.api.display.font.DisplayFont;
import me.phoenixra.atumodcore.api.display.font.Fonts;
import me.phoenixra.atumodcore.api.display.impl.BaseElement;
import me.phoenixra.atumodcore.api.display.misc.DisplayResolution;
import me.phoenixra.atumodcore.api.display.misc.variables.OptimizedVariableInt;
import me.phoenixra.atumodcore.api.misc.AtumColor;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.util.HashMap;

/**
 * Text element.
 * <br> <br>
 * Settings:
 * <ul>
 *     <li>font - font name</li>
 *     <li>fontSize - font size</li>
 *     <li>text - text</li>
 * </ul>
 * For font name you can use any font from assets/atumod/fonts
 * <br>
 * For text you can use placeholders and color codes.
 * <br> <br>
 * You can optimize:
 * <ul>
 *     <li>font</li>
 *     <li>fontSize</li>
 * </ul>
 */
@RegisterDisplayElement(templateId = "text")
public class ElementText extends BaseElement {
    private DisplayFont font;
    @RegisterOptimizedVariable
    private OptimizedVariableInt optimizedFontSize;
    private String text = "EMPTY TEXT";

    private HashMap<DisplayResolution, DisplayFont> optimizedFont = new HashMap<>();


    public ElementText(@NotNull AtumMod atumMod,
                       @NotNull DisplayCanvas elementOwner) {
        super(atumMod, elementOwner);
        optimizedFontSize = new OptimizedVariableInt(
                "settings.fontSize",
                25
        );
    }

    @Override
    protected void onDraw(DisplayResolution resolution, float scaleFactor, int mouseX, int mouseY) {
        String text = StringUtils.formatWithPlaceholders(
                getAtumMod(),
                this.text,
                PlaceholderContext.of(getElementOwner().getDisplayRenderer())
        );
        DisplayFont font = optimizedFont.getOrDefault(resolution,this.font);
        int fontSize = (int) (optimizedFontSize.getValue(resolution)/scaleFactor);
        if(!font.isLoadedSize(fontSize)){
            if(!font.isLoadingSize(fontSize)){
                font.loadFontSize(fontSize);
            }
            return;
        }
        int textWidth =  font.getWidth(
                StringUtils.removeColorCodes(text),
                fontSize
        ) - 2;
        int localX = getX();
        if(textWidth>getWidth()){
            localX = getX() - (textWidth - getWidth())/2;
        }
        //centralize by height
        int textHeight = font.getHeight(
                StringUtils.removeColorCodes(text),
                fontSize
        );
        int localY = getY();
        if(textHeight>getHeight()){
            localY = getY() - (textHeight - getHeight())/2;
        }else if(textHeight<getHeight()){
            localY = getY() + (getHeight() - textHeight)/2;
        }

        font.drawString(
                text,
                localX,
                localY,
                AtumColor.WHITE,
                fontSize
        );
    }

    @Override
    public void updateElementVariables(@NotNull Config config) {
         optimizedFontSize.setDefaultValue(
                config.getIntOrDefault("fontSize",25)
        );

        String fontName = config.getStringOrNull("font");
        if(fontName!=null){
            try(InputStream stream = getAtumMod().getClass().getResourceAsStream("/assets/"+getAtumMod().getModID()+"/fonts/"+fontName)) {
                font = Fonts.registerFont(fontName,
                        optimizedFontSize.getDefaultValue(),
                        stream
                );
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        String text = config.getStringOrNull("text");
        if(text!=null){
            this.text = text;
        }
        if(getOriginWidth().getDefaultValue() == 0){
            getOriginWidth().setDefaultValue(100);
        }
        if(getOriginWidth().getDefaultValue() == 0){
            getOriginWidth().setDefaultValue(100);
        }
    }

    @Override
    public void applyResolutionOptimizer(@NotNull DisplayResolution resolution, @NotNull Config config) {
        super.applyResolutionOptimizer(resolution, config);
        String fontName = config.getStringOrNull("settings.font");
        if(fontName == null) return;
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
        cloneText.optimizedFont = new HashMap<>(optimizedFont);
        return cloneText;
    }


}
