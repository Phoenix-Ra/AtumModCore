package me.phoenixra.atumodcore.api.config.variables.impl;

import lombok.Getter;
import me.phoenixra.atumodcore.api.config.Config;
import me.phoenixra.atumodcore.api.config.variables.ConfigVariable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.function.Function;

public class SimpleConfigVariable<T> implements ConfigVariable<T> {


    @Getter private T value;
    private boolean forceCloneValue = false;

    private Function<String, T> valueFromString;
    private Function<Config, T> getValueFromConfig;
    public SimpleConfigVariable(@NotNull T value,
                                @Nullable Function<String, T> valueFromString,
                                @Nullable Function<Config, T> valueFromConfig){
        this.value=value;
        this.valueFromString = valueFromString;
        this.getValueFromConfig = valueFromConfig;
    }

    /**
     * Whether to force clone the value using java reflections.
     *
     * Use it with cautious. May produce lags
     *
     */
    public SimpleConfigVariable<T> setForceCloneValue(boolean value){
        forceCloneValue=value;
        return this;
    }
    @Override
    public @NotNull SimpleConfigVariable<T> setValue(@NotNull T value) {
        this.value=value;
        return this;
    }

    @Override
    public @NotNull ConfigVariable<T> setValueFromString(@NotNull String value) {
        if(valueFromString == null) throw new UnsupportedOperationException("This variable cannot be set from a string");
        return setValue(valueFromString.apply(value));
    }

    @Override
    public @NotNull ConfigVariable<T> setValueFromConfig(@NotNull Config config) {
        if(getValueFromConfig == null) throw new UnsupportedOperationException("This variable cannot be set from a config");
        return setValue(getValueFromConfig.apply(config));
    }

    @Override
    public SimpleConfigVariable<T> clone() throws CloneNotSupportedException {
        SimpleConfigVariable<T> variable = (SimpleConfigVariable<T>) (super.clone());
        if(forceCloneValue && value != null && value instanceof Cloneable){
            try {
                Method method = value.getClass().getMethod("clone");
                method.setAccessible(true);
                setValue((T)method.invoke(value));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return variable;
    }
}
