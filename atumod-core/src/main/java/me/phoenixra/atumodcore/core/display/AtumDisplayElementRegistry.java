package me.phoenixra.atumodcore.core.display;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import lombok.Getter;
import me.phoenixra.atumodcore.api.AtumAPI;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.config.Config;
import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.DisplayElement;
import me.phoenixra.atumodcore.api.display.DisplayElementRegistry;
import me.phoenixra.atumodcore.api.display.annotations.RegisterDisplayElement;
import me.phoenixra.atumodcore.api.display.impl.BaseCanvas;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AtumDisplayElementRegistry implements DisplayElementRegistry {
    @Getter
    private final AtumMod atumMod;

    private Map<String, DisplayElement> registry = new ConcurrentHashMap<>();
    public AtumDisplayElementRegistry(AtumMod atumMod) {
        this.atumMod = atumMod;
        getAtumMod().getLogger().info("Registering display elements");
        registerElements(AtumAPI.getInstance().getCoreMod().getPackagePath());
        registerElements(atumMod.getPackagePath());
    }

    @Override
    public @Nullable DisplayCanvas getDrawableCanvas(@NotNull String id) {
        DisplayElement element = this.getElementTemplate(id);
        if(!(element instanceof DisplayCanvas)){
            return null;
        }
        return (DisplayCanvas) element.cloneWithRandomId();
    }

    @Override
    public @Nullable DisplayElement getElementTemplate(@NotNull String id) {
        return registry.get(id);
    }

    @Override
    public @Nullable DisplayCanvas getCanvasTemplate(@NotNull String id) {
        DisplayElement element = this.getElementTemplate(id);
        if(!(element instanceof DisplayCanvas)){
            return null;
        }
        return (DisplayCanvas) element;
    }

    @Override
    public DisplayCanvas compileCanvasTemplate(@NotNull String id, @NotNull Config config) {

        String canvasType = config.getStringOrDefault("type","canvas");
        DisplayElement element = this.getElementTemplate(canvasType);
        if(element == null){
            this.atumMod.getLogger().error("Could not find canvas type: " + canvasType);
            return null;
        }
        if(!(element instanceof BaseCanvas)){
            this.atumMod.getLogger().error("Canvas type: " + canvasType + " is not a canvas!");
            return null;
        }
        BaseCanvas canvas = (BaseCanvas) (element).cloneWithNewVariables(
                id,
                config,
                null,
                null
        );
        getAtumMod().getLogger().info("Found canvas: " + canvas.getTemplateId());
        canvas.applyResolutionOptimizerGlobally(config);
        return canvas;
    }

    @Override
    public void registerTemplate(@NotNull String id, @NotNull DisplayElement element) {
        registry.put(id, element);
    }

    @Override
    public void unregisterTemplate(@NotNull String id) {
        registry.remove(id);
    }




    private void registerElements(String packagePath){
        // EXAMPLE OF A CHANGE
        try (ScanResult scanResult = new ClassGraph()
                .enableAllInfo()
                .acceptPackages(
                        packagePath +
                                "..*"
                ).scan()) {// Begin the scan
            ClassInfoList classInfos = scanResult.getClassesWithAnnotation(RegisterDisplayElement.class.getName());
            //log
            getAtumMod().getLogger().info("Found " + classInfos.size() + " display elements to register");
            for (ClassInfo classInfo : classInfos) {
                Class<?> clazz = classInfo.loadClass();
                RegisterDisplayElement annotation = clazz.getAnnotation(RegisterDisplayElement.class);
                if (DisplayElement.class.isAssignableFrom(clazz)) {
                    try {
                        getAtumMod()
                                .getLogger().info("Loading "+clazz.getName()+" element template...");
                        registerTemplate(
                                annotation.templateId(),
                                (DisplayElement) clazz.getConstructor(AtumMod.class, DisplayCanvas.class)
                                        .newInstance(getAtumMod(),null)
                        );
                    } catch (InstantiationException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
