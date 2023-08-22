package me.phoenixra.atumodcore.mod.resourcepack;

import me.phoenixra.atumodcore.mod.AtumModCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;

public class PackHandler {
    public static final String PACK_NAME = "atum.hiddenpack";
    public static final ResourceLocation DUMMY_PACK_META = new ResourceLocation("atum:resourcepack/pack.mcmeta");

    public static File resourcesDirectory = new File(Minecraft.getMinecraft().mcDataDir, "resources");

    public static void init() {
        addPackToDefaults(new AtumFolderResourcePack());

        AtumModCore.getInstance().getLogger().info("[AtumModCore] PackHandler initialized!");

    }

    public static void prepareResourcesFolder() {

        if (!resourcesDirectory.isDirectory()) {
            resourcesDirectory.mkdirs();
        }

    }

    public static void addPackToDefaults(IResourcePack pack) {
        try {
            Field f = ObfuscationReflectionHelper.findField(Minecraft.class, "field_110449_ao");
            List<IResourcePack> defaultPacks = (List<IResourcePack>) f.get(Minecraft.getMinecraft());
            defaultPacks.add(pack);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
