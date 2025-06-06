package ru.sdmitriy612.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.sdmitriy612.collection.flat.*;

public class FlatFormController {
    @FXML
    private TextField nameField, coordXField, coordYField, areaField, roomsField,
            houseNameField, houseYearField, liftsField;
    @FXML
    private ChoiceBox<String> furnishChoice, viewChoice, transportChoice;

    private FlatBuilder result;

    public FlatBuilder getResult() {
        return result;
    }

    public void setFlatToUpdate(Flat flat) {
        nameField.setText(flat.getName());
        coordXField.setText(String.valueOf(flat.getCoordinates().x()));
        coordYField.setText(String.valueOf(flat.getCoordinates().y()));
        areaField.setText(String.valueOf(flat.getArea()));
        roomsField.setText(String.valueOf(flat.getNumberOfRooms()));

        furnishChoice.setValue(flat.getFurnish() != null ? flat.getFurnish().name() : null);
        viewChoice.setValue(flat.getView() != null ? flat.getView().name() : null);
        transportChoice.setValue(flat.getTransport() != null ? flat.getTransport().name() : null);

        if (flat.getHouse() != null) {
            houseNameField.setText(flat.getHouse().getName());
            houseYearField.setText(String.valueOf(flat.getHouse().getYear()));
            liftsField.setText(String.valueOf(flat.getHouse().getNumberOfLifts()));
        } else {
            houseNameField.clear();
            houseYearField.clear();
            liftsField.clear();
        }
    }


    @FXML
    private void onSave() {
        try {
            FlatBuilder builder = new FlatBuilder();

            builder.setName(nameField.getText().trim());
            builder.setArea(Float.parseFloat(areaField.getText()));
            builder.setNumberOfRooms(Integer.parseInt(roomsField.getText()));

            float x = Float.parseFloat(coordXField.getText());
            long y = Long.parseLong(coordYField.getText());
            builder.setCoordinates(new Coordinates(x, y));

            builder.setFurnish(furnishChoice.getValue() != null ? Furnish.valueOf(furnishChoice.getValue()) : null);
            builder.setView(viewChoice.getValue() != null ? View.valueOf(viewChoice.getValue()) : null);
            builder.setTransport(transportChoice.getValue() != null ? Transport.valueOf(transportChoice.getValue()) : null);

            House house = getHouse();

            builder.setHouse(house);

            result = builder;

            ((Stage) nameField.getScene().getWindow()).close();

        } catch (Exception e) {
            showAlert("Ошибка ввода", e.getMessage());
        }
    }

    private House getHouse() {
        String houseName = houseNameField.getText();
        String houseYearText = houseYearField.getText();
        String liftsText = liftsField.getText();

        House house = null;
        if (houseName != null && !houseName.isEmpty()
                && houseYearText != null && !houseYearText.isEmpty()
                && liftsText != null && !liftsText.isEmpty()) {

            house = new House();
            house.setName(houseName);
            house.setYear(Long.parseLong(houseYearText));
            house.setNumberOfLifts(Long.parseLong(liftsText));
        }
        return house;
    }

    @FXML
    private void onCancel() {
        result = null;
        ((Stage) nameField.getScene().getWindow()).close();
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
