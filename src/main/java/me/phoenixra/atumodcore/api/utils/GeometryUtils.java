package me.phoenixra.atumodcore.api.utils;

public class GeometryUtils {


    public static boolean isPointInsideRectangle(int posX, int posY, int rectX, int rectY, int rectWidth, int rectHeight){
        return posX >= rectX && posX <= rectX + rectWidth && posY >= rectY && posY <= rectY + rectHeight;
    }
}
