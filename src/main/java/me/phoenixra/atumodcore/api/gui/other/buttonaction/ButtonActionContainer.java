
package me.phoenixra.atumodcore.api.gui.other.buttonaction;


import lombok.Getter;
import me.phoenixra.atumodcore.api.registry.Registrable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A button action container.<br><br>
 *
 * Needs to be registered to the {@link ButtonActionRegistry} at mod init.
 */
public abstract class ButtonActionContainer implements Registrable {

    @Getter
    private final String ID;

    public ButtonActionContainer(@Nonnull String id) {
        this.ID = id;
    }

    /**
     * Returns the name of this action.
     */
    public abstract String getAction();

    /**
     * If this action has a value or not.
     */
    public abstract boolean hasValue();

    /**
     * Is called when a button with this action gets clicked.<br>
     * @param value The value that was set to the action or NULL if this action has no value.
     */
    public abstract void execute(String value);

    /**
     * The description of the action.
     */
    public abstract String getActionDescription();

    /**
     * The description of the action value.
     */
    public abstract String getValueDescription();

    /**
     * An example of how the value of this action should look like.<br>
     * Return NULL if this action has no value.
     */
    @Nullable
    public abstract String getValueExample();


}
