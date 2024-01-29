package me.phoenixra.atumodcore.api.display;


import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.config.Config;
import me.phoenixra.atumodcore.api.display.actions.ActionArgs;
import me.phoenixra.atumodcore.api.display.actions.ActionData;
import me.phoenixra.atumodcore.api.display.misc.DisplayResolution;
import me.phoenixra.atumodcore.api.display.misc.variables.OptimizedVariableInt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface DisplayElement extends Cloneable{


    /**
     * Get the current x coordinate of the element
     *
     * @return The x coordinate
     */
    int getX();

    /**
     * Get the current y coordinate of the element
     *
     * @return The y coordinate
     */
    int getY();

    /**
     * Get the current width of the element
     *
     * @return The width
     */
    int getWidth();

    /**
     * Get the current height of the element
     *
     * @return The height
     */
    int getHeight();

    /**
     * Get the current global x coordinate of the element
     *
     * @return The global x coordinate
     */
    int getGlobalX();

    /**
     * Get the current global y coordinate of the element
     *
     * @return The global y coordinate
     */
    int getGlobalY();

    /**
     * Get the origin x coordinate of the element
     *
     * @return The origin x coordinate
     */
    @NotNull
    OptimizedVariableInt getOriginX();

    /**
     * Get the origin y coordinate of the element
     *
     * @return The origin y coordinate
     */
    @NotNull
    OptimizedVariableInt getOriginY();

    /**
     * Get the origin width of the element
     *
     * @return The origin width
     */
    @NotNull
    OptimizedVariableInt getOriginWidth();

    /**
     * Get the origin height of the element
     *
     * @return The origin height
     */
    @NotNull
    OptimizedVariableInt getOriginHeight();

    /**
     * Get the addition x coordinate of the element
     * <p>It is used in animations/p>
     *
     * @return The addition x coordinate
     */
    int getAdditionX();

    /**
     * Get the addition y coordinate of the element
     * <p>It is used in animations/p>
     *
     * @return The addition y coordinate
     */
    int getAdditionY();

    /**
     * Get the addition width of the element
     * <p>It is used in animations/p>
     *
     * @return The addition width
     */
    int getAdditionWidth();

    /**
     * Get the addition height of the element
     * <p>It is used in animations/p>
     *
     * @return The addition height
     */
    int getAdditionHeight();

    /**
     * Set the x origin coordinate of the element
     *
     * @param variable The x coordinate
     */
    void setOriginX(OptimizedVariableInt variable);

    /**
     * Set the y origin coordinate of the element
     *
     * @param variable The y coordinate
     */
    void setOriginY(OptimizedVariableInt variable);

    /**
     * Set the width origin of the element
     *
     * @param variable The width
     */
    void setOriginWidth(OptimizedVariableInt variable);

    /**
     * Set the height origin of the element
     *
     * @param variable The height
     */
    void setOriginHeight(OptimizedVariableInt variable);

    /**
     * Set the addition x coordinate of the element
     * <p>It is used in animations/p>
     *
     * @param x The x coordinate
     */
    void setAdditionX(int x);

    /**
     * Set the addition y coordinate of the element
     * <p>It is used in animations/p>
     *
     * @param y The y coordinate
     */
    void setAdditionY(int y);

    /**
     * Set the addition width of the element
     * <p>It is used in animations/p>
     *
     * @param width The width
     */
    void setAdditionWidth(int width);

    /**
     * Set the addition height of the element
     * <p>It is used in animations/p>
     *
     * @param height The height
     */
    void setAdditionHeight(int height);


    /**
     * Get the last mouse x coordinate
     *
     * @return The x coordinate
     */
    int getLastMouseX();

    /**
     * Get the last mouse y coordinate
     *
     * @return The y coordinate
     */
    int getLastMouseY();

    /**
     * Apply the resolution optimizer to the element
     * from config
     *
     * @param resolution The resolution
     * @param config The config
     */
    void applyResolutionOptimizer(@NotNull DisplayResolution resolution,
                                  @NotNull Config config);

    /**
     * Draw the element
     *
     * @param resolution The resolution
     * @param scaleFactor The mc scale factor
     * @param mouseX The mouse x coordinate
     * @param mouseY The mouse y coordinate
     */
    void draw(@NotNull DisplayResolution resolution,
              float scaleFactor,
              int mouseX, int mouseY);

    /**
     * Is the element hovered?
     *
     * @param mouseX The mouse x coordinate
     * @param mouseY The mouse y coordinate
     * @return true/false
     */
    boolean isHovered(int mouseX, int mouseY);

    /**
     * Removes cached data
     *
     */
    void onRemove();

    /**
     * Set the element owner
     *
     * @param elementOwner The element owner
     */
    void setElementOwner(@NotNull DisplayCanvas elementOwner);

    /**
     * Get the element owner
     *
     * @return The element owner
     */
    @NotNull DisplayCanvas getElementOwner();

    /**
     * Update the variables of the element from config
     *
     * @param config The config
     * @param configKey The config key to save
     */
    void updateVariables(@NotNull Config config, @Nullable String configKey);

    /**
     * Update the base variables of the element from config
     *
     * @param config The config
     * @param configKey The config key to save
     */
    void updateBaseVariables(@NotNull Config config, @Nullable String configKey);

    /**
     * Update the element variables from config
     *
     * @param config The config
     * @param configKey The config key to save
     */
    void updateElementVariables(@NotNull Config config, @Nullable String configKey);

    /**
     * Is element inside the given coordinates?
     * @param mouseX the mouse x coordinate
     * @param mouseY the mouse y coordinate
     * @return true/false
     */
    default boolean isCoordinateInElement(int mouseX, int mouseY){
        return mouseX >= getGlobalX() &&
                mouseX <= getGlobalX() + getWidth()
                && mouseY >= getGlobalY()
                && mouseY <= getGlobalY() + getHeight();
    }

    /**
     * Is element inside the display element?
     * @param element other element
     * @return true/false
     */
    default boolean isElementInsideThis(@NotNull DisplayElement element){
        return  element.getGlobalX() <= getGlobalX() &&
                element.getGlobalX() + element.getWidth() >= getGlobalX() + getWidth()
                && element.getGlobalY() <= getGlobalY()
                &&  element.getGlobalY() + element.getHeight() >= getGlobalY() + getHeight();
    }


    /**
     * Get the active state of the element
     *
     * @return true/false
     */
    boolean isActive();

    /**
     * Set the active state of the element
     *
     * @param active true/false
     */
    void setActive(boolean active);


    void setOutlineSelected(boolean outlineSelected);


    /**
     * Perform an action with the given id and args
     *
     * @param actionId The id of the action
     * @param actionData The data of the action
     */
    void performAction(@NotNull String actionId,
                       @NotNull ActionData actionData);

    /**
     * Perform an action with the given id and args
     *
     * @param actionId The id of the action
     * @param args The args of the action
     */
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


    /**
     * Get the config of this element
     *
     * @return The config
     */
    @Nullable
    Config getSettingsConfig();


    /**
     * Get the layer of the element
     *
     * @return The layer
     */
    @NotNull DisplayLayer getLayer();

    /**
     * Get the id of the element
     *
     * @return The id
     */
    @NotNull String getId();
    /**
     * Get the template id of the element
     *
     * @return The template id
     */
    @Nullable String getTemplateId();
    /**
     * Get the config key of the element
     *
     * @return The config key
     */
    @Nullable String getConfigKey();


    /**
     * Clone the element with new variables
     *
     * @param id The new id
     * @param config The new config
     * @param configKey The new config key
     * @param elementOwner The new element owner
     * @return The cloned element
     */
    @NotNull
    DisplayElement cloneWithNewVariables(@NotNull String id,
                                         @NotNull Config config,
                                         @Nullable String configKey,
                                         @Nullable DisplayCanvas elementOwner);
    /**
     * Clone the element with a new id
     * The new id going to have the following pattern:
     * "[configName]@[randomUUID]"
     * So, you can recognize the specific element splitting the id with "@"

     * @return The cloned element
     */
    @NotNull DisplayElement cloneWithRandomId();

    /**
     * Clone the element
     *
     * @return The cloned element
     */
    @NotNull DisplayElement clone();


    /**
     * Get the mod instance that owns this element
     *
     * @return The mod instance
     */
    @NotNull AtumMod getAtumMod();

}
