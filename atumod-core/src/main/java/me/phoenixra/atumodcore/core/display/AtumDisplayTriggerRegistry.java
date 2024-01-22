package me.phoenixra.atumodcore.core.display;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import lombok.Getter;
import me.phoenixra.atumodcore.api.AtumAPI;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.display.actions.DisplayAction;
import me.phoenixra.atumodcore.api.display.annotations.RegisterDisplayAction;
import me.phoenixra.atumodcore.api.display.annotations.RegisterDisplayTrigger;
import me.phoenixra.atumodcore.api.display.triggers.DisplayTrigger;
import me.phoenixra.atumodcore.api.display.triggers.DisplayTriggerRegistry;
import me.phoenixra.atumodcore.core.display.triggers.TriggerDataChanged;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AtumDisplayTriggerRegistry implements DisplayTriggerRegistry {
    @Getter
    private final AtumMod atumMod;

    private Map<String, DisplayTrigger> registry = new ConcurrentHashMap<>();

    public AtumDisplayTriggerRegistry(@NotNull AtumMod atumMod) {
        this.atumMod = atumMod;

        getAtumMod().getLogger().info("Registering display triggers");
        registerTriggers(AtumAPI.getInstance().getCoreMod().getPackagePath());
        registerTriggers(atumMod.getPackagePath());
    }

    @Override
    public void registerTemplate(@NotNull String id,
                                 @NotNull DisplayTrigger trigger) {
        registry.put(id, trigger);
    }

    @Override
    public void unregisterTemplate(@NotNull String id) {
        registry.remove(id);
    }

    @Override
    public DisplayTrigger getTemplate(@NotNull String id) {
        return registry.get(id);
    }


    private void registerTriggers(String packagePath){
        // EXAMPLE OF A CHANGE
        try (ScanResult scanResult = new ClassGraph()
                .enableAllInfo()
                .acceptPackages(
                        packagePath +
                                "..*"
                ).scan()) {// Begin the scan
            ClassInfoList classInfos = scanResult.getClassesWithAnnotation(RegisterDisplayTrigger.class.getName());
            //log
            getAtumMod().getLogger().info("Found " + classInfos.size() + " display triggers to register");
            for (ClassInfo classInfo : classInfos) {
                Class<?> clazz = classInfo.loadClass();
                RegisterDisplayTrigger annotation = clazz.getAnnotation(RegisterDisplayTrigger.class);
                if (DisplayTrigger.class.isAssignableFrom(clazz)) {
                    try {
                        getAtumMod()
                                .getLogger().info("Loading "+clazz.getName()+" trigger template...");
                        registerTemplate(
                                annotation.templateId(),
                                (DisplayTrigger) clazz.getConstructor(AtumMod.class)
                                        .newInstance(getAtumMod())
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
