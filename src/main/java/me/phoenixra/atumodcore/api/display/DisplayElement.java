package me.phoenixra.atumodcore.api.display;


import me.phoenixra.atumodcore.api.config.Config;
import me.phoenixra.atumodcore.api.config.variables.ConfigVariable;
import me.phoenixra.atumodcore.api.registry.Registrable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public interface DisplayElement {

    String getId();
    int getX();
    int getY();
    int getWidth();
    int getHeight();

    void draw(float scaleFactor, float scaleX, float scaleY, int mouseX, int mouseY);

    boolean isHovered(int mouseX, int mouseY);

    @NotNull DisplayLayer getLayer();

    void setElementOwner(@NotNull DisplayCanvas elementOwner);
    @NotNull DisplayCanvas getElementOwner();

    void updateVariables(@NotNull HashMap<String, ConfigVariable<?>> variables);
    void updateVariables(@NotNull Config config);

    default boolean isCoordinateInElement(int mouseX, int mouseY){
        return mouseX >= getX() &&
                mouseX < getX() + getWidth()
                && mouseY >= getY()
                && mouseY < getY() + getHeight();
    }
    default boolean isElementInsideThis(@NotNull DisplayElement element){
        return  element.getX() >= getX() &&
                element.getX() + element.getWidth() <= getX() + getWidth()
                && element.getY() >= getY()
                &&  element.getY() + element.getHeight() <= getY() + getHeight();
    }

}
