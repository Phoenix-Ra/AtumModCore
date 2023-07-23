package me.phoenixra.atumodcore.misc;

import net.minecraft.client.renderer.GlStateManager;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public class AtumColor {

    private int red;
    private int green;
    private int blue;
    private int alpha;
    public AtumColor(int red, int green, int blue, int alpha){
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }
    public AtumColor(int red, int green, int blue){
        this(red, green, blue, 255);
    }
    public AtumColor(Color color){
        this(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }
    public AtumColor(int color, boolean hasAlpha){
        this(color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF,hasAlpha ? color >> 24 & 0xFF : 255);
    }
    public AtumColor(String hex){
        this(
                Integer.valueOf(hex.substring(0, 2), 16),
                Integer.valueOf(hex.substring(2, 4), 16),
                Integer.valueOf(hex.substring(4, 6), 16),
                hex.length() == 8 ? Integer.valueOf(hex.substring(6, 8), 16) : 255
        );
    }


    public void useColor(){
        GlStateManager.color(
                (float)red/255,
                (float)green/255,
                (float)blue/255,
                (float)alpha/255
        );
    }


    public Color toColor(){
        return new Color(red, green, blue, alpha);
    }


    @Nullable
    public static AtumColor fromHex(String hex) {
        try {
            hex = hex.replace("#", "");
            if (hex.length() == 6) {
                return new AtumColor(new Color(
                        Integer.valueOf(hex.substring(0, 2), 16),
                        Integer.valueOf(hex.substring(2, 4), 16),
                        Integer.valueOf(hex.substring(4, 6), 16),
                        255)
                );
            }
            if (hex.length() == 8) {
                return new AtumColor( new Color(
                        Integer.valueOf(hex.substring(0, 2), 16),
                        Integer.valueOf(hex.substring(2, 4), 16),
                        Integer.valueOf(hex.substring(4, 6), 16),
                        Integer.valueOf(hex.substring(6, 8), 16))
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
