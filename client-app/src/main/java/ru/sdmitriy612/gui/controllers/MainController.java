package ru.sdmitriy612.gui.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;
import ru.sdmitriy612.clientapp.Session;
import ru.sdmitriy612.clientapp.handling.Handler;
import ru.sdmitriy612.collection.flat.*;
import ru.sdmitriy612.gui.filters.FlatFilter;
import ru.sdmitriy612.gui.visual.FlatCanvas;
import ru.sdmitriy612.interactors.Response;
import ru.sdmitriy612.interactors.ResponseType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Main controller for the application's primary window.
 * Manages the display of Flat objects in a TableView and a FlatCanvas (visualization),
 * handles user interactions for commands, adding, updating, and filtering flats.
 */
public class MainController {

    @FXML private Label currentUserLabel;
    @FXML private TableView<Flat> flatsTable;
    @FXML private Pane visualizationPane; // Этот Pane должен быть определен в FXML

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());
    private List<Flat> allFlats = List.of();  // Все квартиры после /show

    @FXML private TextField removeIdField;
    @FXML private ChoiceBox<View> viewChoiceBox;
    @FXML private ChoiceBox<Furnish> furnishChoiceBox;
    @FXML private TextField scriptPathField;

    @FXML private TextField filterIdField;
    @FXML private TextField filterNameField;
    @FXML private TextField filterXField;
    @FXML private TextField filterYField;
    @FXML private TextField filterCreationField;
    @FXML private TextField filterAreaField;
    @FXML private TextField filterRoomsField;
    @FXML private TextField filterFurnishField;
    @FXML private TextField filterViewField;
    @FXML private TextField filterTransportField;
    @FXML private TextField filterHouseNameField;
    @FXML private TextField filterHouseYearField;
    @FXML private TextField filterHouseLiftsField;
    @FXML private TextField filterOwnerIdField;
    @FXML private BorderPane mainPane; // ДОБАВЛЕНО: убедитесь, что это поле связано в FXML!

    private FlatCanvas flatCanvas; // Сделано снова приватным полем без @FXML для ручной инициализации
    private Stage flatInfoStage; // Добавлен для переиспользования окна информации

    private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    private static final int AUTO_REFRESH_INTERVAL_SECONDS = 5;

    /**
     * Initializes the controller. This method is called after the FXML file has been loaded.
     * Sets up the ObjectMapper, initializes the TableView columns,
     * instantiates and binds the FlatCanvas, sets up click listeners for filtering,
     * and performs an initial data load.
     */
    @FXML
    private void initialize() {

        currentUserLabel.textProperty().bind(new SimpleStringProperty(Session.getInstance().getUserAuthorization().login()));

        flatCanvas = new FlatCanvas();
        visualizationPane.getChildren().add(flatCanvas);
        // Привязка FlatCanvas к размерам visualizationPane
        AnchorPane.setTopAnchor(flatCanvas, 0.0);
        AnchorPane.setBottomAnchor(flatCanvas, 0.0);
        AnchorPane.setLeftAnchor(flatCanvas, 0.0);
        AnchorPane.setRightAnchor(flatCanvas, 0.0);

        // Установка коллбэка для FlatCanvas для открытия окна информации
        flatCanvas.setOnFlatSelectedForInfo(this::openFlatInfoDialog);

        setupTableColumns();
        executeCommand("/show", null);
        viewChoiceBox.setItems(FXCollections.observableArrayList(View.values()));
        furnishChoiceBox.setItems(FXCollections.observableArrayList(Furnish.values()));

        scheduledExecutorService.scheduleAtFixedRate(() -> executeCommand("/show", null),
                0, AUTO_REFRESH_INTERVAL_SECONDS, TimeUnit.SECONDS);

        List<TextField> filterFields = List.of(
                filterIdField,
                filterNameField,
                filterXField,
                filterYField,
                filterCreationField,
                filterAreaField,
                filterRoomsField,
                filterFurnishField,
                filterViewField,
                filterTransportField,
                filterHouseNameField,
                filterHouseYearField,
                filterHouseLiftsField,
                filterOwnerIdField
        );

        filterFields.forEach(field -> {
            if (field != null) {
                field.textProperty().addListener(
                        (observable, oldValue, newValue) -> filterAndRender()
                );
            }
        });

        Platform.runLater(() -> {
            Stage stage = (Stage) mainPane.getScene().getWindow();
            stage.setOnCloseRequest(event -> {
                if (!scheduledExecutorService.isShutdown()) {
                    scheduledExecutorService.shutdownNow(); // Принудительно завершаем все задачи
                    System.out.println("Background Executor stopped.");
                }
            });

        });
    }

    /**
     * Handles the action when the "Remove by ID" button is clicked.
     * Attempts to parse the ID from the `removeIdField` and executes the `/remove_by_id` command.
     */
    @FXML
    private void onRemoveById() {
        String idText = removeIdField.getText();
        if (idText.matches("\\d+")) {
            executeCommand("/remove_by_id " + idText, null);
        } else {
            showAlert("Invalid ID. Please enter a number.", true);
        }
    }

    /**
     * Handles the action when the "Remove Any by View" button is clicked.
     * Uses the selected {@link View} from `viewChoiceBox` and executes the `/remove_any_by_view` command.
     */
    @FXML
    private void onRemoveAnyByView() {
        View selectedView = viewChoiceBox.getValue();
        if (selectedView != null) {
            executeCommand("/remove_any_by_view " + selectedView.toString(), null);
        } else {
            showAlert("Please select a View to remove.", true);
        }
    }

    /**
     * Handles the action when the "Count Greater Than Furnish" button is clicked.
     * Uses the selected {@link Furnish} from `furnishChoiceBox` and executes the `/count_greater_than_furnish` command.
     */
    @FXML
    private void onCountGreaterThanFurnish() {
        Furnish selected = furnishChoiceBox.getValue();
        if (selected != null) {
            executeCommand("/count_greater_than_furnish " + selected, null);
        } else {
            showAlert("Please select a Furnish type to count.", true);
        }
    }

    /**
     * Handles the action when the "Execute Script" button is clicked.
     * Reads the script path from `scriptPathField` and executes the `/execute_script` command.
     */
    @FXML
    private void onExecuteScript() {
        String path = scriptPathField.getText();
        if (!path.isBlank()) {
            executeCommand("/execute_script " + path, null);
        } else {
            showAlert("Script path cannot be empty.", true);
        }
    }

    /**
     * Sets up the columns for the `flatsTable`.
     * Each column is created with its header text corresponding to the original Flat field names
     * and a cell value factory to extract the correct property from a {@link Flat} object.
     * Includes "Actions" column with "Update" and "Delete" buttons.
     */
    private void setupTableColumns() {
        flatsTable.getColumns().clear();

        TableColumn<Flat, Long> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> new SimpleLongProperty(data.getValue().getId()).asObject());

        TableColumn<Flat, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));

        TableColumn<Flat, Double> coordXCol = new TableColumn<>("X");
        coordXCol.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getCoordinates().x()).asObject());

        TableColumn<Flat, Long> coordYCol = new TableColumn<>("Y");
        coordYCol.setCellValueFactory(data -> new SimpleLongProperty(data.getValue().getCoordinates().y()).asObject());

        TableColumn<Flat, String> creationDateCol = new TableColumn<>("Creation Date");
        creationDateCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCreationDate().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));

        TableColumn<Flat, Float> areaCol = new TableColumn<>("Area");
        areaCol.setCellValueFactory(data -> new SimpleFloatProperty(data.getValue().getArea()).asObject());

        TableColumn<Flat, Integer> roomsCol = new TableColumn<>("Number Of Rooms");
        roomsCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getNumberOfRooms()).asObject());

        TableColumn<Flat, String> furnishCol = new TableColumn<>("Furnish");
        furnishCol.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getFurnish() != null ? data.getValue().getFurnish().toString() : "")); // Изменено на пустую строку

        TableColumn<Flat, String> viewCol = new TableColumn<>("View");
        viewCol.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getView() != null ? data.getValue().getView().toString() : "")); // Изменено на пустую строку

        TableColumn<Flat, String> transportCol = new TableColumn<>("Transport");
        transportCol.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getTransport() != null ? data.getValue().getTransport().toString() : "")); // Изменено на пустую строку

        TableColumn<Flat, String> houseNameCol = new TableColumn<>("House Name");
        houseNameCol.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getHouse() != null ? data.getValue().getHouse().getName() : "")); // Изменено на пустую строку

        TableColumn<Flat, Long> houseYearCol = new TableColumn<>("House Year");
        houseYearCol.setCellValueFactory(data -> {
            if (data.getValue().getHouse() != null && data.getValue().getHouse().getYear() != null) {
                return new SimpleLongProperty(data.getValue().getHouse().getYear()).asObject();
            } else {
                return null;
            }
        });

        TableColumn<Flat, Long> houseLiftsCol = new TableColumn<>("House Number Of Lifts");
        houseLiftsCol.setCellValueFactory(data -> {
            if (data.getValue().getHouse() != null && data.getValue().getHouse().getNumberOfLifts() != null) {
                return new SimpleLongProperty(data.getValue().getHouse().getNumberOfLifts()).asObject();
            } else {
                return null;
            }
        });

        TableColumn<Flat, String> userOwnerIdCol = new TableColumn<>("User Owner ID");
        userOwnerIdCol.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getUserOwnerID())));

        flatsTable.getColumns().addAll(idCol, nameCol, coordXCol, coordYCol, creationDateCol,
                areaCol, roomsCol, furnishCol, viewCol, transportCol, houseNameCol, houseYearCol, houseLiftsCol, userOwnerIdCol);

        TableColumn<Flat, Void> actionsCol = addActionsColumn();

        flatsTable.getColumns().add(actionsCol);
    }

    private TableColumn<Flat, Void> addActionsColumn() {
        TableColumn<Flat, Void> actionsCol = new TableColumn<>("Actions");

        actionsCol.setCellFactory(col -> new TableCell<>() {
            private final Button updateButton = new Button();
            private final Button deleteButton = new Button();

            {
                FontIcon iconUpd = new FontIcon("fas-edit");
                iconUpd.setIconSize(14);
                updateButton.setGraphic(iconUpd);
                updateButton.setOnAction(event -> {
                    Flat flat = getTableView().getItems().get(getIndex());
                    openFlatUpdateForm(flat);
                });

                FontIcon iconDel = new FontIcon("fas-trash");
                iconDel.setIconSize(14);
                deleteButton.setGraphic(iconDel);
                deleteButton.setOnAction(event -> {
                    Flat flat = getTableView().getItems().get(getIndex());
                    executeCommand("/remove_by_id " + flat.getId(), null);
                });

                updateButton.setStyle("-fx-cursor: hand;");
                deleteButton.setStyle("-fx-cursor: hand;");
            }

            private final HBox actionBox = new HBox(5, updateButton, deleteButton);

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(actionBox);
                }
            }
        });
        return actionsCol;
    }

    @FXML
    private void onCommand(ActionEvent event) {
        Object source = event.getSource();
        if (source instanceof Button btn) {
            String command = (String) btn.getUserData();
            executeCommand("/" + command, null);
        }
    }

    /**
     * Executes a given command using the {@link Handler}.
     * Displays an alert with the command's response.
     * If the response indicates a collection update (e.g., from `/show`),
     * it deserializes the new data and updates the table and canvas.
     *
     * @param command The command string to execute.
     */
    private void executeCommand(String command, FlatBuilder flatBuilder) {
        scheduledExecutorService.execute(() -> {
            Handler handler = new Handler(flatBuilder);
            Response response = handler.handle(command);
            Platform.runLater(() -> {
                if (command.equals("/show")) {
                    try {
                        allFlats = objectMapper.readValue(response.message(), new TypeReference<List<Flat>>() {});
                        filterAndRender();
                    } catch (Exception e) {
                        System.err.println("JSON parsing error for command '" + command + "': " + e.getMessage());
                    }
                } else if(response.responseType() == ResponseType.COLLECTION_UPDATE) {
                    executeCommand("/show", null);
                    showAlert(response.message(), false);
                }else {
                    showAlert(response.message(), response.responseType() == ResponseType.ERROR);
                }
            });
        });
    }

    @FXML
    private void onChooseScriptFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите файл скрипта");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt", "*.sh", "*.bat"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        Stage stage = (Stage) mainPane.getScene().getWindow();

        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            scriptPathField.setText(selectedFile.getAbsolutePath());
        }
    }

    /**
     * Displays an alert dialog with a given message.
     * The type of the alert (INFORMATION or ERROR) can be controlled by the isError flag.
     *
     * @param message The message to display in the alert.
     * @param isError If true, an ERROR alert is shown; otherwise, an INFORMATION alert is shown.
     */
    private void showAlert(String message, boolean isError){
        Alert.AlertType alertType = isError ? Alert.AlertType.ERROR : Alert.AlertType.INFORMATION;
        Alert alert = new Alert(alertType);
        alert.setTitle(isError ? "Ошибка сервера" : "Ответ сервера"); // Изменяем заголовок в зависимости от типа
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Handles the "Add Flat" action. Opens a form for adding a new flat.
     * If a flat is successfully added, the collection data is refreshed.
     *
     * @param event The {@link ActionEvent} that triggered this method.
     */
    @FXML
    private void openFlatForm(ActionEvent event) {
        Button btn = (Button) event.getSource();
        String command = (String) btn.getUserData();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ru/sdmitriy612/gui/fxml/flat_form.fxml"));
            Parent root = loader.load();

            FlatFormController controller = loader.getController();
            Stage stage = new Stage();
            stage.setTitle("Add Flat");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            FlatBuilder builder = controller.getResult();
            if (builder != null) {
                executeCommand("/" + command, builder);
            }

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error loading flat form: " + e.getMessage(), true);
        }
    }

    /**
     * Opens a form for updating information about a specific {@link Flat}.
     * This method is called from both the TableView's "Update" action and
     * the Flat Info Dialog's "Edit" button.
     * After successful update, the collection data is refreshed.
     *
     * @param flat The {@link Flat} object to be updated.
     */
    private void openFlatUpdateForm(Flat flat) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ru/sdmitriy612/gui/fxml/flat_form.fxml"));
            Parent root = loader.load();

            FlatFormController controller = loader.getController();
            controller.setFlatToUpdate(flat);

            Stage stage = new Stage();
            stage.setTitle("Update Flat");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            FlatBuilder builder = controller.getResult();
            if (builder != null) {
                executeCommand("/update " + flat.getId(), builder);
            }

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error loading flat update form: " + e.getMessage(), true);
        }
    }

    /**
     * Opens a dialog window to display detailed information about a {@link Flat}.
     * The dialog includes an "Edit" button that will call {@link #openFlatUpdateForm(Flat)}
     * for the displayed flat.
     * The dialog is non-modal, allowing interaction with the main window.
     *
     * @param flat The {@link Flat} object for which to display information.
     */
    private void openFlatInfoDialog(Flat flat) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ru/sdmitriy612/gui//fxml/flat_info_dialog.fxml"));
            Parent root = fxmlLoader.load();
            FlatInfoController controller = fxmlLoader.getController();
            controller.setFlat(flat);

            controller.setOnEdit(this::openFlatUpdateForm);

            if (flatInfoStage == null) {
                flatInfoStage = new Stage();
                flatInfoStage.setTitle("Flat Information");
                flatInfoStage.initModality(Modality.NONE);
                flatInfoStage.setScene(new Scene(root));
            } else {
                flatInfoStage.getScene().setRoot(root);
                flatInfoStage.setTitle("Flat Information: " + flat.getName());
            }

            flatInfoStage.show();
            if (flatInfoStage.isShowing()) {
                flatInfoStage.toFront();
            }

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error loading flat information window: " + e.getMessage(), true);
        }
    }

    /**
     * Applies the current filter criteria from the filter text fields to the
     * {@code allFlats} list and updates both the {@code flatsTable} and {@code flatCanvas}
     * with the filtered results.
     */
    private void filterAndRender() {
        FlatFilter flatFilter = new FlatFilter(
                filterIdField.getText().trim(),
                filterNameField.getText().trim(),
                filterXField.getText().trim(),
                filterYField.getText().trim(),
                filterCreationField.getText().trim(),
                filterAreaField.getText().trim(),
                filterRoomsField.getText().trim(),
                filterFurnishField.getText().trim(),
                filterViewField.getText().trim(),
                filterTransportField.getText().trim(),
                filterHouseNameField.getText().trim(),
                filterHouseYearField.getText().trim(),
                filterHouseLiftsField.getText().trim(),
                filterOwnerIdField.getText().trim()
        );

        List<Flat> filtered = flatFilter.filter(allFlats);

        List<TableColumn<Flat, ?>> sortOrder = new ArrayList<>(flatsTable.getSortOrder());
        flatsTable.getSortOrder().clear();
        flatsTable.getItems().setAll(filtered);
        flatsTable.getSortOrder().addAll(sortOrder);
        if (!sortOrder.isEmpty()) {
            flatsTable.sort();
        }
        if (flatCanvas != null) {
            flatCanvas.setFlats(filtered);
        }
    }

    /**
     * Handles the "Exit" action. Closes the application window.
     *
     * @param event The {@link ActionEvent} that triggered this method.
     */
    @FXML
    private void onExit(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}