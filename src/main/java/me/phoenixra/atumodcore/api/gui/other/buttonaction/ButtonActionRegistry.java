package me.phoenixra.atumodcore.api.gui.other.buttonaction;

import me.phoenixra.atumodcore.api.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ButtonActionRegistry {

    private static final Logger LOGGER = LogManager.getLogger();

    protected static Registry<ButtonActionContainer> registry = new Registry<>();

    /**
     * Register your custom button actions here.
     */
    public static void registerButtonAction(@NotNull ButtonActionContainer action) {
        ButtonActionContainer c = getActionByName(action.getAction());
        if (c != null) {
            unregisterButtonAction(c.getID());
        }
        registry.register(action);
    }

    /**
     * Unregister a previously added button action.
     */
    public static void unregisterButtonAction(String id) {
        registry.remove(id);
    }

    public static List<ButtonActionContainer> getActions() {
        return new ArrayList<>(registry.values());
    }

    public static ButtonActionContainer getAction(String id) {
        return registry.get(id);
    }

    public static ButtonActionContainer getActionByName(String actionName) {
        for (ButtonActionContainer c : registry.values()) {
            if (c.getAction().equals(actionName)) {
                return c;
            }
        }
        return null;
    }

}
