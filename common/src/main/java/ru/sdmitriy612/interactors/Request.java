package ru.sdmitriy612.interactors;

import ru.sdmitriy612.collection.flat.FlatBuilder;

import java.io.Serializable;
import java.util.List;
/**
 * Represents a client request to be sent to the server.
 * Contains the command name, its arguments, and an optional {@link FlatBuilder} object.
 * Implements {@link Interactor} for communication purposes and {@link Serializable} for transmission.
 *
 * @param commandName name of the command to execute
 * @param args        list of string arguments for the command
 * @param builder     optional data object used for commands that require complex input
 */
public record Request(String commandName, List<String> args, UserAuthorization userAuthorization, FlatBuilder builder) implements Interactor {
    /**
     * Creates a request with no builder.
     *
     * @param commandName name of the command
     */
    public Request(String commandName, List<String> args, UserAuthorization userAuthorization){
        this(commandName, args, userAuthorization, null);
    }

    /**
     * Creates a request with no builder and userAuthorization.
     *
     * @param commandName name of the command
     */
    public Request(String commandName, List<String> args){
        this(commandName, args, null, null);
    }

}
