package ru.sdmitriy612.clientapp.commands;

import ru.sdmitriy612.clientapp.Session;
import ru.sdmitriy612.interactors.Request;
import ru.sdmitriy612.interactors.Response;

/**
 * Command that displays the history of previously executed commands.
 * <p>
 * This command retrieves and displays the list of commands that have been executed
 * previously in the current session.
 * </p>
 */
public class History extends Command{
    /**
     * Executes the history command, displaying the history of commands that have been executed.
     *
     * @param request the request associated with the history command (not used in this case)
     * @return a {@link Response} containing the history of commands as a string
     */
    @Override
    public Response execute(Request request) {
        return new Response("Last commands:\n" + String.join("\n", Session.getInstance().getCommandsHistory()));
    }
}
