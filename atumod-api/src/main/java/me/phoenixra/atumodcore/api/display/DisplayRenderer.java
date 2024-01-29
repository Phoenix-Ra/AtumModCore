package me.phoenixra.atumodcore.api.display;

import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.display.data.DisplayData;
import me.phoenixra.atumodcore.api.display.impl.BaseScreen;
import me.phoenixra.atumodcore.api.placeholders.InjectablePlaceholderList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface DisplayRenderer extends InjectablePlaceholderList {

    /**
     * Initialize the renderer to be ready for drawing
     *
     */
    void initRenderer();

    /**
     * Render the base canvas
     *
     * @param mouseX The mouse X position
     * @param mouseY The mouse Y position
     */
    void render(int mouseX, int mouseY);


    /**
     * Reload the renderer
     * <p>That can be useful if you reloaded</p>
     * <p>the display elements used in base canvas</p>
     *
     */
    void reloadRenderer();


    /**
     * Close the renderer
     *
     */
    void closeRenderer();

    /**
     * Get the base canvas of this renderer
     *
     * @return The base canvas
     */
    @NotNull
    DisplayCanvas getBaseCanvas();

    /**
     * Set the base canvas of this renderer
     *
     * @param baseCanvas The base canvas
     */
    void setBaseCanvas(@NotNull DisplayCanvas baseCanvas);

    /**
     * Get the display data of this renderer
     *
     * @return The display data
     */
    @NotNull
    DisplayData getDisplayData();

    /**
     * Get the screen that this renderer is attached to
     * <p>If not attached then it means that</p>
     * <p>the renderer is drawn manually or in HUD</p>
     *
     * @return The screen or null, if not attached
     */
    @Nullable
    BaseScreen getAttachedGuiScreen();

    /**
     * Get the mod instance that created this renderer
     *
     * @return The mod instance
     */
    @NotNull
    AtumMod getAtumMod();
}
