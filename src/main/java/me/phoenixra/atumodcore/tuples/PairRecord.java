package me.phoenixra.atumodcore.tuples;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

/**
 * Two not null values.
 *
 * @param <A> The first value type.
 * @param <B> The second value type.
 */
@AllArgsConstructor
public class PairRecord<A, B> {
    /**
     * The first item in the tuple.
     */
    @NotNull
    @Getter
    @Setter
    private A first;

    /**
     * The second item in the tuple.
     */
    @NotNull @Getter @Setter
    private B second;
}
