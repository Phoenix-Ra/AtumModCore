package me.phoenixra.atumodcore.api.display.font;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Fonts {
    private static HashMap<String, HashMap<Integer,DisplayFont>> fonts = new HashMap<>();
    private static HashMap<String, String> fontNames = new HashMap<>();

    @Nullable
    public static DisplayFont registerFont(@NotNull String id, int fontSize, @NotNull InputStream fontFile){
        Map<Integer,DisplayFont> map = fonts.get(id);
        String fontName = fontNames.get(id);
        if(map != null && fontName != null){
            if(map.containsKey(fontSize)){
                return map.get(fontSize);
            }
            DisplayFont out = new DisplayFont(new Font(fontName, Font.PLAIN, fontSize));
            map.put(fontSize, out);
            return map.put(fontSize, out);
        }
        System.out.println("Registering font: " + id);
        try (InputStream stream = fontFile) {
            Font font = Font.createFont(
                    0,
                    stream
            );
            fontNames.put(id, font.getFontName());
            if(!GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font)) return null;
            if(!fonts.containsKey(id)) {
                fonts.put(id, new HashMap<>());
            }
            DisplayFont out = new DisplayFont(new Font(font.getFontName(), Font.PLAIN, fontSize));
            fonts.get(id).put(fontSize, out);
            return out;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    @Nullable
    public DisplayFont getFont(@NotNull String id, int fontSize){
        if(!fonts.containsKey(id)) return null;
        return fonts.get(id).get(fontSize);
    }
}
