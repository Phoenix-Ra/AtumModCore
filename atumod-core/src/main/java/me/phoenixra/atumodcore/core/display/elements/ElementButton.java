package me.phoenixra.atumodcore.core.display.elements;

import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.config.Config;
import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.actions.ActionData;
import me.phoenixra.atumodcore.api.display.actions.DisplayAction;
import me.phoenixra.atumodcore.api.display.impl.BaseElement;
import me.phoenixra.atumodcore.api.events.display.ElementInputPressEvent;
import me.phoenixra.atumodcore.api.events.display.ElementInputReleaseEvent;
import me.phoenixra.atumodcore.api.input.InputType;
import me.phoenixra.atumodcore.api.placeholders.context.PlaceholderContext;
import me.phoenixra.atumodcore.api.tuples.PairRecord;
import me.phoenixra.atumodcore.api.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ElementButton extends BaseElement {

    private Runnable imageBinder;

    private float[] brightnessDefault = new float[]{1.0f, 1.0f, 1.0f};
    private float[] brightnessOnHover = new float[0];
    private float[] brightnessOnClick = new float[0];
    private int textureX;
    private int textureY;
    private int textureWidth;
    private int textureHeight;

    private List<PairRecord<DisplayAction,String[]>> actionsOnPress = new ArrayList<>();
    private List<PairRecord<DisplayAction,String[]>> actionsOnRelease = new ArrayList<>();

    private boolean clicked;

    public ElementButton(@NotNull AtumMod atumMod,
                         @NotNull DisplayCanvas elementOwner) {
        super(atumMod, elementOwner);
    }

    @Override
    protected void onDraw(float scaleFactor, float scaleX, float scaleY, int mouseX, int mouseY) {
        imageBinder.run();
        if (clicked && brightnessOnClick.length == 3) {
            GlStateManager.color(
                    brightnessOnClick[0],
                    brightnessOnClick[1],
                    brightnessOnClick[2],
                    1.0f
            );
        } else if (isHovered(mouseX, mouseY) && brightnessOnHover.length == 3) {
            GlStateManager.color(
                    brightnessOnHover[0],
                    brightnessOnHover[1],
                    brightnessOnHover[2],
                    1.0f
            );
        } else {
            GlStateManager.color(
                    brightnessDefault[0],
                    brightnessDefault[1],
                    brightnessDefault[2],
                    1.0f
            );
        }
        if (textureHeight == 0 || textureWidth == 0) {
            RenderUtils.drawCompleteImage(
                    getX(),
                    getY(),
                    getWidth(),
                    getHeight()
            );
        } else {
            RenderUtils.drawPartialImage(
                    getX(),
                    getY(),
                    getWidth(),
                    getHeight(),
                    textureX,
                    textureY,
                    textureWidth,
                    textureHeight
            );
        }
    }


    @Override
    public void updateVariables(@NotNull Config config, @Nullable String configKey) {
        super.updateVariables(config, configKey);
        TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
        PlaceholderContext context = PlaceholderContext.of(getElementOwner().getDisplayRenderer());

        String image = config.getStringOrNull("settings.image");
        if (image != null) {
            ResourceLocation imageLocation = new ResourceLocation(image);
            this.imageBinder = () -> textureManager.bindTexture(imageLocation);
        }
        String brightness = config.getStringOrNull("settings.brightness");
        if (brightness != null) {
            this.brightnessDefault = new float[]{
                    Float.parseFloat(brightness.split(";")[0]),
                    Float.parseFloat(brightness.split(";")[1]),
                    Float.parseFloat(brightness.split(";")[2])
            };
        }
        brightness = config.getStringOrNull("settings.brightness-onHover");
        if (brightness != null) {
            this.brightnessOnHover = new float[]{
                    Float.parseFloat(brightness.split(";")[0]),
                    Float.parseFloat(brightness.split(";")[1]),
                    Float.parseFloat(brightness.split(";")[2])
            };
        }
        brightness = config.getStringOrNull("settings.brightness-onClick");
        if (brightness != null) {
            this.brightnessOnClick = new float[]{
                    Float.parseFloat(brightness.split(";")[0]),
                    Float.parseFloat(brightness.split(";")[1]),
                    Float.parseFloat(brightness.split(";")[2])
            };
        }
        String textureX = config.getStringOrNull("settings.textureX");
        if (textureX != null) {
            this.textureX = (int) config.getEvaluated(
                    "settings.textureX",
                    context
            );
        }
        String textureY = config.getStringOrNull("settings.textureY");
        if (textureY != null) {
            this.textureY = (int) config.getEvaluated(
                    "settings.textureY",
                    context
            );
        }
        String textureWidth = config.getStringOrNull("settings.textureWidth");
        if (textureWidth != null) {
            this.textureWidth = (int) config.getEvaluated(
                    "settings.textureWidth",
                    context
            );
        }
        String textureHeight = config.getStringOrNull("settings.textureHeight");
        if (textureHeight != null) {
            this.textureHeight = (int) config.getEvaluated(
                    "settings.textureHeight",
                    context
            );
        }
        if (config.hasPath("settings.actions-onPress")) {
            Config actionsOnPress = config.getSubsection("settings.actions-onPress");
            for (String key : actionsOnPress.getKeys(false)) {
                String actionOnPress = actionsOnPress.getStringOrNull(key);
                String[] split = actionOnPress.split("@");
                DisplayAction action = getAtumMod().getDisplayManager().getActionRegistry()
                        .getActionById(split[0]);
                if(action == null){
                    getAtumMod().getLogger().error("Could not find action: " + split[0]);
                    continue;
                }
                String[] args = new String[0];
                if (split.length > 1) {
                    args = split[1].split(";");
                }
                this.actionsOnPress.add(new PairRecord<>(action, args));
            }
        }
        if (config.hasPath("settings.actions-onRelease")) {
            Config actionsOnRelease = config.getSubsection("settings.actions-onRelease");
            for (String key : actionsOnRelease.getKeys(false)) {
                String actionOnRelease = actionsOnRelease.getStringOrNull(key);
                String[] split = actionOnRelease.split("@");
                DisplayAction action = getAtumMod().getDisplayManager().getActionRegistry()
                        .getActionById(split[0]);
                if(action == null){
                    getAtumMod().getLogger().error("Could not find action: " + split[0]);
                    continue;
                }
                String[] args = new String[0];
                if (split.length > 1) {
                    args = split[1].split(";");
                }
                this.actionsOnRelease.add(new PairRecord<>(action, args));
            }
        }
    }

    @Override
    protected BaseElement onClone(BaseElement clone) {
        ElementButton cloneImage = (ElementButton) clone;
        cloneImage.actionsOnPress = new ArrayList<>(this.actionsOnPress);
        cloneImage.actionsOnRelease = new ArrayList<>(this.actionsOnRelease);
        return cloneImage;
    }

    @SubscribeEvent
    public void onPressed(ElementInputPressEvent event) {
        if (!isActive()) return;
        if (event.getParentEvent().getType() != InputType.MOUSE_LEFT) return;
        if (event.getClickedElement().equals(this)) {
            this.clicked = true;
            if (actionsOnPress != null)
                actionsOnPress.forEach(it->it.getFirst().perform(
                        ActionData.builder().atumMod(getAtumMod()).attachedElement(this)
                                .mouseX(event.getParentEvent().getMouseX())
                                .mouseY(event.getParentEvent().getMouseY())
                                .args(it.getSecond())
                                .build()
                ));
        }
    }

    @SubscribeEvent
    public void onReleased(ElementInputReleaseEvent event) {
        if (!isActive()) return;
        if (event.getParentEvent().getType() != InputType.MOUSE_LEFT) return;
        if (event.getClickedElement().equals(this)) {
            if (!clicked) return;
            clicked = false;
            if (actionsOnRelease != null) {
                actionsOnRelease.forEach(it->it.getFirst().perform(
                        ActionData.builder().atumMod(getAtumMod()).attachedElement(this)
                                .mouseX(event.getParentEvent().getMouseX())
                                .mouseY(event.getParentEvent().getMouseY())
                                .args(it.getSecond())
                                .build()
                ));
            }
        }
    }
}
