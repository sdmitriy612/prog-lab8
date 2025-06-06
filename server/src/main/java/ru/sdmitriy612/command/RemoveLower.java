package ru.sdmitriy612.command;

import ru.sdmitriy612.collection.CollectionManager;
import ru.sdmitriy612.collection.flat.Flat;
import ru.sdmitriy612.interactors.Request;
import ru.sdmitriy612.interactors.Response;
import ru.sdmitriy612.interactors.ResponseType;

import java.util.Set;

/**
 * Command that removes all elements from the collection that are "lower" than the given element
 * based on a comparison.
 * <p>
 * This command iterates through the collection and removes all elements where the comparison
 * of their values is less than the given element.
 * </p>
 */
public class RemoveLower extends Command{
    /**
     * Constructs the command with a description and command type.
     */
    protected RemoveLower() {
        super("removes one element which view is equals to given", CommandType.FLAT_ENTER);
    }
    /**
     * Executes the remove lower command.
     * It removes all elements in the collection that are considered "lower" than the provided element
     * based on a comparison of their values.
     *
     * @param request the request containing the element to compare with
     * @return a {@link Response} indicating the result of the operation
     */
    @Override
    public Response execute(CommandArgs request) {
        if(request.builder() == null) return new Response("enter a flat to compare");
        Flat comparingElement = request.builder().build();
        CollectionManager flatCollectionManager = CollectionManager.getInstance();
        Set<Flat> collection = flatCollectionManager.getCollection();

        long removedCount = collection.stream()
                .filter(e -> e.compareTo(comparingElement) < 0)
                .filter(e -> e.getUserOwnerID() == request.user().getId())
                .filter(e -> CollectionManager.getInstance().removeElement(e, request.user().getId()))
                .count();

        return removedCount > 0 ? new Response(removedCount + " elements lower than given were deleted", ResponseType.COLLECTION_UPDATE)
                : new Response("collection doesn't include elements lower than given");
    }
}