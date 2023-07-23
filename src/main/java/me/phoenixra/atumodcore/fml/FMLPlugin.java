package me.phoenixra.atumodcore.fml;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.apache.logging.log4j.LogManager;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;

import javax.annotation.Nullable;
import java.util.Map;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Enumeration;

@IFMLLoadingPlugin.SortingIndex(1)
public class FMLPlugin implements IFMLLoadingPlugin {

    public FMLPlugin() {

        LogManager.getLogger().info("[AtumModCore] LOADING...");

        MixinBootstrap.init();
        Mixins.addConfiguration("atum.mixin.json");
        loadConfigs();

    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[0];
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Nullable
    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }


    public static void loadConfigs() {
        try {

            Enumeration<URL> resources = FMLPlugin.class.getClassLoader().getResources("META-INF/MANIFEST.MF");
            for (URL url : Collections.list(resources)) {
                try {
                    InputStream in = url.openStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
                    String line = reader.readLine();
                    while (line != null) {
                        if (line.startsWith("loadMixinConfigInAtum:")) {
                            String mixinConfig = line.split(":", 2)[1];
                            if (mixinConfig.startsWith(" ")) {
                                mixinConfig = mixinConfig.substring(1);
                                System.out.println("[AtumModCore] Loading Mixin Config: " + mixinConfig);
                                Mixins.addConfiguration(mixinConfig);
                            }
                        }
                        line = reader.readLine();
                    }
                    reader.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
