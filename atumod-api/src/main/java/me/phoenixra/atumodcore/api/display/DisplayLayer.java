package me.phoenixra.atumodcore.api.display;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Enum for display layers.
 * <p>Used to determine the rendering order of elements.</p>
 */
public enum DisplayLayer {
    BACKGROUND(0),
    LOW(1),
    MIDDLE(2),
    HIGH(3),
    FOREGROUND(4);
    private final int layer;
    DisplayLayer(int layer){
        this.layer = layer;
    }
    public int getLayerNumber(){
        return layer;
    }

    public static DisplayLayer fromString(String layer, DisplayLayer defaultLayer){
        for(DisplayLayer guiElementLayer : DisplayLayer.values()){
            if(guiElementLayer.name().equalsIgnoreCase(layer)){
                return guiElementLayer;
            }
        }
        return defaultLayer;
    }
    public static DisplayLayer fromString(String layer){
        for(DisplayLayer guiElementLayer : DisplayLayer.values()){
            if(guiElementLayer.name().equalsIgnoreCase(layer)){
                return guiElementLayer;
            }
        }
        throw new IllegalArgumentException("No such layer: " + layer);
    }

    public static DisplayLayer fromInt(int layer){
        for(DisplayLayer guiElementLayer : DisplayLayer.values()){
            if(guiElementLayer.getLayerNumber() == layer){
                return guiElementLayer;
            }
        }
        return layer<0 ? DisplayLayer.BACKGROUND : DisplayLayer.FOREGROUND;
    }

    public static List<DisplayLayer> valuesOrdered(){
        return Lists.newArrayList(BACKGROUND,LOW,MIDDLE,HIGH,FOREGROUND);
    }
    public static List<DisplayLayer> valuesOrderedFromHighest(){
        return Lists.newArrayList(FOREGROUND,HIGH,MIDDLE,LOW,BACKGROUND);
    }
}
