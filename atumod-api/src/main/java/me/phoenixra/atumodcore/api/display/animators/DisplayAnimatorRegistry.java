package me.phoenixra.atumodcore.api.display.animators;

import me.phoenixra.atumodcore.api.AtumMod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface DisplayAnimatorRegistry {
    /**
     * Get an animator by id.
     *
     * @param id The id of the animator.
     * @return The animator.
     */
    @Nullable
    DisplayAnimator getAnimatorById(@NotNull String id);

    /**
     * Register an animator.
     *
     * @param id       The id of the animator.
     * @param animator The animator.
     */
    void register(@NotNull String id, @NotNull DisplayAnimator animator);

    /**
     * Unregister an animator.
     *
     * @param id The id of the animator.
     */
    void unregister(@NotNull String id);

    /**
     * Get the mod.
     *
     * @return The mod instance.
     */
    @NotNull
    AtumMod getAtumMod();
}
