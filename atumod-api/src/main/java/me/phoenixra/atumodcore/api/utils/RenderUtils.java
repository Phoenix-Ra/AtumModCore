package me.phoenixra.atumodcore.api.utils;

import me.phoenixra.atumodcore.api.misc.AtumColor;
import me.phoenixra.atumodcore.api.tuples.PairRecord;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
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


    public static void drawRect(int posX, int posY, int width, int height, AtumColor color) {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        //draw
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        color.useColor();
        GlStateManager.disableDepth();
        buffer.begin(7, DefaultVertexFormats.POSITION);
        buffer.pos(posX,
                (double) posY + height,
                0
        ).endVertex();
        buffer.pos((double) posX + width,
                (double) posY + height,
                0
        ).endVertex();
        buffer.pos((double) posX + width,
                posY,
                0
        ).endVertex();
        buffer.pos(posX,
                posY,
                0
        ).endVertex();
        tessellator.draw();

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.enableDepth();
        GlStateManager.color(1f, 1f, 1f);
    }

    public static void drawOutline(int x, int y, int width, int height, AtumColor color) {
        drawRect( x, y, width, 1, color);
        drawRect(x, y+1, 1, height-2, color);
        drawRect(x + width - 1, y+1, 1, height-2, color);
        drawRect( x, y + height - 1, width, 1, color);
    }
    public static void drawDashedOutline(int x, int y, int width, int height, AtumColor color) {
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.disableTexture2D();
        color.useColor();
        glTranslatef(x, y, 0);
        glEnable(GL_LINE_STIPPLE);
        glLineStipple(1, (short) 0x00FF);
        glLineWidth(4f);
        glBegin(GL_LINE_STRIP);

        glVertex3f(0, 0, 0);
        glVertex3f(0, height, 0);
        glVertex3f(width, height, 0);
        glVertex3f(width, 0, 0);
        glVertex3f(0, 0, 0);
        glEnd();
        glLineWidth(1f);
        glDisable(GL_LINE_STIPPLE);
        glTranslatef(-x, -y, 0);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.enableDepth();
        GlStateManager.color(1f, 1f, 1f);
    }



    public static void drawCustomBar(int x, int y, int width, int height,
                                     double value,
                                     AtumColor colorBarLight,
                                     AtumColor colorBarDark,
                                     boolean outlined) {
        drawCustomBar(x, y, width, height, value, AtumColor.WHITE, AtumColor.WHITE, colorBarLight, colorBarDark, outlined, AtumColor.BLACK);
    }


    public static void drawCustomBar(int x, int y, int width, int height,
                                     double value,
                                     AtumColor colorGroundLight,
                                     AtumColor colorGroundDark,
                                     AtumColor colorBarLight,
                                     AtumColor colorBarDark) {
        drawCustomBar(x, y, width, height, value, colorGroundLight, colorGroundDark, colorBarLight, colorBarDark, true, AtumColor.BLACK);
    }


    public static void drawCustomBar(int x, int y, int width, int height,
                                     double value,
                                     AtumColor colorGroundLight,
                                     AtumColor colorGroundDark,
                                     AtumColor colorBarLight,
                                     AtumColor colorBarDark,
                                     boolean outlined) {
        drawCustomBar(x, y, width, height, value, colorGroundLight, colorGroundDark, colorBarLight, colorBarDark, outlined, AtumColor.BLACK);
    }


    public static void drawCustomBar(int x, int y, int width, int height,
                                     double value,
                                     AtumColor colorGroundLight,
                                     AtumColor colorGroundDark,
                                     AtumColor colorBarLight,
                                     AtumColor colorBarDark,
                                     AtumColor colorOutline) {
        drawCustomBar(x, y, width, height, value, colorGroundLight, colorGroundDark, colorBarLight, colorBarDark, true, colorOutline);
    }


    public static void drawCustomBar(int x, int y, int width, int height,
                                     double value,
                                     AtumColor colorGroundLight,
                                     AtumColor colorGroundDark,
                                     AtumColor colorBarLight,
                                     AtumColor colorBarDark,
                                     boolean outlined,
                                     AtumColor colorOutline) {
        if (value < 0.0D) {
            value = 0.0D;
        }else if (value > 100D) {
            value = 100D;
        }

        int offset = 1;

        int filledWidth = width;
        filledWidth = width - (offset * 2);
        if (filledWidth < 0)
            filledWidth = 0;
        int filledHeight = width;
        filledHeight = height - (offset * 2);
        if (filledHeight < 0)
            filledHeight = 0;

        int percentFilled = (int) Math.round(value / 100.0D * filledWidth);

        if (outlined)
            drawOutline(x, y, width, height, colorOutline);
        int halfedFilledHeight = filledHeight / 2;

        drawRect(x + offset, y + offset, percentFilled, halfedFilledHeight, colorBarLight);
        drawRect(x + offset, y + offset + halfedFilledHeight, percentFilled, filledHeight - halfedFilledHeight, colorBarDark);

        if (colorGroundDark != null && colorGroundLight != null && filledWidth - percentFilled > 0) {
            drawRect(x + offset + percentFilled, y + offset, filledWidth - percentFilled, halfedFilledHeight, colorGroundLight);
            drawRect(x + offset + percentFilled, y + offset + halfedFilledHeight, filledWidth - percentFilled, filledHeight - halfedFilledHeight, colorGroundDark);
        }
    }

    public static void drawCompleteImage(int posX, int posY, int width, int height)
    {
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
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
        glTranslatef(-posX, -posY, 0);
        GlStateManager.disableBlend();
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
        double imageWidth = glGetTexLevelParameteri(GL_TEXTURE_2D, 0, GL_TEXTURE_WIDTH);
        double imageHeight = glGetTexLevelParameteri(GL_TEXTURE_2D, 0, GL_TEXTURE_HEIGHT);

        double einsTeilerWidth = 1F / imageWidth;
        double uvWidth = einsTeilerWidth * imagePartWidth;
        double uvX = einsTeilerWidth * imageX;

        double einsTeilerHeight = 1F / imageHeight;
        double uvHeight = einsTeilerHeight * imagePartHeight;
        double uvY = einsTeilerHeight * imageY;

        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
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
        glTranslatef(-posX, -posY, 0);
        GlStateManager.disableBlend();

    }



    public static int[] fixCoordinates(int x, int y, int width, int height, boolean fixRatio) {


        double scaleX = (scaleFactorCache*0.9925/windowRatioXCache);
        double scaleY = (scaleFactorCache/windowRatioYCache);
        if(fixRatio){
            if(scaleX > scaleY){
                boolean b = y > Display.getHeight()/2;
                int heightDifference = (int) ((height / scaleX) - height/scaleY)/2;
                return new int[]{
                        (int) (x / scaleX),
                        (int) (y / scaleY) - (b ? heightDifference : -heightDifference),
                        (int) (width / scaleX),
                        (int) (height / scaleX)
                };
            }else if(scaleX <= scaleY){
                boolean b = x > Display.getWidth()/2;
                int widthDifference = (int) ((width / scaleY) - width/scaleX)/2;
                return new int[]{
                        (int) (x / scaleX) - (b ? widthDifference : -widthDifference),
                        (int) (y / scaleY),
                        (int) (width / scaleY),
                        (int) (height / scaleY)
                };
            }
        }
        return new int[]{
                MathHelper.ceil (x / scaleX),
                MathHelper.ceil  (y / scaleY),
                MathHelper.ceil  (width / scaleX),
                MathHelper.ceil (height / scaleY)
        };
    }
    public static int[] fixCoordinates(int x, int y) {
        double scaleX = (scaleFactorCache/windowRatioXCache);
        double scaleY = (scaleFactorCache/windowRatioYCache);

        return new int[]{
                (int) (x / scaleX),
                (int) (y / scaleY)
        };
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
