package me.phoenixra.atumodcore.core.display.elements;

import me.phoenixra.atumodcore.api.config.Config;
import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.misc.AtumColor;
import me.phoenixra.atumodcore.api.display.font.DisplayFont;
import me.phoenixra.atumodcore.api.display.font.Fonts;
import me.phoenixra.atumodcore.api.display.impl.BaseElement;
import me.phoenixra.atumodcore.api.utils.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;

public class ElementText extends BaseElement {
    private DisplayFont font;
    private String text = "EMPTY TEXT";

    private int fontSize = 20;


    public ElementText(@NotNull DisplayCanvas elementOwner) {
        super(elementOwner);
    }

    @Override
    public void draw(float scaleFactor, float scaleX, float scaleY, int mouseX, int mouseY) {
        super.draw(scaleFactor, scaleX, scaleY, mouseX, mouseY);
        font.drawString(
                StringUtils.format(text),
                getX(),
                getY(),
                AtumColor.WHITE
        );
    }

    @Override
    public void updateVariables(@NotNull Config config) {
        super.updateVariables(config);
        Integer fontSize = config.getIntOrNull("settings.fontSize");
        if(fontSize!=null){
            this.fontSize = fontSize;
        }
        String fontName = config.getStringOrNull("settings.font");
        if(fontName!=null){
            try(InputStream stream = getClass().getResourceAsStream("/assets/atumodcore/fonts/"+fontName)) {
                font = Fonts.registerFont(fontName,fontSize,stream);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        String text = config.getStringOrNull("settings.text");
        if(text!=null){
            this.text = text;
        }
    }

    @Override
    protected BaseElement onClone(BaseElement clone) {
        ElementText cloneText = (ElementText) clone;
        return cloneText;
    }


}
