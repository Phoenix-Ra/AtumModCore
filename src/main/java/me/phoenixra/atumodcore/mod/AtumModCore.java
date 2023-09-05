package me.phoenixra.atumodcore.mod;

import lombok.Getter;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.config.ConfigType;
import me.phoenixra.atumodcore.api.utils.PlayerUtils;
import me.phoenixra.atumodcore.mod.sound.SoundHandler;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.jetbrains.annotations.NotNull;

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
        return "atumodcore";
    }

    @Override
    public boolean isDebugEnabled() {
        return false;
    }

    @Mod.EventHandler
    private void onClientSetup(FMLPostInitializationEvent e) {

        if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
            getConfigManager().reloadAllConfigCategories();
            getLogger().info("[AtumModCore] Client-side libs ready to use!");
            PostLoadingHandler.runPostLoadingEvents();




        }
    }


    @SubscribeEvent
    public void onPlayerJoin(EntityJoinWorldEvent event){
        PlayerUtils.savePlayerSkin(Minecraft.getMinecraft().player.getLocationSkin());
    }

    /**
     * ONLY WORKS CLIENT-SIDE! DOES NOTHING ON A SERVER!
     */
    public static void addPostLoadingEvent(String modid, Runnable event) {
        PostLoadingHandler.addEvent(modid, event);
    }
}