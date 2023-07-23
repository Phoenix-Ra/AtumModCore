package me.phoenixra.atumodcore.tuples;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

/**
 * Three not null values.
 *
 * @param <A> The first value type.
 * @param <B> The second value type.
 * @param <C> The third value type.
 */
@AllArgsConstructor
public class TripletRecord<A,B,C> {

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

    /**
     * The third item in the tuple.
     */
    @NotNull @Getter @Setter
    private C third;
}
