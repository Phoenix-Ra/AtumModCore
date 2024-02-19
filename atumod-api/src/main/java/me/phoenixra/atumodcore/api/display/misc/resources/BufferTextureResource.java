package me.phoenixra.atumodcore.api.display.misc.resources;

import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;

/**
 * Experimental
 */
public class BufferTextureResource implements TextureResource{
    private DynamicTexture texture;
    private BufferedImage bufferedImage;

    @Getter
    private ResourceLocation resourceLocation;
    @Getter
    private boolean loaded = false;
    @Getter
    private int width;
    @Getter
    private int height;


    public BufferTextureResource(BufferedImage bufferedImage){
        this.bufferedImage = bufferedImage;
        this.width = bufferedImage.getWidth();
        this.height = bufferedImage.getHeight();
    }
    public BufferTextureResource(InputStream stream) throws IOException {
        ImageInputStream imageStream = ImageIO.createImageInputStream(stream);
        this.bufferedImage = ImageIO.read(imageStream);
        this.width = bufferedImage.getWidth();
        this.height = bufferedImage.getHeight();
        IOUtils.closeQuietly(imageStream);
    }
    public BufferTextureResource(URL url) throws IOException, IllegalArgumentException {
        HttpURLConnection httpcon = (HttpURLConnection) url.openConnection(Minecraft.getMinecraft().getProxy());
        httpcon.setDoInput(true);
        httpcon.setDoOutput(false);
        httpcon.connect();
        InputStream stream = httpcon.getInputStream();
        if (stream == null) {
            throw new IllegalArgumentException("url is not accessible");
        }
        this.bufferedImage = ImageIO.read(stream);
        this.width = bufferedImage.getWidth();
        this.height = bufferedImage.getHeight();
        IOUtils.closeQuietly(stream);
        httpcon.disconnect();
    }
    public BufferTextureResource(File file) throws IOException {
        InputStream stream = Files.newInputStream(file.toPath());
        ImageInputStream imageStream = ImageIO.createImageInputStream(stream);
        this.bufferedImage = ImageIO.read(imageStream);
        this.width = bufferedImage.getWidth();
        this.height = bufferedImage.getHeight();
        IOUtils.closeQuietly(imageStream);
        IOUtils.closeQuietly(stream);
    }

    public void loadTexture() {
        if (this.loaded) {
            return;
        }

        try {
            texture = new DynamicTexture(bufferedImage);
            this.resourceLocation = Minecraft.getMinecraft()
                    .getTextureManager().getDynamicTextureLocation(
                            "displaytexture",
                            texture
                    );
            loaded = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clear() {
        this.bufferedImage.getGraphics().dispose();
        this.bufferedImage = null;
        if(texture!=null){
            texture.deleteGlTexture();
            texture = null;
        }

    }
}
