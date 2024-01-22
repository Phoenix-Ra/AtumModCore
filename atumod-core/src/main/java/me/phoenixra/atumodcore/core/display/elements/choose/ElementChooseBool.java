package me.phoenixra.atumodcore.core.display.elements.choose;

import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.config.Config;
import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.annotations.RegisterDisplayElement;
import me.phoenixra.atumodcore.api.display.impl.BaseElement;
import me.phoenixra.atumodcore.api.display.misc.DisplayResolution;
import me.phoenixra.atumodcore.api.events.display.ElementInputPressEvent;
import me.phoenixra.atumodcore.api.input.InputType;
import me.phoenixra.atumodcore.api.misc.AtumColor;
import me.phoenixra.atumodcore.api.utils.RenderUtils;
import me.phoenixra.atumodcore.core.display.elements.ElementImage;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
@RegisterDisplayElement(templateId = "choose_bool")
public class ElementChooseBool extends BaseElement {
    private Type displayType = Type.PHONE_LIKE;
    private State state = State.FALSE;
    private String displayDataAttached;


    //IMAGE TYPE
    private ElementImage imageTrue;
    private ElementImage imageFalse;

    //PHONE LIKE TYPE
    private AtumColor circleColor = AtumColor.GRAY;
    private AtumColor rectColorTrue = AtumColor.BLUE;
    private AtumColor rectColorFalse = AtumColor.WHITE;

    private float circleRadiusMultiplier = 0.5f;
    private int circlePolygons = 40;
    private int maxAnimationTicks = 20;



    private int animationTimer = -1;
    private int xPosStep = 0;


    private boolean init;

    public ElementChooseBool(@NotNull AtumMod atumMod,
                             @NotNull DisplayCanvas elementOwner) {
        super(atumMod, elementOwner);
    }
    public ElementChooseBool(@NotNull AtumMod atumMod) {
        super(atumMod, null);
    }

    @Override
    protected void onDraw(DisplayResolution resolution,
                          float scaleFactor, int mouseX, int mouseY) {
        boolean hasData = getElementOwner()
                .getDisplayRenderer().getDisplayData()
                .hasData(displayDataAttached);
        if(!init){
            if(hasData){
                state = State.TRUE;
            }else{
                state = State.FALSE;
            }
            init = true;
        }

        switch (displayType){
            case IMAGE:
                if(hasData){
                    state = State.TRUE;
                    imageTrue.draw(resolution,scaleFactor,mouseX,mouseY);
                }else{
                    state = State.FALSE;
                    imageFalse.draw(resolution,scaleFactor,mouseX,mouseY);
                }
                break;
            case PHONE_LIKE:
                if(animationTimer==-1) {
                    if (hasData && state == State.FALSE) {
                        state = State.FROM_FALSE_TO_TRUE;
                        animationTimer = 0;
                    } else if (!hasData && state == State.TRUE) {
                        state = State.FROM_TRUE_TO_FALSE;
                        animationTimer = 0;
                    }
                    xPosStep = ((getX() + getWidth() - getHeight() / 2)
                            - (getX() + getHeight() / 2)) / maxAnimationTicks;
                }else if(animationTimer>=maxAnimationTicks) {
                    animationTimer = -1;
                    if (hasData) {
                        state = State.TRUE;
                    } else {
                        state = State.FALSE;
                    }
                }else {
                    animationTimer++;
                }
                int circleXPos;
                if(animationTimer==-1){
                    if(hasData){
                        circleXPos = getX() + getWidth() - getHeight() / 2;
                    }else{
                        circleXPos = getX() + getHeight() / 2;
                    }
                }else{
                    if(state == State.FROM_FALSE_TO_TRUE){
                        circleXPos = getX() + getHeight() / 2 + xPosStep * animationTimer;
                    }else{
                        circleXPos = getX() + getWidth() - getHeight() / 2 - xPosStep * animationTimer;
                    }
                }
                RenderUtils.drawOutline(
                        getX(),
                        getY(),
                        getWidth(),
                        getHeight(),
                        AtumColor.BLACK
                );
                if(hasData){
                    RenderUtils.fill(
                            getX(),
                            getY(),
                            getX()+getWidth(),
                            getY()+getHeight(),
                            rectColorTrue.toInt(),
                            1.0f
                    );
                    if(animationTimer!=-1){
                        RenderUtils.fill(
                                circleXPos,
                                getY(),
                                getX()+getWidth(),
                                getY()+getHeight(),
                                rectColorFalse.toInt(),
                                1.0f
                        );
                    }
                    RenderUtils.drawCircle(
                            circleXPos,
                            getY() + getHeight() / 2.0f,
                            getHeight() * circleRadiusMultiplier,
                            circlePolygons,
                            circleColor
                    );
                }else{
                    RenderUtils.fill(
                            getX(),
                            getY(),
                            getX()+getWidth(),
                            getY()+getHeight(),
                            rectColorFalse.toInt(),
                            1.0f
                    );
                    if(animationTimer!=-1){
                        RenderUtils.fill(
                                getX(),
                                getY(),
                                circleXPos,
                                getY()+getHeight(),
                                rectColorTrue.toInt(),
                                1.0f
                        );
                    }
                    RenderUtils.drawCircle(
                            circleXPos,
                            getY() + getHeight() / 2.0f,
                            getHeight() * circleRadiusMultiplier,
                            circlePolygons,
                            circleColor
                    );
                }
                break;

        }
    }

    @Override
    public void updateElementVariables(@NotNull Config config,
                                       @Nullable String configKey) {

        displayDataAttached = config.getString("display-data-attached");

        displayType = Type.valueOf(config.getStringOrDefault(
                        "displayType","PHONE_LIKE"
                )
        );


        switch (displayType){
            case IMAGE:
                imageTrue = new ElementImage(getAtumMod(),getElementOwner());
                imageTrue.updateBaseVariables(
                        config,
                        configKey+".settings.imageTrue"
                );
                imageTrue.updateElementVariables(
                        config.getSubsection("imageTrue"),
                        configKey+".settings.imageTrue"
                );

                imageFalse = new ElementImage(getAtumMod(),getElementOwner());
                imageFalse.updateBaseVariables(
                        config,
                        configKey+".settings.imageFalse"
                );
                imageFalse.updateElementVariables(
                        config.getSubsection("imageFalse"),
                        configKey+".settings.imageFalse"
                );

                break;
            case PHONE_LIKE:
                circleColor = AtumColor.fromHex(
                        config.getStringOrDefault(
                                "circleColor",
                                "#4C4C4C"
                        )
                );
                circleRadiusMultiplier = (float) config.getDoubleOrDefault(
                        "circleRadiusMultiplier",0.8f
                );
                circlePolygons = config.getIntOrDefault(
                        "circlePolygons",40
                );
                rectColorFalse = AtumColor.fromHex(
                        config.getStringOrDefault(
                                "rectColorFalse",
                                AtumColor.RED.toHex(false)
                        )
                );
                rectColorTrue = AtumColor.fromHex(
                        config.getStringOrDefault(
                                "rectColorTrue",
                                AtumColor.GREEN.toHex(false)
                        )
                );
                maxAnimationTicks = config.getIntOrDefault(
                        "maxAnimationTicks",7
                );


                break;
        }

    }

    @Override
    protected BaseElement onClone(BaseElement clone) {
        ElementChooseBool cloneImage = (ElementChooseBool) clone;

        cloneImage.circleColor = AtumColor.from(
                this.circleColor.toInt(),false
        );
        if(this.imageTrue != null) {
            cloneImage.imageTrue = (ElementImage) this.imageTrue.cloneWithRandomId();
        }
        if(this.imageFalse != null) {
            cloneImage.imageFalse = (ElementImage) this.imageFalse.cloneWithRandomId();
        }
        return cloneImage;
    }


    @SubscribeEvent
    public void onPressed(ElementInputPressEvent event) {
        if (!isActive()) return;
        if (event.getParentEvent().getType() != InputType.MOUSE_LEFT) return;
        if (event.getClickedElement().equals(this)) {

            if(animationTimer!=-1) return;

            boolean hasData = getElementOwner()
                    .getDisplayRenderer().getDisplayData()
                    .hasData(displayDataAttached);

            if(hasData){
                getElementOwner().getDisplayRenderer()
                        .getDisplayData()
                        .removeData(displayDataAttached);
            }else {
                getElementOwner().getDisplayRenderer()
                        .getDisplayData()
                        .setData(displayDataAttached, "true");
            }
        }
    }

    protected enum State{
        TRUE,
        FALSE,
        FROM_TRUE_TO_FALSE,
        FROM_FALSE_TO_TRUE

    }

    protected enum Type{
        IMAGE,
        PHONE_LIKE
    }
}
