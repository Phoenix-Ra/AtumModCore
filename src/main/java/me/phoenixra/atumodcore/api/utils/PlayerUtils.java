package me.phoenixra.atumodcore.api.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;

public final class PlayerUtils {
    private static File skinFile = new File(Minecraft.getMinecraft().mcDataDir, "saved_skin.png");

    private static boolean isLoaded = false;
    /**
     * Binds the player's head skin texture to OpenGL
     */
    public static void bindPlayerSkinTexture(){
        if(!isLoaded) savePlayerSkin();
        try {
            BufferedImage skinImage = ImageIO.read(skinFile);
            BufferedImage headImage = new BufferedImage(8, 8, BufferedImage.TYPE_INT_ARGB);

            // Extract the head texture from the skin image
            headImage.getGraphics().drawImage(skinImage, 0, 0, 8, 8, 8, 8, 16, 16, null);

            TextureUtil.uploadTextureImage(new DynamicTexture(headImage).getGlTextureId(), headImage);
        }catch (Exception e){
            savePlayerSkin();
            try {
                BufferedImage skinImage = ImageIO.read(skinFile);
                BufferedImage headImage = new BufferedImage(8, 8, BufferedImage.TYPE_INT_ARGB);

                // Extract the head texture from the skin image
                headImage.getGraphics().drawImage(skinImage, 0, 0, 8, 8, 8, 8, 16, 16, null);

                TextureUtil.uploadTextureImage(new DynamicTexture(headImage).getGlTextureId(), headImage);
            }catch (Exception e1){
                e1.printStackTrace();
            }
        }
    }

    /**
     * Saves the player's skin to a file
     * You can use that if player's UUID has been changed during the
     * game session or u want to make the first head skin binding faster
     */
    public static void savePlayerSkin(){
        System.out.println("Saving player skin... "+Minecraft.getMinecraft().getSession().getProfile().getId().toString());
        String uuid = Minecraft.getMinecraft().getSession().getProfile().getId().toString();
        String skinUrl = String.format("https://crafatar.com/skins/%s.png", uuid);
        try(InputStream inputStream = new URL(skinUrl).openStream()) {
            if(skinFile.exists()) skinFile.delete();

            FileUtils.copyInputStreamToFile(inputStream, skinFile);

            isLoaded = true;
        }catch (Exception e1){
            e1.printStackTrace();
        }
    }

    public PlayerUtils() {
        throw new RuntimeException("Utility class");
    }

}
