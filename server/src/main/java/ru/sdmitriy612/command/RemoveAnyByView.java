package ru.sdmitriy612.command;

import ru.sdmitriy612.collection.CollectionManager;
import ru.sdmitriy612.collection.flat.Flat;
import ru.sdmitriy612.collection.flat.View;
import ru.sdmitriy612.interactors.Request;
import ru.sdmitriy612.interactors.Response;
import ru.sdmitriy612.interactors.ResponseType;

import java.util.Set;
import java.util.Optional;

/**
 * Command that removes one element from the collection where the view equals the specified value.
 * <p>
 * This command iterates through the collection and removes the first element
 * that matches the provided view.
 * </p>
 */
public class RemoveAnyByView extends Command{
    /**
     * Constructs the command with a description and command type.
     */
    protected RemoveAnyByView() {
        super("removes one element which view is equals to given", CommandType.PRIMITIVE);
    }
    /**
     * Executes the remove by view command.
     * It removes the first element in the collection whose view matches the provided value.
     *
     * @param request the request containing the view to be matched and removed
     * @return a {@link Response} indicating the result of the operation
     */
    @Override
    public Response execute(CommandArgs request) {
        if(request.args() == null || request.args().isEmpty()) return new Response("enter a view to remove");
        if(request.args().size() > 1) return new Response("enter 1 view");

        View view;
        try{
            view = View.valueOf(request.args().get(0));
        }catch (IllegalArgumentException e){
            return new Response("entered view doesn't exist");
        }

        Set<Flat> collection = CollectionManager.getInstance().getCollection();

        Optional<Flat> flatToRemove = collection.stream()
                .filter(element -> element.getView() == view)
                .filter(element -> request.user().getId() == element.getUserOwnerID())
                .findAny();

        if(flatToRemove.isEmpty()){
            return new Response("your collection doesn't include elements with view " + view);
        }else{
            if(CollectionManager.getInstance().removeElement(flatToRemove.get(), request.user().getId())){
                return new Response("was deleted element with view " + view, ResponseType.COLLECTION_UPDATE);
            }
            return new Response("element wasn't deleted");

        }

    }
}
