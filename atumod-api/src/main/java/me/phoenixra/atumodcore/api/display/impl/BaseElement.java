package me.phoenixra.atumodcore.api.display.impl;

import lombok.Getter;
import lombok.Setter;
import me.phoenixra.atumconfig.api.placeholders.context.PlaceholderContext;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumconfig.api.config.Config;
import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.DisplayElement;
import me.phoenixra.atumodcore.api.display.DisplayLayer;
import me.phoenixra.atumodcore.api.display.actions.ActionData;
import me.phoenixra.atumodcore.api.display.actions.DisplayAction;
import me.phoenixra.atumodcore.api.display.annotations.RegisterOptimizedVariable;
import me.phoenixra.atumodcore.api.display.misc.DisplayResolution;
import me.phoenixra.atumodcore.api.display.misc.OptimizedVariable;
import me.phoenixra.atumodcore.api.display.misc.variables.OptimizedVariableInt;
import me.phoenixra.atumodcore.api.misc.AtumColor;
import me.phoenixra.atumodcore.api.utils.ClassUtils;
import me.phoenixra.atumodcore.api.utils.RenderUtils;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class BaseElement implements DisplayElement, Cloneable {
    @Getter
    private String id = UUID.randomUUID().toString(); //default id before loading from config
    @Getter
    private String configKey = null;
    @Getter @Setter
    private String templateId = null;
    @Getter
    private final AtumMod atumMod;
    @Getter
    private DisplayLayer layer;

    @Getter @Setter @RegisterOptimizedVariable
    private OptimizedVariableInt originX;
    @Getter @Setter @RegisterOptimizedVariable
    private OptimizedVariableInt originY;
    @Getter @Setter @RegisterOptimizedVariable
    private OptimizedVariableInt originWidth;
    @Getter @Setter @RegisterOptimizedVariable
    private OptimizedVariableInt originHeight;

    @Getter @Setter
    private int additionX;
    @Getter @Setter
    private int additionY;
    @Getter @Setter
    private int additionWidth;
    @Getter @Setter
    private int additionHeight;

    @Getter
    private int x;
    @Getter
    private int y;
    @Getter
    private int width;
    @Getter
    private int height;

    @Getter
    private int globalX;
    @Getter
    private int globalY;


    @Getter
    private int lastMouseX;
    @Getter
    private int lastMouseY;

    @Getter
    private boolean fixRatio = false;
    @Getter
    private boolean active = true;

    @Setter
    private boolean outlineSelected;

    protected boolean hasOutline = false;
    private AtumColor outlineColor = AtumColor.WHITE;
    private int outlineSize = 1;

    @Getter @Setter
    private DisplayCanvas elementOwner;

    @Getter
    private Config settingsConfig = null;

    private List<OptimizedVariable<?>> optimizedVariables;
    private final List<Field> optimizedVariableFields = new ArrayList<>();
    private boolean initialized;

    public BaseElement(@NotNull AtumMod atumMod,
                       @NotNull DisplayLayer layer,
                       int x,
                       int y,
                       int width,
                       int height,
                       @Nullable DisplayCanvas elementOwner){
        this.atumMod = atumMod;
        this.layer = layer;
        this.originX = new OptimizedVariableInt("posX",x);
        this.originY = new OptimizedVariableInt("posY",y);
        this.originWidth = new OptimizedVariableInt("width",width);
        this.originHeight = new OptimizedVariableInt("height",height);
        this.x =  x;
        this.y =  y;
        this.width = width;
        this.height =  height;

        this.elementOwner = elementOwner;
    }
    public BaseElement(@NotNull AtumMod atumMod, @Nullable DisplayCanvas elementOwner){
        this(atumMod,DisplayLayer.MIDDLE, 0, 0, 0, 0, elementOwner);
    }


    @Override
    public void draw(@NotNull DisplayResolution resolution, float scaleFactor, int mouseX, int mouseY) {
        if(!initialized){
            MinecraftForge.EVENT_BUS.register(this);
            initialized = true;
        }
        lastMouseX = mouseX;
        lastMouseY = mouseY;
        int[] coords;
        coords = RenderUtils.fixCoordinates(
                originX.getValue(resolution)+additionX,
                originY.getValue(resolution)+additionY,
                originWidth.getValue(resolution)+additionWidth,
                originHeight.getValue(resolution)+additionHeight,
                fixRatio
        );
        x = coords[0];
        y = coords[1];
        width = coords[2];
        height = coords[3];
        globalX = elementOwner == null || elementOwner == this
                ? x : elementOwner.getGlobalX() + x;
        globalY = elementOwner == null || elementOwner == this
                ? y : elementOwner.getGlobalY() + y;
        onDraw(resolution, scaleFactor,mouseX,mouseY);
        if(outlineSelected){
            RenderUtils.drawDashedOutline(
                    getX(),
                    getY(),
                    getWidth(),
                    getHeight(),
                    AtumColor.LIME
            );
        }else if(hasOutline){
            RenderUtils.drawOutline(
                    getX(),
                    getY(),
                    getWidth(),
                    getHeight(),
                    outlineSize,
                    outlineColor
            );
        }
    }
    protected abstract void onDraw(DisplayResolution resolution, float scaleFactor, int mouseX, int mouseY);

    @Override
    public final void updateVariables(@NotNull Config config,
                                      @Nullable String configKey) {
        updateBaseVariables(config, configKey);
        updateElementVariables(config.getSubsection("settings"));
    }

    @Override
    public void updateBaseVariables(@NotNull Config config, @Nullable String configKey) {
        settingsConfig = config;
        this.configKey = configKey;
        if(configKey == null){
            this.id = UUID.randomUUID().toString();
        }else {
            if (elementOwner == null) {
                this.id = configKey;
            } else {
                if (elementOwner.isRootCanvas()) {
                    this.id = configKey;
                } else {
                    this.id = elementOwner.getId() + "#" + configKey;
                }
            }
        }
        String layer = config.getStringOrNull("layer");
        if(layer != null){
            this.layer = DisplayLayer.valueOf(layer.toUpperCase());
        }
        String x = config.getStringOrNull("posX");
        if(x != null){
            this.x = (int) getAtumMod().getApi().evaluate(
                     getAtumMod(),
                    x,
                    PlaceholderContext.of(config)
            );
            this.originX.setDefaultValue(this.x);
        }
        String y = config.getStringOrNull("posY");
        if(y != null){
            this.y = (int) getAtumMod().getApi().evaluate(
                    getAtumMod(),
                    y,
                    PlaceholderContext.of(config)
            );
            this.originY.setDefaultValue(this.y);
        }
        String width = config.getStringOrNull("width");
        if(width != null){
            if(width.equalsIgnoreCase("max")){
                this.width = getElementOwner()==this ?
                        1920 : getElementOwner().getOriginWidth().getDefaultValue();
            }else {
                this.width = (int) getAtumMod().getApi().evaluate(
                        getAtumMod(),
                        width,
                        PlaceholderContext.of(config)
                );
            }
            this.originWidth.setDefaultValue(this.width);
        }
        String height = config.getStringOrNull("height");
        if(height != null){
            if(height.equalsIgnoreCase("max")){
                this.height = getElementOwner()==this ?
                        1080 : getElementOwner().getOriginHeight().getDefaultValue();
            }else {
                this.height = (int) getAtumMod().getApi().evaluate(
                        getAtumMod(),
                        height,
                        PlaceholderContext.of(config)
                );
            }
            this.originHeight.setDefaultValue(this.height);
        }
        Boolean fixRatio = config.getBoolOrNull("fixRatio");
        if(fixRatio != null){
            this.fixRatio = fixRatio;
        }
        if(config.hasPath("outline.color")){
            this.outlineColor = AtumColor.fromHex(config.getString("outline.color"));
            this.outlineSize = config.getIntOrDefault("outline.size",1);
            this.hasOutline = true;
        }
        if(config.hasPath("active")) {
            active = config.getBool("active");
        }
    }


    @Override
    public void onRemove() {
        MinecraftForge.EVENT_BUS.unregister(this);
        //empty to not implement it everywhere
    }

    @Override
    public void applyResolutionOptimizer(@NotNull DisplayResolution resolution,
                                         @NotNull Config config) {
        loadOptimizedVariables();
        try {
            for (OptimizedVariable<?> variable : optimizedVariables) {
                variable.addOptimizedValue(
                        resolution,
                        config.getStringOrNull(variable.getConfigKey())
                );
            }
        }catch (Throwable throwable){
            getAtumMod().getLogger().error("Failed to apply resolution optimizer for element: "+getId(),throwable);
        }
    }

    @Override
    public void performAction(@NotNull String actionId, @NotNull ActionData actionData) {
        DisplayAction action = getAtumMod().getDisplayManager().getActionRegistry()
                .getActionById(actionId);
        if(action == null) return;
        action.perform(actionData);
    }

    @Override
    public boolean isHovered(int mouseX, int mouseY) {
        return getElementOwner().getHoveredElement(mouseX, mouseY) == this;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
        if(getElementOwner().getDisplayRenderer() == null) return;
        getElementOwner().getDisplayRenderer().getDisplayData()
                .setElementEnabled(getId(),active);
    }

    private void loadOptimizedVariables(){
        //get all variables using reflection
        if(optimizedVariables == null){
            optimizedVariables = new ArrayList<>();
            for(Field field : ClassUtils.getAllFields(getClass(),
                    OptimizedVariable.class)){
                if(!field.isAnnotationPresent(RegisterOptimizedVariable.class)) return;
                field.setAccessible(true);
                try {
                    optimizedVariables.add((OptimizedVariable<?>) field.get(this));
                    optimizedVariableFields.add(field);
                } catch (IllegalAccessException e) {
                    getAtumMod().getLogger().error("Failed to load optimized variable: "+field.getName()+" The template: "+ getTemplateId());
                }
            }
        }

    }


    @Override
    public @NotNull DisplayElement cloneWithNewVariables(@NotNull Config config,
                                                         @Nullable String configKey,
                                                         @Nullable DisplayCanvas elementOwner) {
        DisplayElement clone = clone();
        ((BaseElement)clone).templateId = id;
        if(elementOwner != null) {
            clone.setElementOwner(elementOwner);
        }
        clone.updateVariables(config,configKey);
        return clone;
    }

    @Override
    public @NotNull DisplayElement clone() {
        try {
            BaseElement clone = (BaseElement) super.clone();
            clone.initialized = false;
            clone.active = true;
            try {
                loadOptimizedVariables();
                clone.optimizedVariables = new ArrayList<>();
                for (Field field : optimizedVariableFields) {
                    field.setAccessible(true);
                    OptimizedVariable<?> variable = (OptimizedVariable<?>) ((OptimizedVariable<?>) field.get(this)).clone();
                    field.set(clone, variable);
                    clone.optimizedVariables.add(variable);
                }
            }catch (Exception e){
                getAtumMod().getLogger().error("Failed to clone optimized variables for element: "+getTemplateId());
            }

            return onClone(clone);
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
    protected abstract BaseElement onClone(BaseElement clone);



}
