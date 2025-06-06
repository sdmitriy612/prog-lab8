package ru.sdmitriy612;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.sdmitriy612.clientapp.Session;

public class Main extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        showAuthWindow();
    }

    private void showAuthWindow() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ru/sdmitriy612/gui/fxml/auth_window.fxml"));
        Scene scene = new Scene(loader.load());
        Stage authStage = new Stage();
        authStage.setTitle("Flat Collector - Authorization");
        authStage.setScene(scene);
        authStage.initModality(Modality.APPLICATION_MODAL); // модальное окно
        authStage.showAndWait();

        if (Session.getInstance().getUserAuthorization() != null) {
            showMainWindow();
        } else {
            primaryStage.close();
        }
    }

    private void showMainWindow() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ru/sdmitriy612/gui/fxml/main_window.fxml"));
        Scene scene = new Scene(loader.load(), 800, 600);
        primaryStage.setTitle("Flat Collector");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
