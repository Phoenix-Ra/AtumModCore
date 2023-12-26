package me.phoenixra.atumodcore.mod;

import lombok.Getter;
import me.phoenixra.atumodcore.api.AtumAPI;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.AtumModProperties;
import me.phoenixra.atumodcore.api.config.ConfigType;
import me.phoenixra.atumodcore.core.AtumAPIImpl;
import me.phoenixra.atumodcore.mod.sound.SoundHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.jetbrains.annotations.NotNull;

@Mod(
        modid = AtumModProperties.MOD_ID,
        name = AtumModProperties.MOD_NAME,
        version = AtumModProperties.VERSION,
        acceptedMinecraftVersions="[1.12,1.12.2]"
)
public class AtumModCore extends AtumMod {

    @Getter
    private static AtumModCore instance;

    public static boolean isOptifineLoaded = false;

    public AtumModCore() {
        getLogger().info("\n" +
                "     \\\\\\\\////\n" +
                "    -========-\n" +
                "   // '''''' \\\\\n" +
                "  // PHOENIXRA \\\\\n" +
                " //_____________\\\\\n" +
                " \\\\             //\n" +
                "  \\\\ DEVELOPER //\n" +
                "   \\\\_________//\n" +
                "    / * * * * \\\n" +
                "   /   * * *   \\\n" +
                "  / * * * * * * \\\n" +
                " (______/ \\______)\n" +
                "                       --- AtumModCore\n");
        instance = this;
        if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
            getApi().createLoadableConfig(this,
                    "test",
                    "",
                    ConfigType.JSON,
                    false
            );

            SoundHandler.init();


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

    @Override
    public @NotNull String getName() {
        return "AtumModCore";
    }

    @Override
    public @NotNull String getModID() {
        return "assets/atumodcore";
    }

    @Override
    public boolean isDebugEnabled() {
        return false;
    }

    @Mod.EventHandler
    private void onClientSetup(FMLPostInitializationEvent e) {

        if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {/*
            getConfigManager().reloadAllConfigCategories();
            getLogger().info("[AtumModCore] Client-side libs ready to use!");*/
            PostLoadingHandler.runPostLoadingEvents();

        }
    }

    /**
     * ONLY WORKS CLIENT-SIDE! DOES NOTHING ON A SERVER!
     */
    public static void addPostLoadingEvent(String modid, Runnable event) {
        PostLoadingHandler.addEvent(modid, event);
    }


    @Override
    protected AtumAPI createAPI() {
        return new AtumAPIImpl(this);
    }
}