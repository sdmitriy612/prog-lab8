package ru.sdmitriy612.server.handling;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sdmitriy612.collection.CollectionManager;
import ru.sdmitriy612.command.Command;
import ru.sdmitriy612.command.CommandArgs;
import ru.sdmitriy612.command.CommandType;
import ru.sdmitriy612.command.CommandsMap;
import ru.sdmitriy612.interactors.Request;
import ru.sdmitriy612.interactors.Response;
import ru.sdmitriy612.interactors.ResponseType;
import ru.sdmitriy612.user.User;
import ru.sdmitriy612.interactors.UserAuthorization;
import ru.sdmitriy612.user.UserNotFoundException;

import java.util.ArrayDeque;
import java.util.HashMap;
/**
 * {@code Processor} handles the validation and execution of commands received from clients.
 * It processes requests based on their type and command validity.
 *
 */
public class Processor {
    private static final Logger logger = LogManager.getLogger(Processor.class);
    private final static int maxHistorySize = 12;
    /**
     * Processes the received request, validates the command, and returns the appropriate response.
     * It handles different command types (client/server).
     *
     * @param request the {@link Request} object containing the command data
     * @param isServer flag indicating whether the request is processed on the server-side
     * @return a {@link Response} based on the command and its validity
     */
    public static Response process(Request request, boolean isServer){
        logger.info("Server started process data from client. Data: {}", request.toString());

        HashMap<String, Command> commands = CommandsMap.getCommands();

        Command command = commands.getOrDefault(request.commandName(), null);

        if(command == null || !isServer && command.getCommandType() == CommandType.SERVER){
            return new Response("unknown command. Use /help for command list.", ResponseType.ERROR);
        }
        User user = new User(request.userAuthorization());

        if(command.getCommandType() != CommandType.AUTHORIZATION && command.getCommandType() != CommandType.CLIENT_OR_SERVER
            && !(command.getCommandType() == CommandType.SERVER && isServer)){
            if(request.userAuthorization() == null) return new Response("You are not authorized. Please call the /signin or /signup command to log in.");
            try{
                user.authorize();
            }catch (UserNotFoundException e){
                return new Response("Entered login or password is incorrect");
            }
        }

        Response response = handleCommandType(request, isServer, command);
        if (response != null) {
            return response;
        }

        return commands.get(request.commandName()).execute(new CommandArgs(request.args(), user, request.builder()));
    }

    /**
     * Handles the different types of commands and returns an appropriate response.
     *
     * @param request the request object
     * @param isServer flag indicating if the request is processed on the server-side
     * @param command the command being processed
     * @return a {@link Response} object or null if the command is valid
     */
    private static Response handleCommandType(Request request, boolean isServer, Command command) {

        if (request.builder() == null && command.getCommandType() == CommandType.FLAT_ENTER) {
            return new Response("Enter a Flat", ResponseType.ENTER_OBJECT);
        }

        if (!isServer && command.getCommandType() == CommandType.CLIENT_OR_SERVER) {
            return new Response("Client side", ResponseType.CLIENT);
        }

        return null;
    }
}
