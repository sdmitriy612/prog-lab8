package ru.sdmitriy612.clientapp.handling;

import ru.sdmitriy612.interactors.UserAuthorization;
import ru.sdmitriy612.io.StringIOController;
import ru.sdmitriy612.io.console.BufferedConsoleIOController;
import ru.sdmitriy612.io.console.ConsoleIOController;
import ru.sdmitriy612.utils.HashSha512;

public class CredentialsInput {
    private StringIOController stringIOController;
    public UserAuthorization inputCredentials(StringIOController stringIOController){

        try{
            String login = stringIOController.read("Login: ");
            String password = stringIOController.read("Password: ");
            if(login.isBlank()) stringIOController.error("Login cannot be empty");
            if(password.isBlank()) stringIOController.error("Password cannot be empty");

            return new UserAuthorization(login, HashSha512.hash(password));

        }catch (Exception e){
            stringIOController.error("Error during authorization");
            return null;
        }
    }
}
