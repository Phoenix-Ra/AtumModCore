package me.phoenixra.atumodcore.api;

import me.phoenixra.atumconfig.api.placeholders.context.PlaceholderContext;
import me.phoenixra.atumodcore.api.display.DisplayManager;
import me.phoenixra.atumodcore.api.input.InputHandler;
import me.phoenixra.atumodcore.api.network.NetworkManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface AtumAPI {

    /**
     * Create an input handler
     *
     * @param atumMod The mod to attach to.
     * @return The handler
     */
    @NotNull
    InputHandler createInputHandler(@NotNull AtumMod atumMod);


    /**
     * Create a logger
     *
     * @param atumMod The mod to attach to.
     * @return The logger
     */
    @NotNull
    Logger createLogger(@NotNull AtumMod atumMod);



    /**
     * Create a display manager
     *
     * @param atumMod The mod to attach to.
     * @return The manager
     */
    @NotNull
    DisplayManager createDisplayManager(@NotNull AtumMod atumMod);

    /**
     * Create a network manager
     *
     * @param atumMod The mod to attach to.
     * @return The manager
     */
    @NotNull
    NetworkManager createNetworkManager(@NotNull AtumMod atumMod);

    /**
     * Evaluate an expression.
     *
     * @param atumMod    The atum mod instance
     * @param expression The expression.
     * @param context    The context.
     * @return The value of the expression, or zero if invalid.
     */
    double evaluate(@NotNull AtumMod atumMod,
                    @NotNull String expression,
                    @NotNull PlaceholderContext context);

    /**
     * Get the core mod.
     * @return The core mod.
     */
    AtumMod getCoreMod();

    /**
     * Get the loaded mod.
     *
     * @param name The name of the mod.
     * @return The mod or null if not found.
     */
    @Nullable AtumMod getLoadedAtumMod(String name);

    /**
     * Get all loaded mods.
     *
     * @return The mods.
     */
    @NotNull Collection<AtumMod> getLoadedAtumMods();

    /**
     * Register a mod.
     *
     * @param atumMod The mod.
     */
    void registerAtumMod(@NotNull AtumMod atumMod);

    /**
     * Get the instance.
     *
     * @return The instance.
     */
    static AtumAPI getInstance() {
        return Instance.get();
    }

    final class Instance {
        private static AtumAPI api;
        private Instance() {
            throw new UnsupportedOperationException("This is an utility class and cannot be instantiated");
        }

        static void set(final AtumAPI api) {
            if(Instance.api != null) return;

            Instance.api = api;
        }


        static AtumAPI get() {
            return api;
        }
    }


}
