package me.phoenixra.atumodcore.api.display;

import com.google.common.collect.Lists;
import org.jetbrains.annotations.NotNull;

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

    /**
     * Get the layer number.
     *
     * @return The layer number.
     */
    public int getLayerNumber(){
        return layer;
    }

    /**
     * Get the layer from string name.
     *
     * @param layer The layer name.
     * @param defaultLayer The default layer.
     * @return The layer or default layer if not found.
     */
    @NotNull
    public static DisplayLayer fromString(@NotNull String layer,
                                          @NotNull DisplayLayer defaultLayer){
        for(DisplayLayer guiElementLayer : DisplayLayer.values()){
            if(guiElementLayer.name().equalsIgnoreCase(layer)){
                return guiElementLayer;
            }
        }
        return defaultLayer;
    }

    /**
     * Get the layer from string name.
     *
     * @param layer The layer name.
     * @return The layer
     *
     * @throws IllegalArgumentException if no such layer.
     */
    @NotNull
    public static DisplayLayer fromString(@NotNull String layer) throws IllegalArgumentException{
        for(DisplayLayer guiElementLayer : DisplayLayer.values()){
            if(guiElementLayer.name().equalsIgnoreCase(layer)){
                return guiElementLayer;
            }
        }
        throw new IllegalArgumentException("No such layer: " + layer);
    }

    /**
     * Get the layer from int.
     *
     * @param layer The layer number.
     * @return The layer or BACKGROUND if not found.
     */
    @NotNull
    public static DisplayLayer fromInt(int layer){
        for(DisplayLayer guiElementLayer : DisplayLayer.values()){
            if(guiElementLayer.getLayerNumber() == layer){
                return guiElementLayer;
            }
        }
        return layer<0 ? DisplayLayer.BACKGROUND : DisplayLayer.FOREGROUND;
    }

    /**
     * Get the layer list ordered from lowest to highest.
     * @return The layer list.
     */
    @NotNull
    public static List<DisplayLayer> valuesOrdered(){
        return Lists.newArrayList(BACKGROUND,LOW,MIDDLE,HIGH,FOREGROUND);
    }

    /**
     * Get the layer list ordered from highest to lowest.
     * @return The layer list.
     */
    @NotNull
    public static List<DisplayLayer> valuesOrderedFromHighest(){
        return Lists.newArrayList(FOREGROUND,HIGH,MIDDLE,LOW,BACKGROUND);
    }
}
