package me.phoenixra.atumodcore.api;

import lombok.Getter;
import me.phoenixra.atumodcore.api.config.ConfigManager;
import me.phoenixra.atumodcore.api.display.EnabledCanvasRegistry;
import me.phoenixra.atumodcore.api.display.DisplayElementRegistry;
import me.phoenixra.atumodcore.api.display.actions.DisplayActionRegistry;
import me.phoenixra.atumodcore.api.input.InputHandler;
import me.phoenixra.atumodcore.api.network.NetworkManager;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public abstract class AtumMod {

    @Getter
    private AtumAPI api;

    @Getter
    private Logger logger;

    @Getter
    private ConfigManager configManager;

    @Getter
    private DisplayElementRegistry displayElementRegistry;
    @Getter
    private DisplayActionRegistry displayActionRegistry;
    @Getter
    private EnabledCanvasRegistry enabledCanvasRegistry;
    @Getter
    private InputHandler inputHandler;
    @Getter
    private NetworkManager networkManager;

    @Getter
    private File dataFolder;
    public AtumMod() {
        if(AtumAPI.getInstance() == null) {
            AtumAPI.Instance.set(createAPI());
            api = AtumAPI.getInstance();
            if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
                inputHandler = api.createInputHandler(this);
            }
        }
        api = AtumAPI.getInstance();
        logger = AtumAPI.getInstance().createLogger(this);
        if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
            inputHandler = AtumAPI.getInstance().getCoreMod().getInputHandler();
            configManager = AtumAPI.getInstance().createConfigManager(this);
            dataFolder =  new File(Minecraft.getMinecraft().mcDataDir,"config/" + getName());

            displayActionRegistry = AtumAPI.getInstance().createDisplayActionRegistry(this);
            displayElementRegistry = AtumAPI.getInstance().createDisplayElementRegistry(this);

            enabledCanvasRegistry = AtumAPI.getInstance().createEnabledCanvasRegistry(this);

            networkManager = AtumAPI.getInstance().createNetworkManager(this);
        } else {
            dataFolder =  new File("");
        }
    }

    public abstract @NotNull String getName();
    public abstract @NotNull String getModID();

    public abstract boolean isDebugEnabled();
    protected AtumAPI createAPI() {
        return null;
    }

}
