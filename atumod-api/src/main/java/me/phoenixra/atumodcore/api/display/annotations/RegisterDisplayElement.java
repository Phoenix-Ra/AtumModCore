package me.phoenixra.atumodcore.api.display.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Register a display element in the
 * {@link me.phoenixra.atumodcore.api.display.DisplayManager}
 * implementation
 * <br><br>
 * The element class must extend
 * {@link me.phoenixra.atumodcore.api.display.DisplayElement}
 * <br><br>
 * The element class must have a constructor with
 * {@link me.phoenixra.atumodcore.api.AtumMod}
 * and
 * {@link me.phoenixra.atumodcore.api.display.DisplayCanvas}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RegisterDisplayElement {
    String templateId();

}
