package ru.sdmitriy612.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.sdmitriy612.clientapp.handling.Handler;
import ru.sdmitriy612.interactors.Response;
import ru.sdmitriy612.interactors.ResponseType;
import ru.sdmitriy612.utils.HashSha512;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;


public class AuthWindowController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label messageLabel;

    private final Handler handler = new Handler();

    @FXML
    private void onCommand(ActionEvent event){
        Button btn = (Button) event.getSource();
        String command = (String) btn.getUserData();

        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Пожалуйста, заполните все поля");
            return;
        }
        String hashedPwd = HashSha512.hash(password);
        Response response = handler.handle("/" + command + " " + username + " " + hashedPwd);

        if (response.responseType() == ResponseType.SUCCESS) {
            messageLabel.setText("");
            closeWindow();
        } else {
            messageLabel.setText(response.message());
        }
    }


    private void closeWindow() {
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.close();
    }
}
