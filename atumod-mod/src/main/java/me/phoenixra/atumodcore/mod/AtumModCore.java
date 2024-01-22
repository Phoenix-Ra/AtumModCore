package me.phoenixra.atumodcore.mod;

import lombok.Getter;
import me.phoenixra.atumodcore.api.AtumAPI;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.AtumModProperties;
import me.phoenixra.atumodcore.api.config.ConfigType;
import me.phoenixra.atumodcore.api.service.AtumModService;
import me.phoenixra.atumodcore.core.AtumAPIImpl;
import me.phoenixra.atumodcore.mod.sound.SoundHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.relauncher.Side;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.Display;

import java.io.File;
import java.util.List;

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
            getApi().createLoadableConfig(this,
                    "settings",
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

    }


    @Mod.EventHandler
    public void construct(FMLConstructionEvent event) {
        notifyModServices(event);
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Display.setResizable(false);

        MinecraftForge.EVENT_BUS.register(this);

        File configDir = event.getModConfigurationDirectory();
        dataFolder = new File(configDir, getName());

        for(AtumMod atumMod : getApi().getLoadedAtumMods()){
            atumMod.setDataFolder(new File(configDir, atumMod.getName()));
        }
        notifyModServices(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        notifyModServices(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        notifyModServices(event);
    }

    @Mod.EventHandler
    public void loadComplete(FMLLoadCompleteEvent event) {
        notifyModServices(event);
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        notifyModServices(event);
    }

    @Mod.EventHandler
    public void serverStarted(FMLServerStartedEvent event) {
        notifyModServices(event);
    }


    @Override
    protected AtumAPI createAPI() {
        return new AtumAPIImpl(this);
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
    public @NotNull String getPackagePath() {
        return "me.phoenixra.atumodcore";
    }

    @Override
    public boolean isDebugEnabled() {
        return false;
    }
}