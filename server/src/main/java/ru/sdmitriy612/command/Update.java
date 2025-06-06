package ru.sdmitriy612.command;

import ru.sdmitriy612.collection.CollectionManager;
import ru.sdmitriy612.collection.flat.Flat;
import ru.sdmitriy612.interactors.Request;
import ru.sdmitriy612.interactors.Response;
import ru.sdmitriy612.interactors.ResponseType;

import java.util.Set;
import java.util.Optional;

/**
 * Command that updates the value of an item in the collection based on the given id.
 * <p>
 * This command looks for an element with the specified id and replaces it with the provided element
 * if such an element exists in the collection.
 * </p>
 */
public class Update extends Command{
    /**
     * Constructs the command with a description and command type.
     */
    protected Update() {
        super("updates the value of the collection item whose id is equal to the given one",
                CommandType.FLAT_ENTER);
    }
    /**
     * Executes the command to update an element in the collection by its id.
     *
     * @param request the request object containing the id and the element to update
     * @return a {@link Response} indicating the result of the operation (either success or failure)
     */
    @Override
    public Response execute(CommandArgs request) {
        if(request.args() == null || request.args().isEmpty()) return new Response("enter an id to update");
        if(request.args().size() > 1) return new Response("enter only id");

        long id;
        try{
            id = Long.parseLong(request.args().get(0));
        }catch (NumberFormatException e){
            return new Response("id must be digit");
        }

        if(request.builder() == null) return new Response("enter a flat to add");
        Flat newElement = request.builder().build();
        newElement.setId(id);

        Set<Flat> collection = CollectionManager.getInstance().getCollection();

        Optional<Flat> flatToRemove = collection.stream()
                .filter(element -> element.getId() == id)
                .filter(element -> request.user().getId() == element.getUserOwnerID())
                .findAny();

        CollectionManager collectionManager = CollectionManager.getInstance();
        
        if(flatToRemove.isEmpty()){
            return new Response("your collection doesn't include element with id " + id, ResponseType.ERROR);
        }else{
            if(collectionManager.removeElement(flatToRemove.get(), request.user().getId())){
                collectionManager.addElement(newElement, request.user().getId());
            }
            return new Response("element was updated", ResponseType.COLLECTION_UPDATE);
        }


    }
}