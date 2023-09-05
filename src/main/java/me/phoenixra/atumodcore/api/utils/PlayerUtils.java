package me.phoenixra.atumodcore.api.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class PlayerUtils {
    private static File skinFile = new File(Minecraft.getMinecraft().mcDataDir, "saved_skin.png");

    public static void savePlayerSkin(ResourceLocation resourceLocation){
        try {
            if(skinFile.exists()) skinFile.delete();

            // Get the texture data as an input stream
            InputStream inputStream = Minecraft.getMinecraft().getResourceManager().getResource(resourceLocation).getInputStream();

            // Copy the input stream to the head texture file
            FileUtils.copyInputStreamToFile(inputStream, skinFile);

            // Close the input stream
            inputStream.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public static void bindPlayerSkinTexture(){
        try {
            BufferedImage skinImage = ImageIO.read(skinFile);
            BufferedImage headImage = new BufferedImage(8, 8, BufferedImage.TYPE_INT_ARGB);

            // Extract the head texture from the skin image
            headImage.getGraphics().drawImage(skinImage, 0, 0, 8, 8, 8, 8, 16, 16, null);

            TextureUtil.uploadTextureImage(new DynamicTexture(headImage).getGlTextureId(), headImage);
        }catch (Exception e){
            String uuid = Minecraft.getMinecraft().getSession().getProfile().getId().toString();
            String skinUrl = String.format("https://crafatar.com/skins/%s.png", uuid);

            try {
                InputStream inputStream = new URL(skinUrl).openStream();
                BufferedImage skinImage = ImageIO.read(inputStream);
                TextureUtil.uploadTextureImage(new DynamicTexture(skinImage).getGlTextureId(), skinImage);
            }catch (Exception e1){
                Minecraft.getMinecraft().getTextureManager().bindTexture(
                        new ResourceLocation("gtwclient:textures/gui/main_menu/default_head.png")
                );
            }
        }


    }
}
