package me.phoenixra.atumodcore.api;

import lombok.Getter;
import lombok.Setter;
import me.phoenixra.atumconfig.api.ConfigOwner;
import me.phoenixra.atumconfig.api.config.ConfigManager;
import me.phoenixra.atumodcore.api.config.AtumConfigManagerMod;
import me.phoenixra.atumodcore.api.display.DisplayManager;
import me.phoenixra.atumodcore.api.input.InputHandler;
import me.phoenixra.atumodcore.api.network.NetworkManager;
import me.phoenixra.atumodcore.api.service.AtumModService;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;

public abstract class AtumMod implements ConfigOwner {

    @Getter
    private AtumAPI api;

    @Getter
    private Logger logger;

    @Getter
    private DisplayManager displayManager;
    @Getter
    private InputHandler inputHandler;
    @Getter
    private NetworkManager networkManager;

    @Getter
    private ConfigManager configManager;

    @Getter @Setter
    protected File dataFolder;

    private HashMap<String,AtumModService> modServices = new LinkedHashMap<>();
    public AtumMod() {
        if(AtumAPI.getInstance() == null) {
            AtumAPI.Instance.set(createAPI());
            api = AtumAPI.getInstance();
            if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
                inputHandler = api.createInputHandler(this);
            }
        }
        api = AtumAPI.getInstance();
        api.registerAtumMod(this);
        logger = AtumAPI.getInstance().createLogger(this);
        if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
            inputHandler = AtumAPI.getInstance().getCoreMod().getInputHandler();
            dataFolder =  new File(Minecraft.getMinecraft().mcDataDir,"config/" + getName());
            configManager = new AtumConfigManagerMod(this);
            displayManager = AtumAPI.getInstance().createDisplayManager(this);
        }else{
            dataFolder =  new File("config/" + getName());
            configManager = new AtumConfigManagerMod(this);
        }
        networkManager = AtumAPI.getInstance().createNetworkManager(this);
    }


    /**
     * Get the mod name
     *
     * @return The name.
     */
    public abstract @NotNull String getName();

    /**
     * Get the mod id
     *
     * @return The id.
     */
    public abstract @NotNull String getModID();

    /**
     * Get the mod package path
     *<br><br>
     * It is used in java reflection to register display elements
     *
     * @return The package path.
     */
    public abstract @NotNull String getPackagePath();

    /**
     * Is debug enabled.
     * <br><br>
     * Enables special debug features and logs.
     *
     * @return true if enabled
     */
    public abstract boolean isDebugEnabled();

    /**
     * Create the API.
     * <br><br>
     * This method is called only in AtumModCore.
     * So, it is basically useless for you if you are not planning
     * to create your own API implementation.
     *
     * @return The API implementation.
     */
    protected AtumAPI createAPI() {
        return null;
    }

    /**
     * Get the mod service by id
     *
     * @param id The id
     * @return the atum mod service
     */
    @Nullable
    public AtumModService getModService(@NotNull String id){
        return modServices.get(id);
    }

    /**
     * Provide the mod service/
     * <br><br>
     * It is a convenient feature to listen
     * for FMLEvent outside the mod class
     *
     * @param service The atum mod service
     */
    public void provideModService(@NotNull AtumModService service){
        if(modServices.containsKey(service.getId())) return;
        modServices.put(service.getId(), service);
    }

    /**
     * Remove the mod service
     *
     * @param id The id of a mod service to remove
     */
    public void removeModService(@NotNull String id){
        AtumModService atumModService = modServices.get(id);
        if(atumModService==null) return;
        atumModService.onRemove();
        modServices.remove(id);
        MinecraftForge.EVENT_BUS.unregister(atumModService);
    }

    /**
     * Clear all mod services
     *
     */
    public void clearAllModServices(){
        for(AtumModService entry : modServices.values()){
            entry.onRemove();
            MinecraftForge.EVENT_BUS.unregister(entry);
        }
        modServices.clear();
    }

    /**
     * Notify all mod services about the FML event
     * <br><br>
     * Make sure to call this method in your mod class
     * if you are planning to use mod services
     *
     * @param event the fml event
     */
    protected void notifyModServices(@NotNull FMLEvent event){
        if(event instanceof FMLPreInitializationEvent){
            for(AtumModService entry : modServices.values()){
                MinecraftForge.EVENT_BUS.register(entry);
                entry.handleFmlEvent(event);
            }
        }else {
            for (AtumModService entry : modServices.values()) {
                entry.handleFmlEvent(event);
            }
        }
    }

    @Override
    public boolean supportMinecraft() {
        return true;
    }

    @Override
    public void logInfo(String s) {
        logger.info(s);
    }

    @Override
    public void logError(String s) {
        logger.error(s);
    }
    @Override
    public void logWarning(String s) {
        logger.warn(s);
    }
}
