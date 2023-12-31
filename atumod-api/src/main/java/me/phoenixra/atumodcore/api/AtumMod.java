package me.phoenixra.atumodcore.api;

import lombok.Getter;
import lombok.Setter;
import me.phoenixra.atumodcore.api.config.ConfigManager;
import me.phoenixra.atumodcore.api.display.DisplayManager;
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
    private DisplayManager displayManager;
    @Getter
    private InputHandler inputHandler;
    @Getter
    private NetworkManager networkManager;

    @Getter @Setter
    protected File dataFolder;
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
        configManager = AtumAPI.getInstance().createConfigManager(this);
        if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
            inputHandler = AtumAPI.getInstance().getCoreMod().getInputHandler();
            dataFolder =  new File(Minecraft.getMinecraft().mcDataDir,"config/" + getName());

            displayManager = AtumAPI.getInstance().createDisplayManager(this);

            networkManager = AtumAPI.getInstance().createNetworkManager(this);
        }
    }


    public abstract @NotNull String getName();
    public abstract @NotNull String getModID();

    public abstract boolean isDebugEnabled();
    protected AtumAPI createAPI() {
        return null;
    }

}
