package me.phoenixra.atumodcore.api.placeholders;

import com.google.common.collect.Sets;
import me.phoenixra.atumodcore.api.placeholders.types.injectable.StaticPlaceholder;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public interface InjectablePlaceholderList {
    /**
     * Inject arguments.
     *
     * @param placeholders The placeholders.
     */
    default void injectPlaceholders(@NotNull StaticPlaceholder... placeholders) {
        this.addInjectablePlaceholder(Arrays.stream(placeholders).collect(Collectors.toSet()));
    }

    /**
     * Inject arguments.
     *
     * @param placeholders The placeholders.
     */
    default void injectPlaceholders(@NotNull InjectablePlaceholder... placeholders) {
        this.addInjectablePlaceholder(Arrays.stream(placeholders).collect(Collectors.toSet()));
    }

    /**
     * Inject placeholders.
     * <p>
     * If a placeholder already has the same pattern, it should be replaced.
     *
     * @param placeholders The placeholders.
     */
    void addInjectablePlaceholder(@NotNull Iterable<InjectablePlaceholder> placeholders);

    /**
     * Remove placeholders
     */
    void removeInjectablePlaceholder(@NotNull Iterable<InjectablePlaceholder> placeholders);

    /**
     * Clear injected placeholders.
     */
    void clearInjectedPlaceholders();

    /**
     * Get injected placeholders.
     * <p>
     * This method should always return an immutable list.
     *
     * @return Injected placeholders.
     */
    @NotNull
    List<InjectablePlaceholder> getPlaceholderInjections();
}
