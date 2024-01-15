package me.phoenixra.atumodcore.api.display;


import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.config.Config;
import me.phoenixra.atumodcore.api.config.variables.ConfigVariable;
import me.phoenixra.atumodcore.api.display.actions.ActionArgs;
import me.phoenixra.atumodcore.api.display.actions.ActionData;
import me.phoenixra.atumodcore.api.display.misc.DisplayResolution;
import me.phoenixra.atumodcore.api.input.event.InputPressEvent;
import me.phoenixra.atumodcore.api.input.event.InputReleaseEvent;
import me.phoenixra.atumodcore.api.registry.Registrable;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.input.Mouse;

import java.util.HashMap;

public interface DisplayElement extends Cloneable{

    @NotNull String getId();
    @Nullable String getTemplateId();
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

    int getLastMouseX();
    int getLastMouseY();

    void applyResolutionOptimizer(@NotNull DisplayResolution resolution,
                                 @NotNull Config config);
    void draw(DisplayResolution resolution, float scaleFactor, int mouseX, int mouseY);

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

    void setOutline_selected(boolean outline_selected);


    void performAction(@NotNull String actionId,
                       @NotNull ActionData actionData);
    default void performAction(@NotNull String actionId,
                          @NotNull String args){
        performAction(actionId,
                new ActionData(
                        getAtumMod(),
                        this,
                        null,
                        getLastMouseX(),
                        getLastMouseY(),
                        new ActionArgs(args)
                )
        );
    }


    Config getSettingsConfig();
    DisplayElement cloneWithNewVariables(@NotNull String id,
                                         @NotNull Config config,
                                         @Nullable String configKey,
                                         @Nullable DisplayCanvas elementOwner);
    /**
     * Clone the element with a new id
     * The new id gonna have the following pattern:
     * "[configName]@[randomUUID]"
     * So, you can recognize the specific element splitting the id with "@"
     * @return The cloned element
     */
    DisplayElement cloneWithRandomId();
    DisplayElement clone();


    AtumMod getAtumMod();

}
