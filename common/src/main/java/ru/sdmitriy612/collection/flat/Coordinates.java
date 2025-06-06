package ru.sdmitriy612.collection.flat;

import java.io.Serializable;

/**
 * @param x X coordinate. Max field value 497
 * @param y Y coordinate. Min field value -738
 */
public record Coordinates(double x, long y) implements Serializable {

    /**
     * @throws IllegalArgumentException in case of coordinate limit reached
     */
    public Coordinates {
        if (x > 497) throw new IllegalArgumentException("x must be less then 497");
        if (y < -738) throw new IllegalArgumentException("y must be greater then -738");
    }

    /**
     * Returns a string representation of the {@code Coordinates} in the format "(x; y)".
     *
     * @return a string in the format "(x; y)"
     */
    @Override
    public String toString() {
        return String.format("(%f; %d)", x, y);
    }

}