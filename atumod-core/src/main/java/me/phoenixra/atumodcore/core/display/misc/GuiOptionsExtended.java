package me.phoenixra.atumodcore.core.display.misc;

import me.phoenixra.atumodcore.api.AtumAPI;
import me.phoenixra.atumodcore.api.display.DisplayManager;
import me.phoenixra.atumodcore.api.tuples.PairRecord;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptionSlider;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import scala.actors.threadpool.Arrays;

import java.util.ArrayList;
import java.util.List;

public class GuiOptionsExtended extends GuiOptions {
    private int windowSizeType;
    public GuiOptionsExtended(GuiScreen guiScreen, GameSettings gameSettings) {
        super(guiScreen, gameSettings);
        /*new PairRecord<>(1024, 728),
            new PairRecord<>(1280, 720),
            new PairRecord<>(1366, 768),
            new PairRecord<>(1600, 900),
            new PairRecord<>(1920, 1080)*/
        windowSizeType = DisplayManager.getCurrentResolutionIndex();
    }

    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.add(new WindowSizeSlider(

                2222,
                this.width / 2 - 155,
                this.height / 6 + 144 - 6,
                0,
                DisplayManager.SUPPORTED_RESOLUTIONS.size()-1)
        );
    }


    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        try {
            AtumAPI.getInstance().getCoreMod()
                    .getDisplayManager().changeResolution(windowSizeType);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private class WindowSizeSlider extends GuiButton
    {
        private float sliderValue;
        public boolean dragging;
        private final float minValue;
        private final float maxValue;

        private List<PairRecord<Integer,Integer>> windowSizes;

        public WindowSizeSlider(int buttonId, int x, int y)
        {
            this(buttonId, x, y, 0.0F, 1.0F);
        }

        public WindowSizeSlider(int buttonId, int x, int y, float minValueIn, float maxValue)
        {
            super(buttonId, x, y, 150, 20, "");
            windowSizes = DisplayManager.SUPPORTED_RESOLUTIONS;
            this.minValue = minValueIn;
            this.maxValue = maxValue;
            this.sliderValue = normalizeValue(windowSizeType);
            this.displayString = "Resolution: "+ windowSizes.get(windowSizeType).getFirst()
                    +"x"+windowSizes.get(windowSizeType).getSecond();
        }

        protected int getHoverState(boolean mouseOver)
        {
            return 0;
        }

        protected void mouseDragged(Minecraft mc, int mouseX, int mouseY)
        {
            if (this.visible)
            {
                if (this.dragging)
                {
                    this.sliderValue = (float)(mouseX - (this.x + 4)) / (float)(this.width - 8);
                    this.sliderValue = MathHelper.clamp(this.sliderValue, 0.0F, 1.0F);
                    float f = denormalizeValue(this.sliderValue);
                    windowSizeType = (int)f;
                    this.sliderValue = normalizeValue(f);
                    this.displayString = "Resolution: "+ windowSizes.get(windowSizeType).getFirst()
                            +"x"+windowSizes.get(windowSizeType).getSecond();
                }

                mc.getTextureManager().bindTexture(BUTTON_TEXTURES);
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                this.drawTexturedModalRect(this.x + (int)(this.sliderValue * (float)(this.width - 8)), this.y, 0, 66, 4, 20);
                this.drawTexturedModalRect(this.x + (int)(this.sliderValue * (float)(this.width - 8)) + 4, this.y, 196, 66, 4, 20);
            }
        }

        public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
        {
            if (super.mousePressed(mc, mouseX, mouseY))
            {
                this.sliderValue = (float)(mouseX - (this.x + 4)) / (float)(this.width - 8);
                this.sliderValue = MathHelper.clamp(this.sliderValue, 0.0F, 1.0F);
                float f = denormalizeValue(this.sliderValue);
                windowSizeType = (int)f;
                this.displayString = "Resolution: "+ windowSizes.get(windowSizeType).getFirst()
                        +"x"+windowSizes.get(windowSizeType).getSecond();
                this.dragging = true;
                return true;
            }
            else
            {
                return false;
            }
        }

        public void mouseReleased(int mouseX, int mouseY)
        {
            this.dragging = false;
        }

        public float normalizeValue(float value)
        {
            return MathHelper.clamp((this.snapToStepClamp(value) - this.minValue) / (this.maxValue - this.minValue), 0.0F, 1.0F);
        }

        public float denormalizeValue(float value)
        {
            return this.snapToStepClamp(this.minValue + (this.maxValue - this.minValue) * MathHelper.clamp(value, 0.0F, 1.0F));
        }

        public float snapToStepClamp(float value)
        {
            value = this.snapToStep(value);
            return MathHelper.clamp(value, this.minValue, this.maxValue);
        }

        private float snapToStep(float value)
        {
            value = (float)Math.round(value);

            return value;
        }
    }
}
