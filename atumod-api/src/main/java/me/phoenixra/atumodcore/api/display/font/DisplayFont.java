package me.phoenixra.atumodcore.api.display.font;

import lombok.Getter;
import me.phoenixra.atumconfig.api.tuples.PairRecord;
import me.phoenixra.atumodcore.api.display.misc.DisplayResolution;
import me.phoenixra.atumodcore.api.misc.AtumColor;
import me.phoenixra.atumodcore.api.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Made by MemesValkyrie for Faint.
 * <p>
 * Just a little fontrenderer for minecraft I wrote. Should work with any font size
 * without any graphical glitches, but because of this setup takes forever. Feel free to make any edits.
 * <p>
 * Created by Zeb on 12/19/2016.
 */
public class DisplayFont {

    /**
     * The font to be drawn.
     */
    @Getter
    private Font font;
    private int characterCount;

    /**
     * If fractional metrics should be used in the font renderer.
     */
    private boolean fractionalMetrics = false;

    private Map<Integer,FontData> fontData;

    /**
     * All the color codes used in minecraft.
     */
    private int[] colorCodes = new int[32];

    /**
     * The margin on each texture.
     */
    private static final int MARGIN = 2;

    /**
     * The character that invokes color in a string when rendered.
     */
    private static final char COLOR_INVOKER = '\247';

    /**
     * The random offset in obfuscated text.
     */
    private static int RANDOM_OFFSET = 1;

    private final List<Runnable> afterLoad = new ArrayList<>();

    public DisplayFont(Font font) {
        this(font, 256);
    }

    public DisplayFont(Font font, int characterCount) {
        this(font, characterCount, true);
    }

    public DisplayFont(Font font, boolean fractionalMetrics) {
        this(font, 256, fractionalMetrics);
    }

    public DisplayFont(Font font, int characterCount, boolean fractionalMetrics) {
        this.font = font;
        this.fractionalMetrics = fractionalMetrics;
        this.characterCount = characterCount;
        fontData = new HashMap<>();
        // Generates all the character textures.
        setup(font.getSize(),characterCount, Font.PLAIN);
        setup(font.getSize(),characterCount, Font.BOLD);
        setup(font.getSize(),characterCount, Font.ITALIC);
    }

    public void loadFontSize(int fontSize){
        if(fontData.get(fontSize) != null) return;
        setup(fontSize,characterCount, Font.PLAIN);
        setup(fontSize,characterCount, Font.BOLD);
        setup(fontSize,characterCount, Font.ITALIC);
    }
    public boolean isLoadingSize(int fontSize){
        FontData fontData = this.fontData.get(fontSize);
        if(fontData == null) return false;
        return !fontData.regularDataState.get()
                || !fontData.boldDataState.get()
                || !fontData.italicDataState.get();
    }
    /**
     * Sets up the character data and textures.
     *
     * @param fontSize       The size of the font.
     * @param characterCount The array of character data that should be filled.
     * @param type           The font type. (Regular, Bold, and Italics)
     */
    private void setup(int fontSize, int characterCount, int type) {
        if(this.fontData.get(fontSize) == null){
            this.fontData.put(fontSize, new FontData());
        }
        FontData fontData = this.fontData.get(fontSize);
        Map<Character, DisplayFont.CharacterData> characterData;
        if(type == Font.PLAIN){
            characterData = fontData.regularData;
        }else if(type == Font.BOLD){
            characterData = fontData.boldData;
        }else if(type == Font.ITALIC){
            characterData = fontData.italicsData;
        }else{
            throw new IllegalArgumentException("Invalid font type: " + type);
        }
        // Quickly generates the colors.
        generateColors();

        // Changes the type of the font to the given type.
        Font font = this.font.deriveFont(type,fontSize);

        // An image just to get font data.
        BufferedImage utilityImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);

        // The graphics of the utility image.
        Graphics2D utilityGraphics = (Graphics2D) utilityImage.getGraphics();

        // Sets the font of the utility image to the font.
        utilityGraphics.setFont(font);

        // The font metrics of the utility image.
        FontMetrics fontMetrics = utilityGraphics.getFontMetrics();

        // Iterates through all the characters in the character set of the font renderer.
        AtomicInteger added = new AtomicInteger();
        for (int index = 0; index < characterCount; index++) {
            int finalIndex = index;
            new Thread(()-> {
                // The character at the current index.
                char character = (char) finalIndex;

                // The width and height of the character according to the font.
                Rectangle2D characterBounds = fontMetrics.getStringBounds(String.valueOf(character), utilityGraphics);

                // The width of the character texture.
                float width = (float) characterBounds.getWidth() + (2 * MARGIN);

                // The height of the character texture.
                float height = (float) characterBounds.getHeight();
                // The image that the character will be rendered to.
                BufferedImage characterImage = new BufferedImage(MathHelper.ceil(width), MathHelper.ceil(height), BufferedImage.TYPE_INT_ARGB);

                // The graphics of the character image.
                Graphics2D graphics = (Graphics2D) characterImage.getGraphics();

                // Sets the font to the input font/
                graphics.setFont(font);

                // Sets the color to white with no alpha.
                graphics.setColor(new Color(255, 255, 255, 0));

                // Fills the entire image with the color above, makes it transparent.
                graphics.fillRect(0, 0, characterImage.getWidth(), characterImage.getHeight());

                // Sets the color to white to draw the character.
                graphics.setColor(Color.WHITE);

                // Enables anti-aliasing so the font doesn't have aliasing.
                graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, this.fractionalMetrics ? RenderingHints.VALUE_FRACTIONALMETRICS_ON : RenderingHints.VALUE_FRACTIONALMETRICS_OFF);

                // Draws the character.
                graphics.drawString(String.valueOf(character), MARGIN, fontMetrics.getAscent());

                // Array of all the colors in the image.
                int[] pixels = new int[characterImage.getWidth() * characterImage.getHeight()];

                // Fetches all the colors in the image.
                characterImage.getRGB(0, 0, characterImage.getWidth(), characterImage.getHeight(), pixels, 0, characterImage.getWidth());

                // Buffer that will store the texture data.
                ByteBuffer buffer = BufferUtils.createByteBuffer(characterImage.getWidth() * characterImage.getHeight() * 4); //4 for RGBA, 3 for RGB

                // Puts all the pixel data into the buffer.
                for (int y = 0; y < characterImage.getHeight(); y++) {
                    for (int x = 0; x < characterImage.getWidth(); x++) {

                        // The pixel in the image.
                        int pixel = pixels[y * characterImage.getWidth() + x];

                        // Puts the data into the byte buffer.
                        buffer.put((byte)((pixel >> 16) & 0xFF));
                        buffer.put((byte)((pixel >> 8) & 0xFF));
                        buffer.put((byte)(pixel & 0xFF));
                        buffer.put((byte)((pixel >> 24) & 0xFF));
                    }
                }

                // Flips the byte buffer, not sure why this is needed.
                buffer.flip();
                Minecraft.getMinecraft().addScheduledTask(()->{
                    // Generates a new texture id.
                    int textureId = GlStateManager.generateTexture();
                    // Allocates the texture in opengl.
                    createTexture(textureId, characterImage, buffer);
                    characterData.put((char)finalIndex,new CharacterData(character, characterImage.getWidth(), characterImage.getHeight(), textureId));
                    if (added.incrementAndGet() >= characterCount - 1){
                        if(type == Font.PLAIN){
                            fontData.regularDataState.set(true);
                        }else if(type == Font.BOLD){
                            fontData.boldDataState.set(true);
                        }else {
                            fontData.italicDataState.set(true);
                        }
                        if(fontData.regularDataState.get() && fontData.boldDataState.get() && fontData.italicDataState.get()){
                            synchronized(afterLoad) {
                                if (!afterLoad.isEmpty()) {
                                    afterLoad.forEach(Runnable::run);
                                    afterLoad.clear();
                                }
                            }
                        }
                    }
                });
            }).start();
         }

        // Returns the filled character data array.
    }

    /**
     * Uploads the opengl texture.
     *
     * @param textureId The texture id to upload to.
     * @param image     The image to upload.
     */
    private void createTexture(int textureId, BufferedImage image, ByteBuffer buffer) {

        // Binds the opengl texture by the texture id.
        GlStateManager.bindTexture(textureId);

        // Sets the texture parameter stuff.
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

        // Uploads the texture to opengl.
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, image.getWidth(), image.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);

        // Binds the opengl texture 0.
        GlStateManager.bindTexture(0);
    }

    /**
     * Renders the given string.
     *
     * @param text     The text to be rendered.
     * @param x        The x position of the text.
     * @param y        The y position of the text.
     * @param color    The color of the text.
     * @param fontSize The size of a font to draw
     */
    public void drawString(String text, float x, float y,
                           AtumColor color,
                           int fontSize) {
        if(!isLoadedSize(fontSize)) return;
        renderString(text, x, y, color, false, fontSize);
    }

    /**
     * Renders the given string.
     *
     * @param text     The text to be rendered.
     * @param x        The x position of the text.
     * @param y        The y position of the text.
     * @param color    The color of the text.
     * @param fontSize The size of a font to draw
     */
    public void drawStringWithShadow(String text,
                                     float x, float y,
                                     AtumColor color,
                                     int fontSize) {
        if(!isLoadedSize(fontSize)) return;
        GL11.glTranslated(0.5, 0.5, 0);
        renderString(text, x, y, color, true, fontSize);
        GL11.glTranslated(-0.5, -0.5, 0);
        renderString(text, x, y, color, false, fontSize);
    }

    /**
     * Renders the given string.
     *
     * @param text   The text to be rendered.
     * @param x      The x position of the text.
     * @param y      The y position of the text.
     * @param shadow If the text should be rendered with the shadow color.
     * @param color  The color of the text.
     * @param fontSize The size of a font to draw
     */
    private void renderString(String text, float x, float y,
                              AtumColor color, boolean shadow,
                              int fontSize) {
        // Returns if the text is empty.
        if (text.length() == 0) return;

        DisplayResolution resolution = DisplayResolution.getCurrentResolution();
        int MARGIN = resolution==DisplayResolution._4x3?1 : DisplayFont.MARGIN;
        // Pushes the matrix to store gl values.
        GL11.glPushMatrix();

        // Scales down to make the font look better.
        GlStateManager.scale(0.5, 0.5, 1);

        // Removes half the margin to render in the right spot.
        x -= MARGIN / 2.0f;
        y -= MARGIN / 2.0f;

        // Adds 0.5 to x and y.
        x += 0.5F;
        y += 0.5F;

        // Doubles the position because of the scaling.
        x *= 2;
        y *= 2;

        // The character texture set to be used. (Regular by default)
        Map<Character,CharacterData> characterData = fontData.get(fontSize).regularData;

        // Booleans to handle the style.
        boolean underlined = false;
        boolean strikethrough = false;
        boolean obfuscated = false;

        // The length of the text used for the draw loop.
        int length = text.length();

        // The multiplier.
        float multiplier = (shadow ? 4 : 1);

        float a = color.getAlpha();
        float r = color.getRed();
        float g = color.getGreen();
        float b = color.getBlue();

        GlStateManager.color(r / multiplier, g / multiplier, b / multiplier, a);

        // Loops through the text.
        for (int i = 0; i < length; i++) {
            // The character at the index of 'i'.
            char character = text.charAt(i);

            // The previous character.
            char previous = i > 0 ? text.charAt(i - 1) : '.';

            // Continues if the previous color was the color invoker.
            if (previous == COLOR_INVOKER) continue;

            // Sets the color if the character is the color invoker and the character index is less than the length.
            if (character == COLOR_INVOKER) {

                // The color index of the character after the current character.
                int index = "0123456789abcdefklmnor".indexOf(text.toLowerCase(Locale.ENGLISH).charAt(i + 1));

                // If the color character index is of the normal color invoking characters.
                if (index < 16) {
                    // Resets all the styles.
                    obfuscated = false;
                    strikethrough = false;
                    underlined = false;

                    // Sets the character data to the regular type.
                    characterData = fontData.get(fontSize).regularData;

                    // Clamps the index just to be safe in case an odd character somehow gets in here.
                    if (index < 0) index = 15;

                    // Adds 16 to the color index to get the darker shadow color.
                    if (shadow) index += 16;

                    // Gets the text color from the color codes array.
                    int textColor = this.colorCodes[index];

                    // Sets the current color.
                    GL11.glColor4d((textColor >> 16) / 255d, (textColor >> 8 & 255) / 255d, (textColor & 255) / 255d, a);
                } else if (index == 16)
                    obfuscated = true;
                else if (index == 17)
                    // Sets the character data to the bold type.
                    characterData = fontData.get(fontSize).boldData;
                else if (index == 18)
                    strikethrough = true;
                else if (index == 19)
                    underlined = true;
                else if (index == 20)
                    // Sets the character data to the italics type.
                    characterData = fontData.get(fontSize).italicsData;
                else if (index == 21) {
                    // Resets the style.
                    obfuscated = false;
                    strikethrough = false;
                    underlined = false;

                    // Sets the character data to the regular type.
                    characterData = fontData.get(fontSize).regularData;

                    // Sets the color to white
                    GL11.glColor4d(1 * (shadow ? 0.25 : 1), 1 * (shadow ? 0.25 : 1), 1 * (shadow ? 0.25 : 1), a);
                }
            } else {
                // Continues to not crash!
                if (character > 255) continue;

                // Sets the character to a random char if obfuscated is enabled.
                if (obfuscated)
                    character = (char)(((int) character) + RANDOM_OFFSET);

                // The character data for the given character.
                CharacterData charData = characterData.get(character);
                charData.updateOptimizedSize();

                // Draws the character.
                drawChar(character, characterData, x, y);


                // Draws the strikethrough line if enabled.
                if (strikethrough)
                    drawLine(new Vector2f(0, charData.optimizedHeight / 2f), new Vector2f(charData.optimizedWidth, charData.optimizedHeight / 2f), 3);

                // Draws the underline if enabled.
                if (underlined)
                    drawLine(new Vector2f(0, charData.optimizedHeight - 15), new Vector2f(charData.optimizedWidth, charData.optimizedHeight - 15), 3);

                // Adds to the offset.
                x += charData.optimizedWidth - (2 * MARGIN);
            }
        }

        // Restores previous values.
        GL11.glPopMatrix();

        // Sets the color back to white so no odd rendering problems happen.
        GL11.glColor4d(1, 1, 1, 1);

        GlStateManager.bindTexture(0);
    }

    /**
     * Gets the width of the given text.
     *
     * @param text The text to get the width of.
     * @param fontSize The size of a font to draw
     * @return The width of the given text.
     */
    public int getWidth(String text, int fontSize) {
        if(!isLoadedSize(fontSize)) return -1;
        DisplayResolution resolution = DisplayResolution.getCurrentResolution();
        int MARGIN = resolution==DisplayResolution._4x3?1 : DisplayFont.MARGIN;

        // The width of the string.
        float width = 0;

        // The character texture set to be used. (Regular by default)
        Map<Character,CharacterData> characterData = fontData.get(fontSize).regularData;;

        // The length of the text.
        int length = text.length();

        // Loops through the text.
        for (int i = 0; i < length; i++) {
            // The character at the index of 'i'.
            char character = text.charAt(i);

            // The previous character.
            char previous = i > 0 ? text.charAt(i - 1) : '.';

            // Continues if the previous color was the color invoker.
            if (previous == COLOR_INVOKER) continue;

            // Sets the color if the character is the color invoker and the character index is less than the length.
            if (character == COLOR_INVOKER) {

                // The color index of the character after the current character.
                int index = "0123456789abcdefklmnor".indexOf(text.toLowerCase(Locale.ENGLISH).charAt(i + 1));

                if (index == 17) {
                    // Sets the character data to the bold type.
                    characterData = fontData.get(fontSize).boldData;
                    ;
                }else if (index == 20) {
                    // Sets the character data to the italics type.
                    characterData = fontData.get(fontSize).italicsData;
                    ;
                }else if (index == 21)
                    // Sets the character data to the regular type.
                    characterData = fontData.get(fontSize).regularData;;
            } else {
                // Continues to not crash!
                if (character > 255) continue;

                // The character data for the given character.
                CharacterData charData = characterData.get(character);

                // Adds to the offset.
                width += (charData.optimizedWidth - (2 * MARGIN)) / 2;
            }
        }

        // Returns the width.
        return (int)(width + MARGIN / 2.0f);
    }

    /**
     * Gets the height of the given text.
     *
     * @param text The text to get the height of.
     * @param fontSize The size of a font to draw
     * @return The height of the given text.
     */
    public int getHeight(String text, int fontSize) {
        if(!isLoadedSize(fontSize)) return -1;
        DisplayResolution resolution = DisplayResolution.getCurrentResolution();
        int MARGIN = resolution==DisplayResolution._4x3?1 : DisplayFont.MARGIN;

        // The height of the string.
        float height = 0;

        // The character texture set to be used. (Regular by default)
        Map<Character,CharacterData> characterData = fontData.get(fontSize).regularData;;

        // The length of the text.
        int length = text.length();

        // Loops through the text.
        for (int i = 0; i < length; i++) {
            // The character at the index of 'i'.
            char character = text.charAt(i);

            // The previous character.
            char previous = i > 0 ? text.charAt(i - 1) : '.';

            // Continues if the previous color was the color invoker.
            if (previous == COLOR_INVOKER) continue;

            // Sets the color if the character is the color invoker and the character index is less than the length.
            if (character == COLOR_INVOKER) {

                // The color index of the character after the current character.
                int index = "0123456789abcdefklmnor".indexOf(text.toLowerCase(Locale.ENGLISH).charAt(i + 1));

                if (index == 17) {
                    // Sets the character data to the bold type.
                    characterData = fontData.get(fontSize).boldData;
                    ;
                }else if (index == 20) {
                    // Sets the character data to the italics type.
                    characterData = fontData.get(fontSize).italicsData;
                    ;
                }else if (index == 21)
                    // Sets the character data to the regular type.
                    characterData = fontData.get(fontSize).regularData;;
            } else {
                // Continues to not crash!
                if (character > 255) continue;

                // The character data for the given character.
                CharacterData charData = characterData.get(character);

                // Sets the height if its bigger.
                height = Math.max(height, charData.optimizedHeight);
            }
        }

        // Returns the height.
        return (int)(height / 2.0f - MARGIN / 2.0f);
    }

    /**
     * Draws the character.
     *
     * @param character     The character to be drawn.
     * @param characterData The character texture set to be used.
     */
    private void drawChar(char character, Map<Character,CharacterData> characterData, float x, float y) {
        // The char data that stores the character data.
        CharacterData charData = characterData.get(character);

        charData.bind();
        RenderUtils.drawPartialImage(
                (int) x, (int) y,
                charData.optimizedWidth, charData.optimizedHeight,
                0,0,
                charData.width,  charData.height
        );

        // Binds the opengl texture by the texture id.
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }

    /**
     * Draws a line from start to end with the given width.
     *
     * @param start The starting point of the line.
     * @param end   The ending point of the line.
     * @param width The thickness of the line.
     */
    private void drawLine(Vector2f start, Vector2f end, float width) {
        // Disables textures so we can draw a solid line.
        GL11.glDisable(GL11.GL_TEXTURE_2D);

        // Sets the width.
        GL11.glLineWidth(width);

        // Begins drawing the line.
        GL11.glBegin(GL11.GL_LINES); {
            GL11.glVertex2f(start.x, start.y);
            GL11.glVertex2f(end.x, end.y);
        }
        // Ends drawing the line.
        GL11.glEnd();

        // Enables texturing back on.
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    /**
     * Generates all the colors.
     */
    private void generateColors() {
        // Iterates through 32 colors.
        for (int i = 0; i < 32; i++) {
            // Not sure what this variable is.
            int thingy = (i >> 3 & 1) * 85;

            // The red value of the color.
            int red = (i >> 2 & 1) * 170 + thingy;

            // The green value of the color.
            int green = (i >> 1 & 1) * 170 + thingy;

            // The blue value of the color.
            int blue = (i & 1) * 170 + thingy;

            // Increments the red by 85, not sure why does this in minecraft's font renderer.
            if (i == 6) red += 85;

            // Used to make the shadow darker.
            if (i >= 16) {
                red /= 4;
                green /= 4;
                blue /= 4;
            }

            // Sets the color in the color code at the index of 'i'.
            this.colorCodes[i] = (red & 255) << 16 | (green & 255) << 8 | blue & 255;
        }
    }

    public boolean isLoadedSize(int fontSize){
        FontData fontData = this.fontData.get(fontSize);
        if(fontData == null) return false;
        return fontData.regularDataState.get()
                && fontData.boldDataState.get()
                && fontData.italicDataState.get();
    }

    public void addAfterLoad(Runnable runnable){
        afterLoad.add(runnable);
    }

    /**
     * Class that holds the data for each character.
     */
    protected static class CharacterData {

        /**
         * The character the data belongs to.
         */
        public char character;

        /**
         * The width of the character.
         */
        private int width;

        /**
         * The height of the character.
         */
        private int height;

        /**
         * The id of the character texture.
         */
        private int textureId;

        protected int optimizedWidth;
        protected int optimizedHeight;

        public CharacterData(char character, int width, int height, int textureId) {
            this.character = character;
            this.width = width;
            this.height = height;
            this.textureId = textureId;
            updateOptimizedSize();
        }

        /**
         * Binds the texture.
         */
        public void bind() {
            // Binds the opengl texture by the texture id.
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
        }

        private void updateOptimizedSize(){
            int[] arr = RenderUtils.fixCoordinates(0,0, width, height,false);
            optimizedWidth = arr[2]*3;
            optimizedHeight = arr[3]*3;
        }
    }

}
