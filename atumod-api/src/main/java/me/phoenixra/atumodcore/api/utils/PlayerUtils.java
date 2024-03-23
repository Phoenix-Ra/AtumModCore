package me.phoenixra.atumodcore.api.utils;

import me.phoenixra.atumodcore.api.AtumAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;

public final class PlayerUtils {
    private static File skinFile = new File(Minecraft.getMinecraft().mcDataDir, "saved_skin.png");

    private static boolean isLoaded = false;


    public static boolean isInMultiplayer() {
        if (FMLCommonHandler.instance().getEffectiveSide().isClient()) {
            Minecraft mc = Minecraft.getMinecraft();
            World world = mc.world;
            if(world==null) return false;
            if (!world.isRemote) {
                return false;
            }
            return mc.getCurrentServerData() != null;
        } else {
            return true;
        }
    }
    /**
     * Binds the player's head skin texture to OpenGL
     * @return if succeeded
     */
    public static boolean bindPlayerSkinTexture(){
        if(!isLoaded && !savePlayerSkin()) return false;
        try {
            BufferedImage skinImage = ImageIO.read(skinFile);
            BufferedImage headImage = new BufferedImage(8, 8, BufferedImage.TYPE_INT_ARGB);

            // Extract the head texture from the skin image
            headImage.getGraphics().drawImage(skinImage, 0, 0, 8, 8, 8, 8, 16, 16, null);

            TextureUtil.uploadTextureImage(new DynamicTexture(headImage).getGlTextureId(), headImage);
        }catch (Exception e){
            AtumAPI.getInstance().getCoreMod().getLogger()
                    .error("Failed to bind player skin! "+e.getMessage());
            return false;

        }
        return true;
    }

    /**
     * Saves the player's skin to a file
     * You can use that if player's UUID has been changed during the
     * game session or u want to make the first head skin binding faster
     * @return if succeeded
     */
    public static boolean savePlayerSkin(){
        System.out.println("Saving player skin... "+Minecraft.getMinecraft().getSession().getProfile().getId().toString());
        String uuid = Minecraft.getMinecraft().getSession().getProfile().getId().toString();
        String skinUrl = String.format("https://crafatar.com/skins/%s.png", uuid);
        try(InputStream inputStream = new URL(skinUrl).openStream()) {
            if(skinFile.exists()) skinFile.delete();

            FileUtils.copyInputStreamToFile(inputStream, skinFile);

            isLoaded = true;
        }catch (Exception e1){
            AtumAPI.getInstance().getCoreMod().getLogger()
                    .error("Failed to save player skin! "+e1.getMessage());
            AtumAPI.getInstance().getCoreMod().getLogger()
                    .info("Saving default head instead...");
            //copy from resources
            try(InputStream inputStream = AtumAPI.getInstance().getCoreMod().getClass()
                    .getResourceAsStream("/assets/atumodcore/textures/default_head.png")) {
                if(skinFile.exists()) skinFile.delete();

                FileUtils.copyInputStreamToFile(inputStream, skinFile);

                isLoaded = true;
            }catch (Exception e2){
                AtumAPI.getInstance().getCoreMod().getLogger()
                        .error("Failed to save default head! "+e2.getMessage());
                return false;
            }

        }
        return true;
    }

    public PlayerUtils() {
        throw new RuntimeException("Utility class");
    }

}
