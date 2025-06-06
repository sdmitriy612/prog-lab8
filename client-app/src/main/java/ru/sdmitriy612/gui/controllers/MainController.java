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
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
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
    @FXML private Pane visualizationPane;

    @FXML private ChoiceBox<String> languageChoiceBox;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());
    private List<Flat> allFlats = List.of();

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
    @FXML private BorderPane mainPane;

    private FlatCanvas flatCanvas;
    private Stage flatInfoStage;

    private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    private static final int AUTO_REFRESH_INTERVAL_SECONDS = 10;

    private static final ObjectProperty<Locale> currentLocaleProperty = new SimpleObjectProperty<>(Locale.getDefault());

    private static final Map<String, Locale> supportedLocales = Map.of(
            getLocalizedMessageStatic("ru_RU"), new Locale("ru", "RU"),
            getLocalizedMessageStatic("fi_FI"), new Locale("fi", "FI"),
            getLocalizedMessageStatic("it_IT"), new Locale("it", "IT"),
            getLocalizedMessageStatic("es_NI"), new Locale("es", "NI")
    );


    private static String getLocalizedMessageStatic(String key) {
        return ResourceBundle.getBundle("ru.sdmitriy612.gui.localization.Messages", Locale.getDefault()).getString(key);
    }

    private String getLocalizedMessage(String key) {
        return ResourceBundle.getBundle("ru.sdmitriy612.gui.localization.Messages", currentLocaleProperty.get()).getString(key);
    }


    public static Parent loadMainView(Locale initialLocale) throws IOException {
        currentLocaleProperty.set(initialLocale);

        FXMLLoader fxmlLoader = new FXMLLoader(MainController.class.getResource("/ru/sdmitriy612/gui/fxml/main_window.fxml"));

        fxmlLoader.setResources(ResourceBundle.getBundle("ru.sdmitriy612.gui.localization.Messages", initialLocale));

        return fxmlLoader.load();
    }

    /**
     * Initializes the controller. This method is called after the FXML file has been loaded.
     * Sets up the ObjectMapper, initializes the TableView columns,
     * instantiates and binds the FlatCanvas, sets up click listeners for filtering,
     * and performs an initial data load.
     */
    @FXML
    private void initialize() {
        currentUserLabel.setText(Session.getInstance().getUserAuthorization().login());

        flatCanvas = new FlatCanvas();
        visualizationPane.getChildren().add(flatCanvas);
        AnchorPane.setTopAnchor(flatCanvas, 0.0);
        AnchorPane.setBottomAnchor(flatCanvas, 0.0);
        AnchorPane.setLeftAnchor(flatCanvas, 0.0);
        AnchorPane.setRightAnchor(flatCanvas, 0.0);

        flatCanvas.setOnFlatSelectedForInfo(this::openFlatInfoDialog);

        setupTableColumns();

        List<String> displayNames = new ArrayList<>(supportedLocales.keySet());
        languageChoiceBox.setItems(FXCollections.observableArrayList(displayNames));


        String currentLangDisplayName = supportedLocales.entrySet().stream()
                .filter(entry -> entry.getValue().equals(currentLocaleProperty.get()))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(getLocalizedMessage("ru_RU")); // Дефолтное значение, если текущий язык не найден

        languageChoiceBox.setValue(currentLangDisplayName);

        languageChoiceBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.equals(oldVal)) {
                Locale newLocale = supportedLocales.get(newVal);
                if (newLocale != null && !newLocale.equals(currentLocaleProperty.get())) {
                    currentLocaleProperty.set(newLocale);
                    try {
                        Stage stage = (Stage) mainPane.getScene().getWindow();
                        Parent root = MainController.loadMainView(newLocale);
                        stage.setScene(new Scene(root));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

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
            field.textProperty().addListener(
                    (observable, oldValue, newValue) -> filterAndRender()
            );
        });

        Platform.runLater(() -> {
            Stage stage = (Stage) mainPane.getScene().getWindow();
            stage.setOnCloseRequest(event -> {
                if (!scheduledExecutorService.isShutdown()) {
                    scheduledExecutorService.shutdownNow();
                    System.exit(0);
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
            showAlert(getLocalizedMessage("invalidId"), true);
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
            executeCommand("/remove_any_by_view " + selectedView, null);
        } else {
            showAlert(getLocalizedMessage("selectViewPrompt"), true);
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
            showAlert(getLocalizedMessage("selectFurnishPrompt"), true);
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
            showAlert(getLocalizedMessage("scriptPathEmpty"), true);
        }
    }

    /**
     * Sets up the columns for the `flatsTable`.
     * Each column is created with its header text corresponding to the original Flat field names
     * and a cell value factory to extract the correct property from a {@link Flat} object.
     * Includes "Actions" column with "Update" and "Delete" buttons.
     */
    @SuppressWarnings("unchecked")
    private void setupTableColumns() {
        flatsTable.getColumns().clear();

        TableColumn<Flat, Long> idCol = new TableColumn<>(getLocalizedMessage("columnId"));
        idCol.setCellValueFactory(data -> new SimpleLongProperty(data.getValue().getId()).asObject());

        TableColumn<Flat, String> nameCol = new TableColumn<>(getLocalizedMessage("columnName"));
        nameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));

        TableColumn<Flat, Double> coordXCol = new TableColumn<>(getLocalizedMessage("columnX"));
        coordXCol.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getCoordinates().x()).asObject());

        TableColumn<Flat, Long> coordYCol = new TableColumn<>(getLocalizedMessage("columnY"));
        coordYCol.setCellValueFactory(data -> new SimpleLongProperty(data.getValue().getCoordinates().y()).asObject());

        TableColumn<Flat, String> creationDateCol = new TableColumn<>(getLocalizedMessage("columnCreationDate"));
        creationDateCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCreationDate().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));

        TableColumn<Flat, Float> areaCol = new TableColumn<>(getLocalizedMessage("columnArea"));
        areaCol.setCellValueFactory(data -> new SimpleFloatProperty(data.getValue().getArea()).asObject());

        TableColumn<Flat, Integer> roomsCol = new TableColumn<>(getLocalizedMessage("columnRooms"));
        roomsCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getNumberOfRooms()).asObject());

        TableColumn<Flat, String> furnishCol = new TableColumn<>(getLocalizedMessage("columnFurnish"));
        furnishCol.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getFurnish() != null ? data.getValue().getFurnish().toString() : ""));

        TableColumn<Flat, String> viewCol = new TableColumn<>(getLocalizedMessage("columnView"));
        viewCol.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getView() != null ? data.getValue().getView().toString() : ""));

        TableColumn<Flat, String> transportCol = new TableColumn<>(getLocalizedMessage("columnTransport"));
        transportCol.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getTransport() != null ? data.getValue().getTransport().toString() : ""));

        TableColumn<Flat, String> houseNameCol = new TableColumn<>(getLocalizedMessage("columnHouseName"));
        houseNameCol.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getHouse() != null ? data.getValue().getHouse().getName() : ""));

        TableColumn<Flat, Long> houseYearCol = new TableColumn<>(getLocalizedMessage("columnHouseYear"));
        houseYearCol.setCellValueFactory(data -> {
            if (data.getValue().getHouse() != null && data.getValue().getHouse().getYear() != null) {
                return new SimpleLongProperty(data.getValue().getHouse().getYear()).asObject();
            } else {
                return null;
            }
        });

        TableColumn<Flat, Long> houseLiftsCol = new TableColumn<>(getLocalizedMessage("columnLifts"));
        houseLiftsCol.setCellValueFactory(data -> {
            if (data.getValue().getHouse() != null && data.getValue().getHouse().getNumberOfLifts() != null) {
                return new SimpleLongProperty(data.getValue().getHouse().getNumberOfLifts()).asObject();
            } else {
                return null;
            }
        });

        TableColumn<Flat, String> userOwnerIdCol = new TableColumn<>(getLocalizedMessage("columnUserOwnerId"));
        userOwnerIdCol.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getUserOwnerID())));

        flatsTable.getColumns().addAll(idCol, nameCol, coordXCol, coordYCol, creationDateCol,
                areaCol, roomsCol, furnishCol, viewCol, transportCol, houseNameCol, houseYearCol, houseLiftsCol, userOwnerIdCol);

        TableColumn<Flat, Void> actionsCol = addActionsColumn();

        flatsTable.getColumns().add(actionsCol);
    }

    private TableColumn<Flat, Void> addActionsColumn() {
        TableColumn<Flat, Void> actionsCol = new TableColumn<>(getLocalizedMessage("columnActions"));

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
     * @param flatBuilder {@link FlatBuilder} to handle command with flat interaction
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
                        System.err.println(getLocalizedMessage("jsonParsingError") + command + "': " + e.getMessage());
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
        fileChooser.setTitle(getLocalizedMessage("chooseScriptFileTitle"));

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(getLocalizedMessage("textFiles"), "*.txt", "*.sh", "*.bat"),
                new FileChooser.ExtensionFilter(getLocalizedMessage("allFiles"), "*.*")
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
        alert.setTitle(isError ? getLocalizedMessage("serverErrorTitle") : getLocalizedMessage("serverResponseTitle"));
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
            loader.setResources(ResourceBundle.getBundle("ru.sdmitriy612.gui.localization.Messages", currentLocaleProperty.get()));
            Parent root = loader.load();

            FlatFormController controller = loader.getController();
            Stage stage = new Stage();
            stage.setTitle(getLocalizedMessage("addFlatTitle"));
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            FlatBuilder builder = controller.getResult();
            if (builder != null) {
                executeCommand("/" + command, builder);
            }

        } catch (IOException e) {
            e.printStackTrace();
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
            loader.setResources(ResourceBundle.getBundle("ru.sdmitriy612.gui.localization.Messages", currentLocaleProperty.get()));

            Parent root = loader.load();

            FlatFormController controller = loader.getController();
            controller.setFlatToUpdate(flat);

            Stage stage = new Stage();
            stage.setTitle(getLocalizedMessage("updateFlatTitle"));
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            FlatBuilder builder = controller.getResult();
            if (builder != null) {
                executeCommand("/update " + flat.getId(), builder);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens a dialog window to display detailed information about a {@link Flat}.
     * The dialog includes an "Edit" button that will call {@link #openFlatUpdateForm(Flat)}
     * for the displayed flat.
     *
     * @param flat The {@link Flat} object for which to display information.
     */
    private void openFlatInfoDialog(Flat flat) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ru/sdmitriy612/gui/fxml/flat_info_dialog.fxml"));
            fxmlLoader.setResources(ResourceBundle.getBundle("ru.sdmitriy612.gui.localization.Messages", currentLocaleProperty.get()));
            Parent root = fxmlLoader.load();
            FlatInfoController controller = fxmlLoader.getController();
            controller.setFlat(flat);

            controller.setOnEdit(this::openFlatUpdateForm);

            if (flatInfoStage == null) {
                flatInfoStage = new Stage();
                flatInfoStage.setTitle(getLocalizedMessage("flatInformationTitle"));
                flatInfoStage.initModality(Modality.NONE);
                flatInfoStage.setScene(new Scene(root));
            } else {
                flatInfoStage.getScene().setRoot(root);
                flatInfoStage.setTitle(getLocalizedMessage("flatInformationTitle") + ": " + flat.getName());
            }

            flatInfoStage.show();
            if (flatInfoStage.isShowing()) {
                flatInfoStage.toFront();
            }

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(getLocalizedMessage("errorLoadingFlatInfoWindow") + e.getMessage(), true);
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