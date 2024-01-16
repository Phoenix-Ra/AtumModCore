package me.phoenixra.atumodcore.core.display.misc;

import me.phoenixra.atumodcore.api.AtumAPI;
import me.phoenixra.atumodcore.api.display.misc.DisplayResolution;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.math.MathHelper;


public class GuiOptionsExtended extends GuiOptions {
    private int windowSizeType;
    public GuiOptionsExtended(GuiScreen guiScreen, GameSettings gameSettings) {
        super(guiScreen, gameSettings);
        windowSizeType = DisplayResolution.getCurrentResolution() == DisplayResolution.UNRECOGNIZED ?
         0 : DisplayResolution.getCurrentResolution().getIndex();
    }

    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.add(new WindowSizeSlider(

                2222,
                this.width / 2 - 155,
                this.height / 6 + 144 - 6,
                0,
                DisplayResolution.values().length-2)
        );
    }


    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        try {
            DisplayResolution.changeResolution(windowSizeType);
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


        public WindowSizeSlider(int buttonId, int x, int y)
        {
            this(buttonId, x, y, 0.0F, 1.0F);
        }

        public WindowSizeSlider(int buttonId, int x, int y, float minValueIn, float maxValue)
        {
            super(buttonId, x, y, 150, 20, "");

            this.minValue = minValueIn;
            this.maxValue = maxValue;
            this.sliderValue = normalizeValue(windowSizeType);
            DisplayResolution resolution = DisplayResolution.from(windowSizeType);
            this.displayString = "Resolution: "+ resolution.getWidth()
                    +"x"+resolution.getHeight();
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
                    DisplayResolution resolution = DisplayResolution.from(windowSizeType);
                    this.displayString = "Resolution: "+ resolution.getWidth()
                            +"x"+resolution.getHeight();
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
                DisplayResolution resolution = DisplayResolution.from(windowSizeType);
                this.displayString = "Resolution: "+ resolution.getWidth()
                        +"x"+resolution.getHeight();
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
