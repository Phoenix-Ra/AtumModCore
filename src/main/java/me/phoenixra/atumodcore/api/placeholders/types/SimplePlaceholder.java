package me.phoenixra.atumodcore.api.placeholders.types;

import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.placeholders.RegistrablePlaceholder;
import me.phoenixra.atumodcore.api.placeholders.context.PlaceholderContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Supplier;
import java.util.regex.Pattern;
public class SimplePlaceholder implements RegistrablePlaceholder {
    /**
     * The arguments pattern.
     */
    private final Pattern pattern;

    /**
     * The function to retrieve the output of the arguments.
     */
    private final Supplier<@Nullable String> function;

    /**
     * The plugin for the arguments.
     */
    private final AtumMod plugin;

    /**
     * Create a new player arguments.
     *
     * @param plugin     The plugin.
     * @param identifier The identifier.
     * @param function   The function to retrieve the value.
     */
    public SimplePlaceholder(@NotNull final AtumMod plugin,
                             @NotNull final String identifier,
                             @NotNull final Supplier<@Nullable String> function) {
        this.plugin = plugin;
        this.pattern = Pattern.compile("%"+identifier+"%");
        this.function = function;
    }

    @Override
    public @Nullable String getValue(@NotNull final String args,
                                     @NotNull final PlaceholderContext context) {
        return function.get();
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
    public @NotNull SimplePlaceholder register() {
        return (SimplePlaceholder) RegistrablePlaceholder.super.register();
    }

    @Override
    public boolean equals(@Nullable final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SimplePlaceholder)) {
            return false;
        }
        SimplePlaceholder that = (SimplePlaceholder) o;
        return Objects.equals(this.getPattern(), that.getPattern())
                && Objects.equals(this.getAtumMod(), that.getAtumMod());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getPattern(), this.getAtumMod());
    }
}
