package me.phoenixra.atumodcore.api;

import lombok.Getter;
import me.phoenixra.atumodcore.api.config.ConfigManager;
import me.phoenixra.atumodcore.api.display.DisplayElementRegistry;
import me.phoenixra.atumodcore.core.AtumAPIImpl;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.Logger;

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
    private File dataFolder;
    public AtumMod() {
        if(AtumAPI.getInstance() == null) {
            AtumAPI.Instance.set(createAPI());
            api = AtumAPI.getInstance();
        }
        logger = AtumAPI.getInstance().createLogger(this);
        if (FMLClientHandler.instance().getSide() == Side.CLIENT) {
            configManager = AtumAPI.getInstance().createConfigManager(this);
            dataFolder =  new File(Minecraft.getMinecraft().mcDataDir,"config/" + getName());
            displayElementRegistry = AtumAPI.getInstance().createDisplayElementRegistry(this);
        } else {
            dataFolder =  new File("");
        }
    }

    public abstract String getName();

    public abstract boolean isDebugEnabled();
    protected AtumAPI createAPI() {
        return new AtumAPIImpl();
    }

}
