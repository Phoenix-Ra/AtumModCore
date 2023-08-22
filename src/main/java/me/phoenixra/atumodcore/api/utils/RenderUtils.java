package me.phoenixra.atumodcore.api.utils;

import me.phoenixra.atumodcore.api.tuples.PairRecord;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.image.BufferedImage;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glPopMatrix;

public class RenderUtils {
    private static double windowRatioXCache = 1.0;
    private static double windowRatioYCache = 1.0;
    private static int guiScaleCache = 1;
    private static int scaleFactorCache = 1;

    public static void fill(float minX, float minY, float maxX, float maxY, int color, float opacity) {

        if (minX < maxX) {
            float i = minX;
            minX = maxX;
            maxX = i;
        }
        if (minY < maxY) {
            float j = minY;
            minY = maxY;
            maxY = j;
        }

        float r = (float)(color >> 16 & 255) / 255.0F;
        float g = (float)(color >> 8 & 255) / 255.0F;
        float b = (float)(color & 255) / 255.0F;
        float a = (float)(color >> 24 & 255) / 255.0F;

        a = a * opacity;

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bb = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color(r, g, b, a);
        bb.begin(7, DefaultVertexFormats.POSITION);
        bb.pos(minX, maxY, 0.0F).endVertex();
        bb.pos(maxX, maxY, 0.0F).endVertex();
        bb.pos(maxX, minY, 0.0F).endVertex();
        bb.pos(minX, minY, 0.0F).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();

    }
    public static void setZLevelPre(int zLevel) {
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableDepth();
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0D, 0.0D, zLevel);
    }

    public static void setZLevelPost() {
        GlStateManager.popMatrix();
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableDepth();
    }

    public static void enableScissor(int x, int y, int width, int height) {
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor(x, y, width, height);

    }


    public static void disableScissor() {
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    public static void bindTexture(ResourceLocation texture, boolean depthTest) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
        GlStateManager.enableBlend();
        if (depthTest) {
            GlStateManager.enableDepth();
        }
    }

    public static void bindTexture(ResourceLocation texture) {
        bindTexture(texture, false);
    }

    private static ResourceLocation WHITE = null;
    private static ResourceLocation BLANK = null;
    public static ResourceLocation getWhiteImageResource() {
        if (WHITE != null) {
            return WHITE;
        }
        BufferedImage i = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        i.setRGB(0, 0, Color.WHITE.getRGB());
        ResourceLocation r = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation("whiteback", new DynamicTexture(i));
        WHITE = r;
        return r;
    }

    public static ResourceLocation getBlankImageResource() {
        if (BLANK != null) {
            return BLANK;
        }
        BufferedImage i = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        i.setRGB(0, 0, new Color(255, 255, 255, 0).getRGB());
        ResourceLocation r = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation("blankback", new DynamicTexture(i));
        BLANK = r;
        return r;
    }
    public static void drawCompleteImage(int posX, int posY, int width, int height)
    {
        double scaleX = (scaleFactorCache/windowRatioXCache);
        double scaleY = (scaleFactorCache/windowRatioYCache);
        posX = (int) (posX / scaleX);
        posY = (int) (posY / scaleY);
        width = (int) (width / scaleX);
        height = (int) (height / scaleY);
        glPushMatrix();
        GlStateManager.enableBlend();

        glTranslatef(posX, posY, 0);
        glBegin(GL_QUADS);

        glTexCoord2f(0, 0);
        glVertex3f(0, 0, 0);
        glTexCoord2f(0, 1);
        glVertex3f(0, height, 0);
        glTexCoord2f(1, 1);
        glVertex3f(width, height, 0);
        glTexCoord2f(1, 0);
        glVertex3f(width, 0, 0);
        glEnd();

        glPopMatrix();
    }

    public static void drawPartialImage(int posX,
                                        int posY,
                                        int width,
                                        int height,
                                        int imageX,
                                        int imageY,
                                        int imagePartWidth,
                                        int imagePartHeight)
    {
        double scaleX = (scaleFactorCache/windowRatioXCache);
        double scaleY = (scaleFactorCache/windowRatioYCache);
        posX = (int) (posX / scaleX);
        posY = (int) (posY / scaleY);
        width = (int) (width / scaleX);
        height = (int) (height / scaleY);
        double imageWidth = glGetTexLevelParameteri(GL_TEXTURE_2D, 0, GL_TEXTURE_WIDTH);
        double imageHeight = glGetTexLevelParameteri(GL_TEXTURE_2D, 0, GL_TEXTURE_HEIGHT);

        double einsTeilerWidth = 1F / imageWidth;
        double uvWidth = einsTeilerWidth * imagePartWidth;
        double uvX = einsTeilerWidth * imageX;

        double einsTeilerHeight = 1F / imageHeight;
        double uvHeight = einsTeilerHeight * imagePartHeight;
        double uvY = einsTeilerHeight * imageY;

        glPushMatrix();
        GlStateManager.enableBlend();

        glTranslatef(posX, posY, 0);
        glBegin(GL_QUADS);

        glTexCoord2d(uvX, uvY);
        glVertex3f(0, 0, 0);
        glTexCoord2d(uvX, uvY + uvHeight);
        glVertex3f(0, height, 0);
        glTexCoord2d(uvX + uvWidth, uvY + uvHeight);
        glVertex3f(width, height, 0);
        glTexCoord2d(uvX + uvWidth, uvY);
        glVertex3f(width, 0, 0);
        glEnd();

        glPopMatrix();
    }


    public static int getScaleFactor(){
        Minecraft mc = Minecraft.getMinecraft();
        int scaledWidth = mc.displayWidth;
        int scaledHeight = mc.displayHeight;
        int guiScale = mc.gameSettings.guiScale;

        double windowRatioX = getWindowRatioWidth();
        double windowRatioY = getWindowRatioHeight();
        if(guiScaleCache == guiScale &&
                windowRatioXCache == windowRatioX && windowRatioYCache == windowRatioY){
            return scaleFactorCache;
        }

        int scaleFactor = 1;
        boolean flag = mc.isUnicode();
        if (guiScale == 0)
        {
            guiScale = 1000;
        }

        while (scaleFactor < guiScale && scaledWidth / (scaleFactor + 1) >= 320 && scaledHeight / (scaleFactor + 1) >= 240)
        {
            ++scaleFactor;
        }

        if (flag && scaleFactor % 2 != 0 && scaleFactor != 1)
        {
            --scaleFactor;
        }
        guiScaleCache = guiScale;
        windowRatioXCache = windowRatioX;
        windowRatioYCache = windowRatioY;
        scaleFactorCache = scaleFactor;
        return scaleFactor;
    }

    public static double getWindowRatioWidth(){
        return (double) Display.getWidth()/1920;
    }
    public static double getWindowRatioHeight(){
        return (double)Display.getHeight()/1080;
    }
}
