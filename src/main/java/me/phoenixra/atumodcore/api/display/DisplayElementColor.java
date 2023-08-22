package me.phoenixra.atumodcore.api.display;

import lombok.Getter;
import net.minecraft.client.renderer.GlStateManager;

public class DisplayElementColor {
    public static final DisplayElementColor WHITE = new DisplayElementColor(1,1,1,1);
    public static final DisplayElementColor BLACK = new DisplayElementColor(0,0,0,1);
    public static final DisplayElementColor RED = new DisplayElementColor(1,0,0,1);
    public static final DisplayElementColor GREEN = new DisplayElementColor(0,1,0,1);
    public static final DisplayElementColor BLUE = new DisplayElementColor(0,0,1,1);
    public static final DisplayElementColor YELLOW = new DisplayElementColor(1,1,0,1);
    public static final DisplayElementColor CYAN = new DisplayElementColor(0,1,1,1);
    public static final DisplayElementColor MAGENTA = new DisplayElementColor(1,0,1,1);
    public static final DisplayElementColor GRAY = new DisplayElementColor(0.5f,0.5f,0.5f,1);
    public static final DisplayElementColor DARK_GRAY = new DisplayElementColor(0.25f,0.25f,0.25f,1);
    public static final DisplayElementColor LIGHT_GRAY = new DisplayElementColor(0.75f,0.75f,0.75f,1);
    public static final DisplayElementColor ORANGE = new DisplayElementColor(1,0.5f,0,1);
    public static final DisplayElementColor PINK = new DisplayElementColor(1,0.68f,0.68f,1);
    public static final DisplayElementColor PURPLE = new DisplayElementColor(0.5f,0,0.5f,1);
    public static final DisplayElementColor BROWN = new DisplayElementColor(0.5f,0.25f,0,1);

    @Getter
    private float red;
    @Getter
    private float green;
    @Getter
    private float blue;
    @Getter
    private float alpha;

    public DisplayElementColor(float red, float green, float blue, float alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }
    public DisplayElementColor(float red, float green, float blue) {
        this(red, green, blue, 1);
    }


    public int toInt() {
        return ((int) (red * 255) << 16) | ((int) (green * 255) << 8) | (int) (blue * 255) | ((int) (alpha * 255) << 24);
    }
    public void useColor(){
        GlStateManager.color(red, green, blue, alpha);
    }

    //from string hex rgba
    public static DisplayElementColor fromHex(String hex, boolean hasAlpha) {
        return from((int)Long.parseLong(hex.substring(1), 16), hasAlpha);
    }

    public static DisplayElementColor from(float red, float green, float blue, float alpha) {
        return new DisplayElementColor(
                red / 255,
                green / 255,
                blue / 255,
                alpha / 255
        );
    }
    public static DisplayElementColor from(float red, float green, float blue) {
        return new DisplayElementColor(
                red / 255,
                green / 255,
                blue / 255
        );
    }
    //from int rgba where a >0 <=1
    public static DisplayElementColor from(int color, boolean hasAlpha) {
        if(hasAlpha){
            return new DisplayElementColor(
                    (color >> 16 & 0xFF) / 255f,
                    (color >> 8 & 0xFF) / 255f,
                    (color & 0xFF) / 255f
            );
        }
        return new DisplayElementColor(
                (color >> 16 & 0xFF) / 255f,
                (color >> 8 & 0xFF) / 255f,
                (color & 0xFF) / 255f,
                (color >> 24 & 0xFF) / 255f
        );
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof DisplayElementColor){
            DisplayElementColor color = (DisplayElementColor) obj;
            return color.red == red && color.green == green && color.blue == blue && color.alpha == alpha;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return toInt();
    }

    @Override
    public String toString() {
        return "DisplayElementColor{" +
                "red=" + red +
                ", green=" + green +
                ", blue=" + blue +
                ", alpha=" + alpha +
                '}';
    }
}
