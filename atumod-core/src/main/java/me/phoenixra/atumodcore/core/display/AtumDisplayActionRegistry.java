package me.phoenixra.atumodcore.core.display;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import lombok.Getter;
import me.phoenixra.atumodcore.api.AtumAPI;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.DisplayElement;
import me.phoenixra.atumodcore.api.display.actions.DisplayAction;
import me.phoenixra.atumodcore.api.display.actions.DisplayActionRegistry;
import me.phoenixra.atumodcore.api.display.annotations.RegisterDisplayAction;
import me.phoenixra.atumodcore.api.display.annotations.RegisterDisplayElement;
import me.phoenixra.atumodcore.core.display.actions.*;
import me.phoenixra.atumodcore.core.display.actions.canvas.ActionAddElement;
import me.phoenixra.atumodcore.core.display.actions.canvas.ActionRemoveElement;
import me.phoenixra.atumodcore.core.display.actions.renderer.*;
import me.phoenixra.atumodcore.core.display.actions.server.ActionConnectToServer;
import me.phoenixra.atumodcore.core.display.actions.server.ActionSendDisplayEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AtumDisplayActionRegistry implements DisplayActionRegistry {

    @Getter
    private final AtumMod atumMod;

    private Map<String, DisplayAction> registry = new ConcurrentHashMap<>();
    public AtumDisplayActionRegistry(AtumMod atumMod) {
        this.atumMod = atumMod;
        getAtumMod().getLogger().info("Registering display actions");
        registerActions(AtumAPI.getInstance().getCoreMod().getPackagePath());
        registerActions(atumMod.getPackagePath());
    }

    @Override
    public @Nullable DisplayAction getActionById(@NotNull String id) {
        return registry.get(id.toLowerCase());
    }

    @Override
    public void register(@NotNull String id, @NotNull DisplayAction action) {
        registry.put(id.toLowerCase(), action);
    }
    @Override
    public void unregister(@NotNull String id) {
        registry.remove(id.toLowerCase());
    }


    private void registerActions(String packagePath){
        // EXAMPLE OF A CHANGE
        try (ScanResult scanResult = new ClassGraph()
                .enableAllInfo()
                .acceptPackages(
                        packagePath +
                                "..*"
                ).scan()) {// Begin the scan
            ClassInfoList classInfos = scanResult.getClassesWithAnnotation(RegisterDisplayAction.class.getName());
            //log
            getAtumMod().getLogger().info("Found " + classInfos.size() + " display actions to register");
            for (ClassInfo classInfo : classInfos) {
                Class<?> clazz = classInfo.loadClass();
                RegisterDisplayAction annotation = clazz.getAnnotation(RegisterDisplayAction.class);
                if (DisplayAction.class.isAssignableFrom(clazz)) {
                    try {
                        getAtumMod()
                                .getLogger().info("Loading "+clazz.getName()+" element action...");
                        register(
                                annotation.templateId(),
                                (DisplayAction) clazz.newInstance()
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
