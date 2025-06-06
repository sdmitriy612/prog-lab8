package ru.sdmitriy612.gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ru.sdmitriy612.collection.flat.Flat;
import ru.sdmitriy612.collection.flat.House;
import javafx.scene.control.Button;

import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;

/**
 * Controller for the flat information dialog window.
 * Displays detailed information about a {@link Flat} object and provides an option to edit it.
 */
public class FlatInfoController {

    @FXML private Label idLabel;
    @FXML private Label nameLabel;
    @FXML private Label coordinatesLabel;
    @FXML private Label creationDateLabel;
    @FXML private Label areaLabel;
    @FXML private Label numberOfRoomsLabel;
    @FXML private Label furnishLabel;
    @FXML private Label viewLabel;
    @FXML private Label transportLabel;
    @FXML private Label ownerIdLabel;
    @FXML private VBox houseInfoContainer;
    @FXML private Label houseNameLabel;
    @FXML private Label houseYearLabel;
    @FXML private Label houseLiftsLabel;

    private Flat currentFlat;
    private Consumer<Flat> onEditCallback;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /**
     * Sets the {@link Flat} object whose information will be displayed.
     *
     * @param flat The {@link Flat} object to display.
     */
    public void setFlat(Flat flat) {
        this.currentFlat = flat;
        populateLabels();
    }

    /**
     * Sets the callback function to be executed when the "Edit" button is pressed.
     * The callback receives the {@link Flat} object to be edited.
     *
     * @param onEditCallback A {@link Consumer} that accepts the {@link Flat} object for editing.
     */
    public void setOnEdit(Consumer<Flat> onEditCallback) {
        this.onEditCallback = onEditCallback;
    }

    /**
     * Populates the UI labels with information from the current {@link Flat} object.
     * Handles the display of optional {@link House} information.
     * Original Flat field names are used for labels.
     */
    private void populateLabels() {
        if (currentFlat == null) return;

        idLabel.setText(String.valueOf(currentFlat.getId()));
        nameLabel.setText(currentFlat.getName());
        coordinatesLabel.setText(String.format("(%.1f; %d)", currentFlat.getCoordinates().x(), currentFlat.getCoordinates().y()));
        creationDateLabel.setText(currentFlat.getCreationDate().format(DATE_FORMATTER));
        areaLabel.setText(String.format("%.2f", currentFlat.getArea()));
        numberOfRoomsLabel.setText(String.valueOf(currentFlat.getNumberOfRooms()));
        furnishLabel.setText(currentFlat.getFurnish() != null ? currentFlat.getFurnish().name() : "N/A");
        viewLabel.setText(currentFlat.getView() != null ? currentFlat.getView().name() : "N/A");
        transportLabel.setText(currentFlat.getTransport() != null ? currentFlat.getTransport().name() : "N/A");
        ownerIdLabel.setText(String.valueOf(currentFlat.getUserOwnerID()));

        House house = currentFlat.getHouse();
        if (house != null) {
            houseInfoContainer.setVisible(true);
            houseInfoContainer.setManaged(true);
            houseNameLabel.setText("House Name: " + (house.getName() != null ? house.getName() : "N/A"));
            houseYearLabel.setText("House Year: " + (house.getYear() != null ? String.valueOf(house.getYear()) : "N/A"));
            houseLiftsLabel.setText("House Number of lifts: " + (house.getNumberOfLifts() != null ? String.valueOf(house.getNumberOfLifts()) : "N/A"));
        } else {
            houseInfoContainer.setVisible(false);
            houseInfoContainer.setManaged(false);
        }
    }

    /**
     * Handles the action when the "Edit" button is clicked.
     * Triggers the registered {@code onEditCallback} with the current {@link Flat} object.
     * Optionally closes the info window after initiating the edit operation.
     *
     * @param event The {@link ActionEvent} that triggered this method.
     */
    @FXML
    private void onEditFlat(ActionEvent event) {
        if (onEditCallback != null && currentFlat != null) {
            onEditCallback.accept(currentFlat);
            // Optionally: close this information window after launching the editor
            ((Stage)((Button)event.getSource()).getScene().getWindow()).close();
        }
    }

    /**
     * Handles the action when the "Close" button is clicked.
     * Closes the current information window.
     */
    @FXML
    private void onClose() {
        ((Stage) nameLabel.getScene().getWindow()).close();
    }
}