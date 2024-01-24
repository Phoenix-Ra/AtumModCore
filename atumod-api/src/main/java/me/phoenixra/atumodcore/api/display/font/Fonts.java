package me.phoenixra.atumodcore.api.display.font;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.io.InputStream;
import java.util.HashMap;

public class Fonts {
    private static HashMap<String, DisplayFont> fonts = new HashMap<>();
    private static HashMap<String, String> fontNames = new HashMap<>();

    @Nullable
    public static DisplayFont registerFont(@NotNull String id, int fontSize, @NotNull InputStream fontFile){
        DisplayFont displayFont = fonts.get(id);
        String fontName = fontNames.get(id);
        if(displayFont != null && fontName != null){
            if(displayFont.isLoadedSize(fontSize)){
                return displayFont;
            }
            displayFont.loadFontSize(fontSize);
            return displayFont;
        }
        System.out.println("Registering font: " + id);
        try (InputStream stream = fontFile) {
            Font font = Font.createFont(
                    0,
                    stream
            );
            fontNames.put(id, font.getFontName());
            if(!GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font)) return null;

            DisplayFont out = new DisplayFont(new Font(font.getFontName(), Font.PLAIN, fontSize));
            fonts.put(id, out);
            return out;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    @Nullable
    public DisplayFont getFont(@NotNull String id){
        if(!fonts.containsKey(id)) return null;
        return fonts.get(id);
    }
}
