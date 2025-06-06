package ru.sdmitriy612.gui.visual;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.input.MouseEvent;
import ru.sdmitriy612.collection.flat.Flat;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Custom JavaFX Pane that displays Flat objects on a Canvas,
 * with animations for appearance, disappearance, and changes.
 * Manages display objects and their animations based on updates.
 */
public class FlatCanvas extends Pane {

    private final Canvas canvas = new Canvas();
    private final GraphicsContext gc;

    /**
     * Map storing {@link FlatDisplayObject} instances, keyed by their Flat ID.
     */
    private final Map<Long, FlatDisplayObject> flatDisplayObjectMap = new HashMap<>();
    /**
     * List of {@link FlatDisplayObject}s used for rendering.
     * This list is populated from {@code flatDisplayObjectMap} and can be sorted for drawing order.
     */
    private final List<FlatDisplayObject> flatDisplayObjects = new ArrayList<>(); // Used for drawing, sorted

    /**
     * Callback function to be invoked when a flat is selected on the canvas for displaying info.
     */
    private Consumer<Flat> onFlatSelectedForInfo;

    private static final double WORLD_TO_CANVAS_SCALE = 6.0;
    private static final double CANVAS_CENTER_X = 200;
    private static final double CANVAS_CENTER_Y = 450;
    private static final Color AXIS_COLOR = Color.GRAY;
    private static final double AXIS_LINE_WIDTH = 1.5;
    private static final double TICK_MARK_LENGTH = 5;
    private static final double TICK_LABEL_OFFSET = 10;
    private static final int DESIRED_TICK_PIXEL_INTERVAL = 50;


    /**
     * Constructs a new {@code FlatCanvas}.
     * Initializes the canvas, binds its dimensions to the pane,
     * starts the animation timer for drawing, and sets up mouse click handling.
     */
    public FlatCanvas() {
        getChildren().add(canvas);

        canvas.widthProperty().bind(widthProperty());
        canvas.heightProperty().bind(heightProperty());

        this.gc = canvas.getGraphicsContext2D();

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                drawFlats();
            }
        }.start();

        canvas.setOnMouseClicked(this::handleCanvasClick);
    }

    /**
     * Sets the callback that will be invoked when a user clicks on a flat
     * displayed on the canvas.
     *
     * @param onFlatSelectedForInfo A {@link Consumer} that accepts the selected {@link Flat} object.
     */
    public void setOnFlatSelectedForInfo(Consumer<Flat> onFlatSelectedForInfo) {
        this.onFlatSelectedForInfo = onFlatSelectedForInfo;
    }

    /**
     * Handles mouse click events on the canvas.
     * Detects which {@link Flat} object was clicked and triggers the {@code onFlatSelectedForInfo} callback.
     *
     * @param event The {@link MouseEvent} that occurred.
     */
    private void handleCanvasClick(MouseEvent event) {
        Flat clickedFlat = detectClickedFlat(event.getX(), event.getY());
        if (clickedFlat != null && onFlatSelectedForInfo != null) {
            onFlatSelectedForInfo.accept(clickedFlat);
        }
    }

    /**
     * Detects which {@link FlatDisplayObject} was clicked based on the mouse coordinates.
     * Iterates through display objects (in reverse order to prioritize top-drawn elements)
     * and checks if the click point falls within their animated bounds.
     *
     * @param mouseX The X-coordinate of the mouse click on the canvas.
     * @param mouseY The Y-coordinate of the mouse click on the canvas.
     * @return The {@link Flat} object that was clicked, or {@code null} if no flat was clicked.
     */
    private Flat detectClickedFlat(double mouseX, double mouseY) {

        List<FlatDisplayObject> currentObjects = new ArrayList<>(flatDisplayObjects);
        currentObjects.sort(Comparator.comparingDouble((FlatDisplayObject fdo) -> fdo.scaleProperty.get()).reversed());

        for (FlatDisplayObject fdo : currentObjects) {
            Flat flat = fdo.flat;

            double centerX = worldToCanvasX(fdo.currentX.get());
            double centerY = worldToCanvasY(fdo.currentY.get());
            double radius = FlatDisplayObject.MIN_FLAT_RADIUS + (Math.sqrt(flat.getArea()) * FlatDisplayObject.BASE_RADIUS_MULTIPLIER);
            radius *= fdo.scaleProperty.get();

            if (radius > 50) radius = 50;

            double distance = Math.sqrt(Math.pow(mouseX - centerX, 2) + Math.pow(mouseY - centerY, 2));
            if (distance <= radius) {
                return flat;
            }
        }
        return null;
    }


    /**
     * Sets the list of {@link Flat} objects to be displayed on the canvas.
     * Manages the lifecycle of {@link FlatDisplayObject}s,
     * initiating appearance, disappearance, and update animations as needed.
     *
     * @param newFlats The new list of {@link Flat} objects to display. If {@code null}, an empty list is used.
     */
    public void setFlats(List<Flat> newFlats) {
        if (newFlats == null) {
            newFlats = new ArrayList<>();
        }

        Map<Long, Flat> newFlatsMap = newFlats.stream()
                .collect(Collectors.toMap(Flat::getId, flat -> flat));

        for (Map.Entry<Long, FlatDisplayObject> entry : flatDisplayObjectMap.entrySet()) {
            FlatDisplayObject fdo = entry.getValue();
            if (!newFlatsMap.containsKey(fdo.flat.getId())) {
                fdo.animateOut(() -> {
                    flatDisplayObjectMap.remove(fdo.flat.getId());
                });
            }
        }

        for (Flat newFlat : newFlats) {
            FlatDisplayObject existingDisplayObject = flatDisplayObjectMap.get(newFlat.getId());
            if (existingDisplayObject == null) {
                FlatDisplayObject newDisplayObject = new FlatDisplayObject(newFlat);
                newDisplayObject.animateIn();
                flatDisplayObjectMap.put(newFlat.getId(), newDisplayObject);
            } else {
                existingDisplayObject.update(newFlat);
            }
        }

        flatDisplayObjects.clear();
        flatDisplayObjects.addAll(flatDisplayObjectMap.values());
    }


    /**
     * Converts a world X-coordinate to a canvas X-coordinate using a fixed scale and center.
     *
     * @param worldX The X-coordinate in the world (model) space.
     * @return The corresponding X-coordinate on the canvas.
     */
    private double worldToCanvasX(double worldX) {
        return CANVAS_CENTER_X + worldX * WORLD_TO_CANVAS_SCALE;
    }

    /**
     * Converts a world Y-coordinate to a canvas Y-coordinate (inverted for screen coordinates)
     * using a fixed scale and center.
     *
     * @param worldY The Y-coordinate in the world (model) space.
     * @return The corresponding Y-coordinate on the canvas.
     */
    private double worldToCanvasY(double worldY) {
        return CANVAS_CENTER_Y - worldY * WORLD_TO_CANVAS_SCALE;
    }

    /**
     * Rounds a raw interval value to a "nice" number (e.g., 1, 2, 5, 10, 20, 50, etc.)
     * for better readability of axis tick marks.
     *
     * @param val The raw interval value.
     * @return A rounded "nice" number.
     */
    private double roundToNearestNiceNumber(double val) {
        if (val == 0) return 1;
        double order = Math.pow(10, Math.floor(Math.log10(val)));
        double mantissa = val / order;
        if (mantissa >= 5) return 5 * order;
        if (mantissa >= 2) return 2 * order;
        return order;
    }

    /**
     * Draws flats and axes on the canvas. This method is called by the {@link AnimationTimer}.
     * Clears the canvas, then draws axes, and finally draws the flat data.
     */
    private void drawFlats() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        if (canvas.getWidth() <= 0 || canvas.getHeight() <= 0) return;

        drawAxes();
        drawFlatsData();
    }

    /**
     * Draws the X and Y axes on the canvas, including tick marks and labels.
     */
    private void drawAxes() {
        gc.setStroke(Color.GRAY);
        gc.setLineWidth(1.5);
        gc.setFont(new javafx.scene.text.Font(10));

        double originX_canvas = CANVAS_CENTER_X;
        double originY_canvas = CANVAS_CENTER_Y;

        gc.strokeLine(originX_canvas, 0, originX_canvas, canvas.getHeight());
        gc.strokeLine(0, originY_canvas, canvas.getWidth(), originY_canvas);

        gc.setFill(Color.BLACK);
        drawAxisTicksAndLabels(originX_canvas, originY_canvas);

        if (originX_canvas >= 0 && originX_canvas <= canvas.getWidth() &&
                originY_canvas >= 0 && originY_canvas <= canvas.getHeight()) {
            gc.fillText("(0,0)", originX_canvas + TICK_LABEL_OFFSET, originY_canvas + TICK_LABEL_OFFSET);
        }

        gc.strokeLine(canvas.getWidth() - 10, originY_canvas, canvas.getWidth(), originY_canvas);
        gc.strokeLine(canvas.getWidth() - 10, originY_canvas - 5, canvas.getWidth(), originY_canvas);
        gc.strokeLine(canvas.getWidth() - 10, originY_canvas + 5, canvas.getWidth(), originY_canvas);
        gc.strokeLine(originX_canvas, 10, originX_canvas, 0);
        gc.strokeLine(originX_canvas - 5, 10, originX_canvas, 0);
        gc.strokeLine(originX_canvas + 5, 10, originX_canvas, 0);
    }

    /**
     * Helper method for drawing axis tick marks and their corresponding labels.
     *
     * @param originX_canvas The canvas X-coordinate of the origin (0,0).
     * @param originY_canvas The canvas Y-coordinate of the origin (0,0).
     */
    private void drawAxisTicksAndLabels(double originX_canvas, double originY_canvas) {
        double worldTickIntervalX = roundToNearestNiceNumber(DESIRED_TICK_PIXEL_INTERVAL / WORLD_TO_CANVAS_SCALE);
        double worldTickIntervalY = roundToNearestNiceNumber(DESIRED_TICK_PIXEL_INTERVAL / WORLD_TO_CANVAS_SCALE);

        double startWorldX = (0 - CANVAS_CENTER_X) / WORLD_TO_CANVAS_SCALE;
        double endWorldX = (canvas.getWidth() - CANVAS_CENTER_X) / WORLD_TO_CANVAS_SCALE;

        for (double val = Math.floor(startWorldX / worldTickIntervalX) * worldTickIntervalX;
             val <= endWorldX + worldTickIntervalX;
             val += worldTickIntervalX) {
            double tickCanvasX = worldToCanvasX(val);
            if (Math.abs(val) > Double.MIN_NORMAL) {
                gc.strokeLine(tickCanvasX, originY_canvas - TICK_MARK_LENGTH / 2,
                        tickCanvasX, originY_canvas + TICK_MARK_LENGTH / 2);
                gc.fillText(String.format("%.0f", val), tickCanvasX + 2, originY_canvas + TICK_LABEL_OFFSET);
            }
        }

        double startWorldY = (CANVAS_CENTER_Y - canvas.getHeight()) / WORLD_TO_CANVAS_SCALE;
        double endWorldY = (CANVAS_CENTER_Y - 0) / WORLD_TO_CANVAS_SCALE;

        for (double val = Math.floor(startWorldY / worldTickIntervalY) * worldTickIntervalY;
             val <= endWorldY + worldTickIntervalY;
             val += worldTickIntervalY) {
            double tickCanvasY = worldToCanvasY(val);
            if (Math.abs(val) > Double.MIN_NORMAL) {
                gc.strokeLine(originX_canvas - TICK_MARK_LENGTH / 2, tickCanvasY,
                        originX_canvas + TICK_MARK_LENGTH / 2, tickCanvasY);
                gc.fillText(String.format("%.0f", val), originX_canvas + TICK_LABEL_OFFSET, tickCanvasY + 4);
            }
        }
    }

    /**
     * Draws the {@link Flat} data as circles on the canvas.
     * Objects are sorted by their current scale property to ensure proper overlap rendering
     */
    private void drawFlatsData() {
        List<FlatDisplayObject> objectsToDraw = new ArrayList<>(flatDisplayObjects);
        objectsToDraw.sort(Comparator.comparingDouble(fdo -> fdo.scaleProperty.get()));

        for (FlatDisplayObject displayObject : objectsToDraw) {
            Flat flat = displayObject.flat;
            double alpha = displayObject.alphaProperty.get();
            double scale = displayObject.scaleProperty.get();

            if (alpha <= 0.001 || scale <= 0.001) {
                continue;
            }

            double x = worldToCanvasX(displayObject.currentX.get());
            double y = worldToCanvasY(displayObject.currentY.get());
            double radius = FlatDisplayObject.MIN_FLAT_RADIUS + (Math.sqrt(flat.getArea()) * FlatDisplayObject.BASE_RADIUS_MULTIPLIER);
            radius *= scale;

            if (radius > 50) radius = 50;

            Color color = FlatDisplayObject.colorFromId(flat.getUserOwnerID());

            gc.setGlobalAlpha(alpha);
            gc.setFill(color);
            gc.setStroke(color.deriveColor(0, 1, 0.7, 1.0));
            gc.setLineWidth(1.0);

            gc.fillOval(x - radius, y - radius, radius * 2, radius * 2);
            gc.strokeOval(x - radius, y - radius, radius * 2, radius * 2);

        }
        gc.setGlobalAlpha(1.0);
    }
}