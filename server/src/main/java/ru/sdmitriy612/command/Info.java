package ru.sdmitriy612.command;

import ru.sdmitriy612.collection.CollectionManager;
import ru.sdmitriy612.interactors.Request;
import ru.sdmitriy612.interactors.Response;
/**
 * Command that displays information about the collection.
 * <p>
 * This command retrieves and displays details about the current state of the collection,
 * including the creation date and the number of elements in the collection.
 * </p>
 */
public class Info extends Command{
    /**
     * Constructs the command with a description and command type.
     */
    public Info(){
        super("displays information of collection", CommandType.PRIMITIVE);
    }
    /**
     * Executes the info command, displaying details about the collection.
     *
     * @param request the request associated with the info command (not used in this case)
     * @return a {@link Response} containing the information about the collection
     */
    @Override
    public Response execute(CommandArgs request) {
        return new Response(CollectionManager.getInstance().toString());
    }
}
