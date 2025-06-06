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
    private FlatBuilder flatBuilder;
    private StringIOController stringIOController;

    public Handler(){

    }

    public Handler(StringIOController stringIOController){
        this.stringIOController = stringIOController;
    }

    public Handler(FlatBuilder flatBuilder){
        this.flatBuilder = flatBuilder;
    }
    /**
     * Processes a command string entered by the user.
     * Interacts with the server, and if required, performs additional input
     * or executes client-side commands.
     *
     * @param requestString the raw command input string
     * @return the result message from the server or client-side command
     */
    public Response handle(String requestString){

        System.out.println(requestString);

        ArrayList<String> requestData = new ArrayList<>(List.of(requestString.split(" ")));
        String commandName = requestData.remove(0);

        Session session = Session.getInstance();
        UserAuthorization sessionAuthorization = session.getUserAuthorization();

        Request request;

        if (commandName.equals("/signin") || commandName.equals("/signup")) {
            if (requestData.size() < 2) {
                return new Response("Недостаточно данных для авторизации");
            }

            UserAuthorization inputAuthorization = new UserAuthorization(requestData.get(0), requestData.get(1));
            session.setUserAuthorization(inputAuthorization); // сохраняем в сессию

            request = new Request(commandName, requestData, inputAuthorization);
        } else {

            request = new Request(commandName, requestData, sessionAuthorization, flatBuilder);
        }

        Response response = ServerCommunication.serverRequest(request);
        System.out.println(response);

        if(response.responseType() != ResponseType.ERROR && !commandName.equals("/show")) session.updateHistory(commandName);

        switch (response.responseType()) {
            case ENTER_OBJECT -> {
                if(flatBuilder == null){
                    ElementInput elementInput = new ElementInput();
                    flatBuilder = elementInput.inputFlat(stringIOController);
                }
                response = ServerCommunication.serverRequest(new Request(commandName, requestData, sessionAuthorization, flatBuilder));
            }
            case CLIENT -> {
                HashMap<String, Command> clientCommandsMap = ClientCommandsMap.getCommands();
                response = clientCommandsMap.get(request.commandName()).execute(request);
            }
        }

        return response;
    }

}
