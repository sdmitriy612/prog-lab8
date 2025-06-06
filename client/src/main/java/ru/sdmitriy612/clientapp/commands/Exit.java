package ru.sdmitriy612.clientapp.commands;

import ru.sdmitriy612.interactors.Request;
import ru.sdmitriy612.interactors.Response;

/**
 * Command that exits the program.
 * <p>
 * This command terminates the application when executed.
 * </p>
 */
public class Exit extends Command{

    /**
     * Executes the exit command, terminating the program.
     *
     * @param request the request associated with the exit command (not used in this case)
     * @return a {@link Response} indicating that the program is exiting
     */
    @Override
    public Response execute(Request request) {
        System.exit(0);
        return new Response("Exiting...");
    }
}
