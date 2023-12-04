package me.phoenixra.atumodcore.api.placeholders.context;



import me.phoenixra.atumodcore.api.placeholders.InjectablePlaceholder;
import me.phoenixra.atumodcore.api.placeholders.InjectablePlaceholderList;
import me.phoenixra.atumodcore.api.utils.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class PlaceholderContext {

    /**
     * The PlaceholderInjectable context.
     */
    @NotNull
    private final InjectablePlaceholderList injectableContext;

    /**
     * Create an empty PlaceholderContext.
     */
    public PlaceholderContext() {
        this(null);
    }

    /**
     * Constructs a new PlaceholderContext with the given parameters.
     *
     * @param injectableContext The PlaceholderInjectable parseContext.
     */
    public PlaceholderContext(@Nullable final InjectablePlaceholderList injectableContext) {
        this.injectableContext = Objects.requireNonNullElse(injectableContext, EMPTY_INJECTABLE);
    }

    /**
     * Get the PlaceholderInjectable context.
     *
     * @return The PlaceholderInjectable context.
     */
    @NotNull
    public InjectablePlaceholderList getInjectableContext() {
        return injectableContext;
    }



    /**
     * Copy with an extra injectable context.
     *
     * @param injectableContext The injectable context to add.
     * @return The new context.
     */
    public PlaceholderContext withInjectableContext(@NotNull final InjectablePlaceholderList injectableContext) {
        return new PlaceholderContext(
                new MergedInjectableContext(this.getInjectableContext(), injectableContext)
        );
    }

    @Override
    public String toString() {
        return "PlaceholderContext{" +
                ", injectableContext=" + injectableContext +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof PlaceholderContext)) {
            return false;
        }
        PlaceholderContext that = (PlaceholderContext) o;
        return getInjectableContext().equals(that.getInjectableContext());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getInjectableContext());
    }

    /**
     * Create PlaceholderContext of a PlaceholderInjectable parseContext.
     *
     * @param injectableContext The PlaceholderInjectable parseContext.
     * @return The context.
     */
    public static PlaceholderContext of(@NotNull final InjectablePlaceholderList injectableContext) {
        return new PlaceholderContext(
                injectableContext
        );
    }

    private static final InjectablePlaceholderList EMPTY_INJECTABLE = new InjectablePlaceholderList() {
        @Override
        public void addInjectablePlaceholder(@NotNull final Iterable<InjectablePlaceholder> placeholders) {
            // Do nothing.
        }

        @Override
        public void removeInjectablePlaceholder(@NotNull Iterable<InjectablePlaceholder> placeholders) {
            // Do nothing.
        }

        @Override
        public void clearInjectedPlaceholders() {
            // Do nothing.
        }

        @Override
        public @NotNull List<InjectablePlaceholder> getPlaceholderInjections() {
            return Collections.emptyList();
        }
    };
    public static final PlaceholderContext EMPTY = new PlaceholderContext(
            null
    );
}
