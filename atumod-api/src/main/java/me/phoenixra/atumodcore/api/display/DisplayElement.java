package me.phoenixra.atumodcore.api.display;


import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.config.Config;
import me.phoenixra.atumodcore.api.config.variables.ConfigVariable;
import me.phoenixra.atumodcore.api.input.event.InputPressEvent;
import me.phoenixra.atumodcore.api.input.event.InputReleaseEvent;
import me.phoenixra.atumodcore.api.registry.Registrable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public interface DisplayElement extends Cloneable{

    @NotNull String getId();
    @Nullable String getConfigKey();

    int getX();
    int getY();
    int getWidth();
    int getHeight();

    int getOriginX();
    int getOriginY();
    int getOriginWidth();
    int getOriginHeight();

    void setOriginX(int x);
    void setOriginY(int y);
    void setOriginWidth(int width);
    void setOriginHeight(int height);

    void draw(float scaleFactor, float scaleX, float scaleY, int mouseX, int mouseY);

    boolean isHovered(int mouseX, int mouseY);
    void onRemove();

    @NotNull DisplayLayer getLayer();

    void setElementOwner(@NotNull DisplayCanvas elementOwner);
    @NotNull DisplayCanvas getElementOwner();

    void updateVariables(@NotNull Config config, @Nullable String configKey);

    default boolean isCoordinateInElement(int mouseX, int mouseY){
        return mouseX >= getX() &&
                mouseX <= getX() + getWidth()
                && mouseY >= getY()
                && mouseY <= getY() + getHeight();
    }
    default boolean isElementInsideThis(@NotNull DisplayElement element){
        return  element.getX() <= getX() &&
                element.getX() + element.getWidth() >= getX() + getWidth()
                && element.getY() <= getY()
                &&  element.getY() + element.getHeight() >= getY() + getHeight();
    }

    boolean isActive();
    void setActive(boolean active);

    void setOutline(boolean outline);

    DisplayElement clone();


    AtumMod getAtumMod();

}
