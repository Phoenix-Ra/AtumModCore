package me.phoenixra.atumodcore.core;

import me.phoenixra.atumconfig.api.placeholders.PlaceholderManager;
import me.phoenixra.atumconfig.api.placeholders.context.PlaceholderContext;
import me.phoenixra.atumodcore.api.AtumAPI;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.display.DisplayManager;
import me.phoenixra.atumodcore.api.input.InputHandler;
import me.phoenixra.atumodcore.api.network.NetworkManager;
import me.phoenixra.atumodcore.core.display.AtumDisplayManager;
import me.phoenixra.atumodcore.core.input.AtumInputHandler;
import me.phoenixra.atumodcore.core.network.AtumNetworkManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import redempt.crunch.Crunch;
import redempt.crunch.functional.EvaluationEnvironment;

import java.util.Collection;
import java.util.HashMap;

public class AtumAPIImpl implements AtumAPI {

    private EvaluationEnvironment evaluationEnvironment = new EvaluationEnvironment();

    private AtumMod atumMod;
    private HashMap<String,AtumMod> mods = new HashMap<>();

    public AtumAPIImpl(@NotNull AtumMod atumMod) {
        this.atumMod = atumMod;
    }

    @Override
    public @NotNull InputHandler createInputHandler(@NotNull AtumMod atumMod) {
        return new AtumInputHandler(atumMod);
    }


    @Override
    public @NotNull Logger createLogger(@NotNull AtumMod atumMod) {
        return LogManager.getLogger(atumMod.getName());
    }


    @Override
    public @NotNull DisplayManager createDisplayManager(@NotNull AtumMod atumMod) {
        return new AtumDisplayManager(atumMod);
    }


    @Override
    public @NotNull NetworkManager createNetworkManager(@NotNull AtumMod atumMod) {
        return new AtumNetworkManager(atumMod);
    }


    @Override
    public double evaluate(@NotNull AtumMod atumMod, @NotNull String expression, @NotNull PlaceholderContext context) {
        return Crunch.compileExpression(
                PlaceholderManager.translatePlaceholders(
                        getCoreMod(),
                        expression,
                        context
                ),
                evaluationEnvironment
        ).evaluate();
    }

    @Override
    public AtumMod getCoreMod() {
        return atumMod;
    }

    @Override
    public AtumMod getLoadedAtumMod(String id) {
        return mods.get(id);
    }

    @Override
    public @NotNull Collection<AtumMod> getLoadedAtumMods() {
        return mods.values();
    }

    @Override
    public void registerAtumMod(@NotNull AtumMod atumMod) {
        mods.put(atumMod.getModID(), atumMod);
    }
}
