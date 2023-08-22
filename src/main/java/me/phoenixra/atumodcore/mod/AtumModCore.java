package me.phoenixra.atumodcore.mod;

import lombok.Getter;
import me.phoenixra.atumodcore.api.AtumAPI;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.config.Config;
import me.phoenixra.atumodcore.api.config.ConfigType;
import me.phoenixra.atumodcore.api.gui.handlers.ButtonHandler;
import me.phoenixra.atumodcore.api.gui.handlers.PopupHandler;
import me.phoenixra.atumodcore.core.AtumAPIImpl;
import me.phoenixra.atumodcore.mod.input.KeyboardHandler;
import me.phoenixra.atumodcore.mod.input.MouseInput;
import me.phoenixra.atumodcore.mod.resourcepack.PackHandler;
import me.phoenixra.atumodcore.mod.sound.SoundHandler;
import me.phoenixra.atumodcore.mod.test.TestMenu;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = "atumodcore", acceptedMinecraftVersions="[1.12,1.12.2]",name = "AtumModCore", version ="1.0.0")
public class AtumModCore extends AtumMod {

    @Getter
    private static AtumModCore instance;

    public static boolean isOptifineLoaded = false;

    public AtumModCore() {
        instance = this;
        if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
            getApi().createLoadableConfig(this,
                    "test",
                    "",
                    ConfigType.JSON,
                    false
            );

            PopupHandler.init();

            KeyboardHandler.init();

            SoundHandler.init();

            ButtonHandler.init();



            try {
                Class.forName("optifine.Installer");
                isOptifineLoaded = true;
                getLogger().info("[AtumModCore] Optifine detected!");
            }
            catch (ClassNotFoundException e) {}

        }


        getLogger().info("[AtumModCore] Successfully initialized!");
        getLogger().info("[AtumModCore] Server-side libs ready to use!");

    }
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void onRenderOverlay(GuiOpenEvent event){
        if(event.getGui() instanceof GuiMainMenu){
            event.setGui(new TestMenu());
        }
    }
    @Override
    public String getName() {
        return "AtumModCore";
    }

    @Override
    public boolean isDebugEnabled() {
        return true;
    }

    @Mod.EventHandler
    private void onClientSetup(FMLPostInitializationEvent e) {

        if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {

            //PackHandler.init();
            MouseInput.init();
            getLogger().info("[AtumModCore] Client-side libs ready to use!");

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