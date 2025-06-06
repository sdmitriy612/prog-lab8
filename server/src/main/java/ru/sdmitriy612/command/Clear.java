package ru.sdmitriy612.command;

import ru.sdmitriy612.collection.CollectionManager;
import ru.sdmitriy612.collection.flat.Flat;
import ru.sdmitriy612.interactors.Response;
import ru.sdmitriy612.interactors.ResponseType;

import java.util.Set;

/**
 * Command that clears the entire collection.
 * <p>
 * This command removes all elements from the collection.
 * </p>
 */
public class Clear extends Command{
    /**
     * Constructs the command with a description and command type.
     */
    protected Clear() {
        super("clears the collection", CommandType.PRIMITIVE);
    }
    /**
     * Executes the command to clear the collection.
     *
     * @param request the request to execute the command (not used in this case)
     * @return a {@link Response} indicating that the collection has been cleared
     */
    @Override
    public Response execute(CommandArgs request) {
        Set<Flat> collection = CollectionManager.getInstance().getCollection();
        collection.stream()
                .filter(element -> request.user().getId() == element.getUserOwnerID())
                .forEach(element -> {
                    CollectionManager.getInstance().removeElement(element, request.user().getId());
                });
        return new Response("your collection has been cleaned", ResponseType.COLLECTION_UPDATE);
    }
}