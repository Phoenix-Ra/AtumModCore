package me.phoenixra.atumodcore.api.placeholders.types;


import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.placeholders.RegistrablePlaceholder;
import me.phoenixra.atumodcore.api.placeholders.context.PlaceholderContext;
import me.phoenixra.atumodcore.api.utils.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Pattern;
public class DynamicPlaceholder implements RegistrablePlaceholder {
    /**
     * The arguments pattern.
     */
    private final Pattern pattern;

    /**
     * The function to retrieve the output of the arguments.
     */
    private final Function<@NotNull String, @Nullable String> function;

    /**
     * The plugin for the arguments.
     */
    private final AtumMod plugin;

    /**
     * Create a new dynamic arguments.
     *
     * @param plugin   The plugin.
     * @param pattern  The pattern.
     * @param function The function to retrieve the value.
     */
    public DynamicPlaceholder(@NotNull final AtumMod plugin,
                              @NotNull final Pattern pattern,
                              @NotNull final Function<@NotNull String, @Nullable String> function) {
        this.plugin = plugin;
        this.pattern = Pattern.compile("%" + plugin.getName()+"_"+pattern + "%");
        this.function = function;

    }

    @Override
    @Nullable
    public String getValue(@NotNull final String args,
                           @NotNull final PlaceholderContext context) {
        return function.apply(args);
    }

    @Override
    public @NotNull AtumMod getAtumMod() {
        return this.plugin;
    }

    @NotNull
    @Override
    public Pattern getPattern() {
        return this.pattern;
    }

    @Override
    public @NotNull DynamicPlaceholder register() {
        return (DynamicPlaceholder) RegistrablePlaceholder.super.register();
    }

    @Override
    public boolean equals(@Nullable final Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof DynamicPlaceholder)) {
            return false;
        }
        DynamicPlaceholder that = (DynamicPlaceholder) o;
        return Objects.equals(this.getPattern(), that.getPattern())
                && Objects.equals(this.getAtumMod(), that.getAtumMod());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getPattern(), this.getAtumMod());
    }
}
