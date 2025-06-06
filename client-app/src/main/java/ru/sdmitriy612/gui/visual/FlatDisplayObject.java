package ru.sdmitriy612.gui.visual;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import ru.sdmitriy612.collection.flat.Flat;

import java.util.Objects;

/**
 * Represents a visual display object for a Flat on a canvas,
 * managing its animatable properties and animations.
 */
public class FlatDisplayObject {
    public Flat flat; // The current Flat object
    // Previous values to detect changes
    private double prevX, prevY;
    private Float prevArea;

    public final DoubleProperty alphaProperty = new SimpleDoubleProperty(1.0); // Opacity
    public final DoubleProperty scaleProperty = new SimpleDoubleProperty(1.0); // Object scale (for appear/disappear/pulsation)
    public final DoubleProperty currentX = new SimpleDoubleProperty(); // For X movement animation
    public final DoubleProperty currentY = new SimpleDoubleProperty(); // For Y movement animation

    // References to active Timelines to stop them if needed
    private Timeline moveTimeline;
    private Timeline pulseTimeline;
    private Timeline visibilityTimeline; // For appear/disappear

    // Constants for drawing and animation (moved from FlatCanvas, specific to display object)
    private static final Duration ANIMATION_DURATION = Duration.millis(500); // General animation duration (fade in/out)
    private static final Duration MOVE_ANIMATION_DURATION = Duration.millis(400); // Duration for movement animation
    private static final Duration PULSE_ANIMATION_DURATION = Duration.millis(300); // Duration for pulsation animation
    public static final double MIN_FLAT_RADIUS = 5;
    public static final double BASE_RADIUS_MULTIPLIER = 0.5;


    /**
     * Constructs a new {@code FlatDisplayObject} with the given {@link Flat}.
     * Initializes current and previous coordinates and area.
     *
     * @param flat The {@link Flat} object to display.
     */
    public FlatDisplayObject(Flat flat) {
        this.flat = flat;
        // Initialize current and previous values
        this.currentX.set(flat.getCoordinates().x());
        this.currentY.set(flat.getCoordinates().y());
        this.prevX = flat.getCoordinates().x();
        this.prevY = flat.getCoordinates().y();
        this.prevArea = flat.getArea();
    }

    /**
     * Updates the internal {@link Flat} object and initiates animations if data has changed.
     * Handles movement animation if coordinates change and pulsation animation if area changes.
     *
     * @param newFlat The new {@link Flat} object with updated data.
     */
    public void update(Flat newFlat) {
        boolean coordsChanged = newFlat.getCoordinates().x() != prevX || newFlat.getCoordinates().y() != prevY;
        boolean areaChanged = !Objects.equals(newFlat.getArea(), prevArea);

        this.flat = newFlat; // Update reference to the current Flat

        // Stop all previous animations for this object to avoid conflicts
        if (moveTimeline != null) moveTimeline.stop();
        if (pulseTimeline != null) pulseTimeline.stop();
        // visibilityTimeline usually doesn't need to be stopped as it's a one-time animation for appear/disappear

        if (coordsChanged) {
            moveTimeline = new Timeline(
                    new KeyFrame(MOVE_ANIMATION_DURATION,
                            new KeyValue(currentX, newFlat.getCoordinates().x(), Interpolator.EASE_BOTH),
                            new KeyValue(currentY, newFlat.getCoordinates().y(), Interpolator.EASE_BOTH)
                    )
            );
            moveTimeline.play();
            prevX = newFlat.getCoordinates().x();
            prevY = newFlat.getCoordinates().y();
        } else {
            // If coordinates haven't changed but were animated, reset currentX/Y
            // to the current world coordinates to prevent shifts from old animations.
            currentX.set(newFlat.getCoordinates().x());
            currentY.set(newFlat.getCoordinates().y());
        }

        if (areaChanged) {
            // Scale pulsation
            pulseTimeline = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(scaleProperty, 1.0)),
                    new KeyFrame(PULSE_ANIMATION_DURATION.divide(2), new KeyValue(scaleProperty, 1.2, Interpolator.EASE_OUT)),
                    new KeyFrame(PULSE_ANIMATION_DURATION, new KeyValue(scaleProperty, 1.0, Interpolator.EASE_IN))
            );
            pulseTimeline.play();
            prevArea = newFlat.getArea();
        } else {
            // If area hasn't changed, ensure scaleProperty is 1.0
            // (unless it's being animated for appearance/disappearance).
            if (visibilityTimeline == null || visibilityTimeline.getStatus() == Timeline.Status.STOPPED) {
                scaleProperty.set(1.0);
            }
        }
        // Ensure alphaProperty for an existing object is 1.0
        // (unless it's being animated for appearance/disappearance).
        if (visibilityTimeline == null || visibilityTimeline.getStatus() == Timeline.Status.STOPPED) {
            alphaProperty.set(1.0);
        }
    }

    /**
     * Starts the animation for the object to appear (fade in and scale up).
     */
    public void animateIn() {
        alphaProperty.set(0.0);
        scaleProperty.set(0.0);
        visibilityTimeline = new Timeline(
                new KeyFrame(ANIMATION_DURATION,
                        new KeyValue(alphaProperty, 1.0, Interpolator.EASE_OUT),
                        new KeyValue(scaleProperty, 1.0, Interpolator.EASE_OUT)
                )
        );
        visibilityTimeline.play();
    }

    /**
     * Starts the animation for the object to disappear (fade out and scale down).
     * A callback is provided to be executed when the animation finishes.
     *
     * @param onFinished A {@link Runnable} to execute once the disappearance animation is complete.
     */
    public void animateOut(Runnable onFinished) {
        // Stop all other animations as the object will be removed
        if (moveTimeline != null) moveTimeline.stop();
        if (pulseTimeline != null) pulseTimeline.stop();
        if (visibilityTimeline != null) visibilityTimeline.stop(); // Stop if still appearing

        visibilityTimeline = new Timeline(
                new KeyFrame(ANIMATION_DURATION,
                        new KeyValue(alphaProperty, 0.0, Interpolator.EASE_OUT),
                        new KeyValue(scaleProperty, 0.0, Interpolator.EASE_OUT)
                )
        );
        visibilityTimeline.setOnFinished(e -> onFinished.run());
        visibilityTimeline.play();
    }

    /**
     * Generates a distinct color based on a given ID using the golden angle algorithm.
     * This ensures good color distribution for different user owners.
     *
     * @param id The ID (e.g., user owner ID) to generate a color for.
     * @return A {@link Color} object.
     */
    public static Color colorFromId(long id) {
        final double GOLDEN_ANGLE_DEG = 137.508;
        double hue = (id * GOLDEN_ANGLE_DEG) % 360;
        if (hue < 0) hue += 360;

        double saturation = 0.65;
        double brightness = 0.85;

        return Color.hsb(hue, saturation, brightness);
    }

    /**
     * Compares this {@code FlatDisplayObject} to the specified object.
     * The comparison is based on the ID of the contained {@link Flat} object.
     *
     * @param o The object to compare with.
     * @return {@code true} if the objects are equal (have the same Flat ID), {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlatDisplayObject that = (FlatDisplayObject) o;
        return Objects.equals(flat.getId(), that.flat.getId());
    }

    /**
     * Returns a hash code value for the object.
     * The hash code is based on the ID of the contained {@link Flat} object.
     *
     * @return A hash code value for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(flat.getId());
    }
}