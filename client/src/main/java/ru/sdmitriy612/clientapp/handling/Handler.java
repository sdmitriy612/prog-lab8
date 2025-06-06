package ru.sdmitriy612.clientapp.handling;

import ru.sdmitriy612.clientapp.Session;
import ru.sdmitriy612.clientapp.commands.ClientCommandsMap;
import ru.sdmitriy612.clientapp.commands.Command;
import ru.sdmitriy612.collection.flat.FlatBuilder;
import ru.sdmitriy612.interactors.*;
import ru.sdmitriy612.io.StringIOController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Handles client-side request processing and communication with the server.
 * Parses user input, builds appropriate {@link Request} objects,
 * and handles server responses including conditional client-side interactions.
 */
public class Handler {

    private final StringIOController stringIOController;

    /**
     * Constructs a Handler with a specified input/output controller.
     *
     * @param stringIOController controller for string input/output operations
     */
    public Handler(StringIOController stringIOController){
        this.stringIOController = stringIOController;
    }

    /**
     * Processes a command string entered by the user.
     * Interacts with the server, and if required, performs additional input
     * or executes client-side commands.
     *
     * @param requestString the raw command input string
     * @return the result message from the server or client-side command
     */
    public String handle(String requestString){

        if (requestString.isBlank()) return "enter correct request";

        ArrayList<String> requestData = new ArrayList<>(List.of(requestString.split(" ")));
        String commandName = requestData.remove(0);

        Session session = Session.getInstance();
        UserAuthorization sessionAuthorization = session.getUserAuthorization();

        Request request = new Request(commandName, requestData, sessionAuthorization);
        Response response = ServerCommunication.serverRequest(request);

        if(response.responseType() != ResponseType.ERROR) session.updateHistory(commandName);

        switch (response.responseType()) {
            case ENTER_OBJECT -> {
                ElementInput elementInput = new ElementInput();
                FlatBuilder flatBuilder = elementInput.inputFlat(stringIOController);
                response = ServerCommunication.serverRequest(new Request(commandName, requestData, sessionAuthorization, flatBuilder));
            }
            case CLIENT -> {
                HashMap<String, Command> clientCommandsMap = ClientCommandsMap.getCommands();
                response = clientCommandsMap.get(request.commandName()).execute(request);
            }
            case AUTHORIZE -> {
                CredentialsInput credentialsInput = new CredentialsInput();
                UserAuthorization inputAuthorization = credentialsInput.inputCredentials(stringIOController);
                if(inputAuthorization == null) return "";
                Session.getInstance().setUserAuthorization(inputAuthorization);
                response = ServerCommunication.serverRequest(new Request(commandName, requestData, inputAuthorization));
            }
        }

        return response.message();
    }

}
