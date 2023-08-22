package me.phoenixra.atumodcore.api.gui.menus.background;

import me.phoenixra.atumodcore.api.events.MenuReloadedEvent;
import me.phoenixra.atumodcore.api.registry.Registry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MenuBackgroundTypeRegistry {

    protected static Registry<MenuBackgroundType> registry = new Registry<>();

    private static boolean initialized = false;

    public static void init() {
        if (!initialized) {
            MinecraftForge.EVENT_BUS.register(new MenuBackgroundTypeRegistry());
            initialized = true;
        }
    }

    /**
     * Register your custom menu background type here.
     */
    public static void register(@NotNull MenuBackgroundType type) {
        registry.register(type);
    }

    /**
     * Unregister a previously added menu background type.
     */
    public static void unregister(String typeIdentifier) {
        registry.remove(typeIdentifier);
    }

    public static List<MenuBackgroundType> getBackgroundTypes() {
        return new ArrayList<>(registry.values());
    }

    /**
     * Returns the background type or NULL if no background type with this identifier was found.
     */
    public static MenuBackgroundType getByID(String typeIdentifier) {
        return registry.get(typeIdentifier);
    }

    @SubscribeEvent
    public void onReload(MenuReloadedEvent e) {
        for (MenuBackgroundType t : registry.values()) {
            t.loadBackgrounds();
        }
    }

}
