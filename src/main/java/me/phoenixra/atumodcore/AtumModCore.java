package me.phoenixra.atumodcore;

import me.phoenixra.atumodcore.gui.handlers.ButtonHandler;
import me.phoenixra.atumodcore.gui.handlers.PopupHandler;
import me.phoenixra.atumodcore.input.KeyboardHandler;
import me.phoenixra.atumodcore.input.MouseInput;
import me.phoenixra.atumodcore.sound.SoundHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = "atum", acceptedMinecraftVersions="[1.12,1.12.2]")
public class  AtumModCore {

    public static final String VERSION = "1.6.0";

    public static Logger LOGGER = LogManager.getLogger();

    public static boolean isOptifineLoaded = false;

    public AtumModCore() {

        if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {

            PopupHandler.init();

            KeyboardHandler.init();

            SoundHandler.init();

            ButtonHandler.init();

            try {
                Class.forName("optifine.Installer");
                isOptifineLoaded = true;
                LOGGER.info("[AtumModCore] Optifine detected!");
            }
            catch (ClassNotFoundException e) {}

        }

        LOGGER.info("[AtumModCore] Successfully initialized!");
        LOGGER.info("[AtumModCore] Server-side libs ready to use!");

    }

    @Mod.EventHandler
    private void onClientSetup(FMLPostInitializationEvent e) {

        if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {

            MouseInput.init();

            LOGGER.info("[AtumModCore] Client-side libs ready to use!");

            PostLoadingHandler.runPostLoadingEvents();

        }

    }

    /**
     * ONLY WORKS CLIENT-SIDE! DOES NOTHING ON A SERVER!
     */
    public static void addPostLoadingEvent(String modid, Runnable event) {
        PostLoadingHandler.addEvent(modid, event);
    }
}