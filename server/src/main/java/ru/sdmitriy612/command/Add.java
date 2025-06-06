package ru.sdmitriy612.command;

import ru.sdmitriy612.collection.CollectionManager;
import ru.sdmitriy612.interactors.Request;
import ru.sdmitriy612.interactors.Response;
import ru.sdmitriy612.interactors.ResponseType;

/**
 * Command that adds an element to the collection.
 * <p>
 * This command adds a new element to the collection managed by {@link CollectionManager}.
 * </p>
 */
public class Add extends Command{
    /**
     * Constructs the command with a description and command type.
     */
    protected Add() {
        super("adds element to the collection", CommandType.FLAT_ENTER);
    }
    /**
     * Executes the command to add an element to the collection.
     *
     * @param request the request containing the element to be added to the collection
     * @return a {@link Response} indicating the result of the operation
     */
    @Override
    public Response execute(CommandArgs request) {
        if(request.builder() == null) return new Response("enter a flat to add");
        if(CollectionManager.getInstance().addElement(request.builder().build(), request.user().getId())){
            return new Response("element added to the collection", ResponseType.COLLECTION_UPDATE);
        }else return new Response("error in add element");

    }
}
