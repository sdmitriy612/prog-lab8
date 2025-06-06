package ru.sdmitriy612.command;

import ru.sdmitriy612.collection.CollectionManager;
import ru.sdmitriy612.interactors.Request;
import ru.sdmitriy612.interactors.Response;
import ru.sdmitriy612.interactors.ResponseType;

/**
 * Command that exits the program.
 * <p>
 * This command terminates the application when executed.
 * </p>
 */
public class Exit extends Command{

    /**
     * Constructs the command with a description and command type.
     */
    public Exit() {
        super("exits program", CommandType.CLIENT_OR_SERVER);
    }
    /**
     * Executes the exit command, terminating the program.
     *
     * @param request the request associated with the exit command (not used in this case)
     * @return a {@link Response} indicating that the program is exiting
     */
    @Override
    public Response execute(CommandArgs request) {
        CollectionManager.getInstance().save();
        System.exit(0);
        return new Response("Exiting");
    }
}
