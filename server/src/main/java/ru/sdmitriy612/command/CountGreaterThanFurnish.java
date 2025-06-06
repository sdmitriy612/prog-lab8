package ru.sdmitriy612.command;

import ru.sdmitriy612.collection.CollectionManager;
import ru.sdmitriy612.collection.flat.Flat;
import ru.sdmitriy612.collection.flat.Furnish;
import ru.sdmitriy612.interactors.Request;
import ru.sdmitriy612.interactors.Response;

import java.util.Set;
/**
 * Command that counts the elements in the collection with a furnish type greater than the given furnish value.
 * <p>
 * This command checks the collection for elements whose furnish type is greater than the specified furnish value.
 * </p>
 */
public class CountGreaterThanFurnish extends Command{
    /**
     * Constructs the command with a description and command type.
     */
    protected CountGreaterThanFurnish() {
        super("counts elements with furnish that greater than the given furnish value", CommandType.PRIMITIVE);
    }
    /**
     * Executes the command to count the number of elements with furnish type greater than the specified furnish value.
     *
     * @param request the request containing furnish value as an argument
     * @return a {@link Response} with the count of elements with furnish type greater than the specified furnish value
     */
    @Override
    public Response execute(CommandArgs request) {
        if(request.args() == null || request.args().isEmpty()) return new Response("enter a furnish to count");
        if(request.args().size() > 1) return new Response("enter 1 furnish");

        Furnish furnish;
        try{
            furnish = Furnish.valueOf(request.args().get(0));
        }catch (IllegalArgumentException e){
            return new Response("entered furnish doesn't exist");
        }

        Set<Flat> collection = CollectionManager.getInstance().getCollection();
        long count = collection.stream()
                .filter(element -> element.getFurnish() != null)
                .filter(element -> element.getFurnish().compareTo(furnish) > 0)
                .count();

        return new Response("collection has " + count + " elements with furnish greater than " + furnish);
    }
}
