package me.phoenixra.atumodcore.api.utils;

import me.phoenixra.atumodcore.api.misc.AtumColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.image.BufferedImage;

import static org.lwjgl.opengl.GL11.*;

//@TODO check all methods, update to newer openGL version and optimize
public class RenderUtils {
    private static double windowRatioXCache = 1.0;
    private static double windowRatioYCache = 1.0;
    private static int guiScaleCache = 1;
    private static int scaleFactorCache = 1;

    /**
     * Draw a filled rectangle.
     * <p>min and max are the corners of the rectangle.</p>
     * @param minX    coord for upper left corner
     * @param minY    coord for upper left corner
     * @param maxX    coord for lower right corner
     * @param maxY    coord for lower right corner
     * @param color   the color
     * @param opacity the opacity
     */
    public static void fill(float minX, float minY,
                            float maxX, float maxY,
                            int color,
                            float opacity) {

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

    /**
     * Draw a filled circle
     *
     * @param centerX The center X coordinate
     * @param centerY The center Y coordinate
     * @param radius The radius of the circle
     * @param polygons The number of polygons to use to draw the circle.
     *                 In most cases 40 is optimal
     * @param color The color of the circle
     */
    public static void drawCircle(float centerX, float centerY,
                                  float radius,
                                  int polygons,
                                  AtumColor color){
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        color.useColor();
        buffer.begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION);

        // Center vertex of the circle
        buffer.pos(centerX, centerY, 0.0D).endVertex();

        // Define the circle vertices
        for (int i = 0; i <= polygons; ++i) {
            double angle = Math.PI * 2 * i / polygons;
            double x = centerX + Math.sin(angle) * radius;
            double y = centerY + Math.cos(angle) * radius;

            buffer.pos(x, y, 0.0D).endVertex();
        }

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


    /**
     *  Bind a texture to the OpenGL context.
     *
     * @param texture The texture to bind.
     */
    public static void bindTexture(ResourceLocation texture) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
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



    /**
     * Draw a rectangle.
     *
     * @param posX The X position.
     * @param posY The Y position.
     * @param width The width.
     * @param height The height.
     * @param color The color.
     */
    public static void drawRect(int posX, int posY,
                                int width, int height,
                                AtumColor color) {
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

    /**
     * Draw an outline.
     *
     * @param x The X position.
     * @param y The Y position.
     * @param width The width.
     * @param height The height.
     * @param outlineSize The outline size.
     * @param color The color.
     */
    public static void drawOutline(int x, int y,
                                   int width, int height,
                                   int outlineSize,
                                   AtumColor color) {
        x -= outlineSize;
        y -= outlineSize;
        width += outlineSize*2;
        height += outlineSize*2;
        drawRect( x, y, width, outlineSize, color);
        drawRect(x, y+outlineSize, outlineSize, height-(outlineSize*2), color);
        drawRect(x + width - outlineSize, y+outlineSize, outlineSize, height-(outlineSize*2), color);
        drawRect( x, y + height - outlineSize, width, outlineSize, color);
    }
    public static void drawOutline(int x, int y,
                                   int width, int height,
                                   AtumColor color) {
        drawOutline(x, y, width, height, 1, color);
    }

    /**
     * Draw a dashed outline.
     *
     * @param x The X position.
     * @param y The Y position.
     * @param width The width.
     * @param height The height.
     * @param color The color.
     */
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
                                     @NotNull AtumColor colorEmpty,
                                     @NotNull AtumColor colorFilled) {
        if (value < 0.0D) {
            value = 0.0D;
        }else if (value > 100) {
            value = 100;
        }


        int percentFilled = (int) Math.round(value / 100.0D * width);

        drawRect(x, y, percentFilled, height, colorFilled);

        if (width - percentFilled > 0) {
            drawRect(x + percentFilled, y, width - percentFilled, height, colorEmpty);
        }
    }

    /**
     * Draw a complete image.
     *
     * @param posX The X position.
     * @param posY The Y position.
     * @param width The width.
     * @param height The height.
     */
    public static void drawCompleteImage(int posX, int posY,
                                         int width, int height)
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

    /**
     * Draw the specified part of a texture
     *
     * @param posX The X position.
     * @param posY The Y position.
     * @param width The width.
     * @param height The height.
     * @param textureX The X position of the image.
     * @param textureY The Y position of the image.
     * @param texturePartWidth The width of the image part.
     * @param texturePartHeight The height of the image part.
     */
    public static void drawPartialImage(int posX, int posY,
                                        int width, int height,
                                        int textureX, int textureY,
                                        int texturePartWidth,
                                        int texturePartHeight)
    {
        double imageWidth = glGetTexLevelParameteri(GL_TEXTURE_2D, 0, GL_TEXTURE_WIDTH);
        double imageHeight = glGetTexLevelParameteri(GL_TEXTURE_2D, 0, GL_TEXTURE_HEIGHT);

        double einsTeilerWidth = 1F / imageWidth;
        double uvWidth = einsTeilerWidth * texturePartWidth;
        double uvX = einsTeilerWidth * textureX;

        double einsTeilerHeight = 1F / imageHeight;
        double uvHeight = einsTeilerHeight * texturePartHeight;
        double uvY = einsTeilerHeight * textureY;

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
    public static void renderItemIntoGUI(ItemStack stack, int x, int y, float xScale, float yScale)
    {
        renderItemModelIntoGUI(stack, x, y, xScale,yScale, Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(stack, null, null));
    }
    protected static void renderItemModelIntoGUI(ItemStack stack, int x, int y, float xScale, float yScale, IBakedModel bakedmodel)
    {
        GlStateManager.pushMatrix();
        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        Minecraft.getMinecraft().getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        setupGuiTransform(x, y, xScale, yScale, bakedmodel.isGui3d());
        bakedmodel = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(bakedmodel, ItemCameraTransforms.TransformType.GUI, false);
        Minecraft.getMinecraft().getRenderItem().renderItem(stack, bakedmodel);
        GlStateManager.disableAlpha();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableLighting();
        GlStateManager.popMatrix();
    }
    private static void setupGuiTransform(int xPosition, int yPosition, float xScale, float yScale, boolean isGui3d)
    {
        GlStateManager.translate((float)xPosition, (float)yPosition, 100.0F + Minecraft.getMinecraft().getRenderItem().zLevel);
        GlStateManager.scale(1.0F, -1.0F, 1.0F);
        GlStateManager.scale(xScale, yScale, 16.0F);

        if (isGui3d)
        {
            GlStateManager.enableLighting();
        }
        else
        {
            GlStateManager.disableLighting();
        }
    }

    /**
     * Get the mouse X and Y positions.
     * Attention! This method uses cached scale factor, so, you should use
     * {@link #getScaleFactor()} before calling this method.
     * @return The mouse X and Y positions.
     */
    public static int[] getMousePos(){
        double scaleX = (scaleFactorCache/windowRatioXCache);
        double scaleY = (scaleFactorCache/windowRatioYCache);

        int width = MathHelper.ceil  (
                Minecraft.getMinecraft().displayWidth / scaleX
        );
        int height = MathHelper.ceil  (
                Minecraft.getMinecraft().displayHeight / scaleY
        );
        int mouseX = Mouse.getX() * width / Minecraft.getMinecraft().displayWidth;
        int mouseZ = height - Mouse.getY() * height / Minecraft.getMinecraft().displayHeight - 1;
        return new int[]{mouseX,mouseZ};
    }

    /**
     * Fix the coordinates to ignore the mc scale factor.
     *
     * <p>Attention! This method uses cached scale factor, so, you should use
     * {@link #getScaleFactor()} before calling this method.</p>
     * @param x The X coordinate.
     * @param y The Y coordinate.
     * @param width The width.
     * @param height The height.
     * @param fixRatio If true, the size will keep the ratio.
     * @return The fixed coordinates.
     */
    public static int[] fixCoordinates(int x, int y,
                                       int width, int height,
                                       boolean fixRatio) {


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
        int outX = x==0
                ? 0 : Math.round((float)( x / scaleX));
        int outY = y==0
                ? 0 : Math.round((float)(y / scaleY));

        return new int[]{
                outX,
                outY,
                Math.round((float)(width / scaleX)),
                Math.round((float)(height / scaleY))
        };

    }

    /**
     * Fix the coordinates to ignore the mc scale factor.
     *
     * <p>Attention! This method uses cached scale factor, so, you should use
     * {@link #getScaleFactor()} before calling this method.</p>
     * @param x The X coordinate.
     * @param y The Y coordinate.
     * @return The fixed coordinates.
     */
    public static int[] fixCoordinates(int x, int y) {
        double scaleX = (scaleFactorCache/windowRatioXCache);
        double scaleY = (scaleFactorCache/windowRatioYCache);

        return new int[]{
                (int) (x / scaleX),
                (int) (y / scaleY)
        };
    }

    /**
     * Get the minecraft scale factor.
     * <p>If the guiScale and window size wasn't changed</p>
     * <p>the method will return the cached value</p>
     *
     * @return The scale factor
     */
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
