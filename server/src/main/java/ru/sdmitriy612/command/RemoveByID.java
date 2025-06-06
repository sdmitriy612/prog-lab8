package ru.sdmitriy612.command;

import ru.sdmitriy612.collection.CollectionManager;
import ru.sdmitriy612.collection.flat.Flat;
import ru.sdmitriy612.interactors.Response;
import ru.sdmitriy612.interactors.ResponseType;

import java.util.Set;
import java.util.Optional;

/**
 * Command that removes one element from the collection where the ID matches the specified value.
 * <p>
 * This command iterates through the collection and removes the first element
 * that matches the given ID.
 * </p>
 */
public class RemoveByID extends Command{
    /**
     * Constructs the command with a description and command type.
     */
    protected RemoveByID() {
        super("removes one element which id is equals to given", CommandType.PRIMITIVE);
    }
    /**
     * Executes the remove by ID command.
     * It removes the first element in the collection whose ID matches the provided value.
     *
     * @param request the request containing the ID of the element to be removed
     * @return a {@link Response} indicating the result of the operation
     */
    @Override
    public Response execute(CommandArgs request) {
        if(request.args() == null || request.args().isEmpty()) return new Response("enter an id to remove");
        if(request.args().size() > 1) return new Response("enter only id");
        long id;
        try{
            id = Long.parseLong(request.args().get(0));
        }catch (NumberFormatException e){
            return new Response("id must be digit");
        }

        Set<Flat> collection = CollectionManager.getInstance().getCollection();

        Optional<Flat> flatToRemove = collection.stream()
                .filter(element -> element.getId() == id)
                .filter(element -> request.user().getId() == element.getUserOwnerID())
                .findAny();

        if(flatToRemove.isEmpty()){
            return new Response("your collection doesn't include element with id " + id, ResponseType.ERROR);
        }else{
            if(CollectionManager.getInstance().removeElement(flatToRemove.get(), request.user().getId())){
                return new Response("was deleted element with id " +  id, ResponseType.COLLECTION_UPDATE);
            }
            return new Response("element wasn't deleted", ResponseType.ERROR);
        }

    }
}
