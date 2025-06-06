package ru.sdmitriy612.command;

import ru.sdmitriy612.collection.CollectionManager;
import ru.sdmitriy612.collection.flat.Flat;
import ru.sdmitriy612.interactors.Request;
import ru.sdmitriy612.interactors.Response;
import ru.sdmitriy612.interactors.ResponseType;

import java.util.Comparator;
import java.util.Set;
import java.util.Optional;

/**
 * Command that adds an element to the collection if it is greater than all existing elements.
 * <p>
 * This command compares the new element (flat) with the maximum element in the collection,
 * and only adds the new element if it is greater than the current maximum element in the collection.
 * </p>
 */
public class AddIfMax extends Command{
    /**
     * Constructs the command with a description and command type.
     */
    protected AddIfMax() {
        super("adds element to the collection if it greater than existing elements", CommandType.FLAT_ENTER);
    }
    /**
     * Executes the command to add an element to the collection if it is greater than all existing elements.
     *
     * @param request the request containing the element to be added to the collection
     * @return a {@link Response} indicating the result of the operation
     */
    @Override
    public Response execute(CommandArgs request) {
        if(request.builder() == null) return new Response("enter a flat to add");
        Flat newElement = request.builder().build();
        CollectionManager flatCollectionManager = CollectionManager.getInstance();
        Set<Flat> collection = flatCollectionManager.getCollection();

        Optional<Flat> maxFlatOptional = collection.stream().max(Comparator.naturalOrder());

        if (maxFlatOptional.isEmpty() || newElement.compareTo(maxFlatOptional.get()) > 0) {
            if(CollectionManager.getInstance().addElement(request.builder().build(), request.user().getId())){
                return new Response("element added to the collection", ResponseType.COLLECTION_UPDATE);
            }else return new Response("error in add element");
        }else return new Response("element isn't greater than max element in collection");

    }
}
