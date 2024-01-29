package me.phoenixra.atumodcore.api.display;

import me.phoenixra.atumodcore.api.config.Config;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;

public interface DisplayCanvas extends DisplayElement, Cloneable{


    /**
     * Add the element to the canvas.
     * @param element The element to add.
     */
    void addElement(@NotNull DisplayElement element);

    /**
     * Remove the element from the canvas.
     * @param element The element to remove.
     */
    void removeElement(@NotNull DisplayElement element);

    /**
     * Clear all elements from the canvas.
     */
    void clearElements();

    /**
     * Get the elements displayed on the canvas.
     * @return The elements displayed on the canvas.
     */
    @NotNull
    HashSet<DisplayElement> getDisplayedElements();

    /**
     * Get the hovered element.
     * @param mouseX The x coordinate.
     * @param mouseY The y coordinate.
     * @return The hovered element.
     */
    @Nullable DisplayElement getHoveredElement(int mouseX, int mouseY);

    /**
     * Get the element from the coordinates.
     * @param posX The x coordinate.
     * @param posY The y coordinate.
     * @return The element from the coordinates.
     */
    @Nullable DisplayElement getElementFromCoordinates(int posX, int posY);


    /**
     * Get the display renderer.
     * @return The display renderer.
     */
    @Nullable DisplayRenderer getDisplayRenderer();

    /**
     * Set the display renderer.
     * @param displayRenderer The display renderer.
     */
    void setDisplayRenderer(@NotNull DisplayRenderer displayRenderer);


    /**
     * Apply the resolution optimizer to the canvas and all its elements.
     * @param config The config to apply.
     */
    void applyResolutionOptimizerGlobally(@NotNull Config config);


    //@TODO rework the setup state
    boolean isSetupState();
    void setSetupState(boolean setupState);

    /**
     * Reload the canvas.
     * This method reloads the canvas and all its elements.
     * <p>Uses DisplayRenderer</p>
     */
    void reloadCanvas();

}
