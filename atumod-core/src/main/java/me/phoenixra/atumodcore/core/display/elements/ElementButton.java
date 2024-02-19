package me.phoenixra.atumodcore.core.display.elements;

import me.phoenixra.atumconfig.api.placeholders.context.PlaceholderContext;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumconfig.api.config.Config;
import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.actions.ActionArgs;
import me.phoenixra.atumodcore.api.display.actions.ActionData;
import me.phoenixra.atumodcore.api.display.actions.DisplayAction;
import me.phoenixra.atumodcore.api.display.annotations.RegisterDisplayElement;
import me.phoenixra.atumodcore.api.display.impl.BaseElement;
import me.phoenixra.atumodcore.api.display.misc.DisplayResolution;
import me.phoenixra.atumodcore.api.events.display.ElementInputPressEvent;
import me.phoenixra.atumodcore.api.events.display.ElementInputReleaseEvent;
import me.phoenixra.atumodcore.api.input.InputType;
import me.phoenixra.atumconfig.api.tuples.Pair;
import me.phoenixra.atumodcore.api.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Default button.
 *
 * Settings:
 *  <ul>
 *      <li>image - the image to display</li>
 *      <li>textureX - the x position of the texture</li>
 *      <li>textureY - the y position of the texture</li>
 *      <li>textureWidth - the width of the texture</li>
 *      <li>textureHeight - the height of the texture</li>
 *      <li>brightness - the brightness of the image</li>
 *      <li>brightness-onHover - the brightness of the image when hovered</li>
 *      <li>brightness-onClick - the brightness of the image when clicked</li>
 *      <li>actions-onPress - the actions to perform when pressed</li>
 *      <li>actions-onRelease - the actions to perform when released</li>
 *  </ul>
 *
 *  For brightness use '1.0;1.0;1.0' format.
 */
@RegisterDisplayElement(templateId = "button")
public class ElementButton extends BaseElement {

    private Runnable imageBinder;

    private float[] brightnessDefault = new float[]{1.0f, 1.0f, 1.0f};
    private float[] brightnessOnHover = new float[0];
    private float[] brightnessOnClick = new float[0];
    private int textureX;
    private int textureY;
    private int textureWidth;
    private int textureHeight;

    private List<Pair<DisplayAction, ActionArgs>> actionsOnPress = new ArrayList<>();
    private List<Pair<DisplayAction,ActionArgs>> actionsOnRelease = new ArrayList<>();

    private boolean clicked;

    public ElementButton(@NotNull AtumMod atumMod,
                         @NotNull DisplayCanvas elementOwner) {
        super(atumMod, elementOwner);
    }
    @Override
    protected void onDraw(DisplayResolution resolution, float scaleFactor, int mouseX, int mouseY) {
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
    public void updateElementVariables(@NotNull Config config) {
        TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
        PlaceholderContext context = PlaceholderContext.of(getElementOwner().getDisplayRenderer());

        String image = config.getStringOrNull("image");
        if (image != null) {
            ResourceLocation imageLocation = new ResourceLocation(image);
            this.imageBinder = () -> textureManager.bindTexture(imageLocation);
        }
        String brightness = config.getStringOrNull("brightness");
        if (brightness != null) {
            this.brightnessDefault = new float[]{
                    Float.parseFloat(brightness.split(";")[0]),
                    Float.parseFloat(brightness.split(";")[1]),
                    Float.parseFloat(brightness.split(";")[2])
            };
        }
        brightness = config.getStringOrNull("brightness-onHover");
        if (brightness != null) {
            this.brightnessOnHover = new float[]{
                    Float.parseFloat(brightness.split(";")[0]),
                    Float.parseFloat(brightness.split(";")[1]),
                    Float.parseFloat(brightness.split(";")[2])
            };
        }
        brightness = config.getStringOrNull("brightness-onClick");
        if (brightness != null) {
            this.brightnessOnClick = new float[]{
                    Float.parseFloat(brightness.split(";")[0]),
                    Float.parseFloat(brightness.split(";")[1]),
                    Float.parseFloat(brightness.split(";")[2])
            };
        }
        String textureX = config.getStringOrNull("textureX");
        if (textureX != null) {
            this.textureX = (int) config.getEvaluated(
                    "textureX",
                    context
            );
        }
        String textureY = config.getStringOrNull("textureY");
        if (textureY != null) {
            this.textureY = (int) config.getEvaluated(
                    "textureY",
                    context
            );
        }
        String textureWidth = config.getStringOrNull("textureWidth");
        if (textureWidth != null) {
            this.textureWidth = (int) config.getEvaluated(
                    "textureWidth",
                    context
            );
        }
        String textureHeight = config.getStringOrNull("textureHeight");
        if (textureHeight != null) {
            this.textureHeight = (int) config.getEvaluated(
                    "textureHeight",
                    context
            );
        }
        if (config.hasPath("actions-onPress")) {
            Config actionsOnPress = config.getSubsection("actions-onPress");
            for (String key : actionsOnPress.getKeys(false)) {
                String actionOnPress = actionsOnPress.getStringOrNull(key);
                String[] split = actionOnPress.split("@");
                DisplayAction action = getAtumMod().getDisplayManager().getActionRegistry()
                        .getActionById(split[0]);
                if(action == null){
                    getAtumMod().getLogger().error("Could not find action: " + split[0]);
                    continue;
                }
                ActionArgs args = null;
                if (split.length > 1) {
                    args = new ActionArgs(split[1]);
                }
                this.actionsOnPress.add(new Pair<>(action, args));
            }
        }
        if (config.hasPath("actions-onRelease")) {
            Config actionsOnRelease = config.getSubsection("actions-onRelease");
            for (String key : actionsOnRelease.getKeys(false)) {
                String actionOnRelease = actionsOnRelease.getStringOrNull(key);
                String[] split = actionOnRelease.split("@");
                DisplayAction action = getAtumMod().getDisplayManager().getActionRegistry()
                        .getActionById(split[0]);
                if(action == null){
                    getAtumMod().getLogger().error("Could not find action: " + split[0]);
                    continue;
                }
                ActionArgs args = null;
                if (split.length > 1) {
                    args = new ActionArgs(split[1]);
                }
                this.actionsOnRelease.add(new Pair<>(action, args));
            }
        }
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
                                .actionArgs(it.getSecond())
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
                                .actionArgs(it.getSecond())
                                .build()
                ));
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

}
