package ru.sdmitriy612.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ru.sdmitriy612.collection.CollectionManager;
import ru.sdmitriy612.collection.flat.Flat;
import ru.sdmitriy612.interactors.Request;
import ru.sdmitriy612.interactors.Response;
import ru.sdmitriy612.interactors.ResponseType;
import ru.sdmitriy612.io.formatters.XMLFormatter;

import java.util.Set;
/**
 * Command that displays all the elements in the collection.
 * <p>
 * This command retrieves all elements from the collection managed by {@link CollectionManager}
 * and formats them into a string representation for display.
 * </p>
 */
public class Show extends Command{
    /**
     * Constructs the command with a description and command type.
     */
    public Show(){
        super("displays all collection elements", CommandType.PRIMITIVE);
    }
    /**
     * Executes the command to display all elements in the collection.
     *
     * @param request the request object containing any arguments (not used in this case)
     * @return a {@link Response} containing the string representation of all elements in the collection
     */
    @Override
    public Response execute(CommandArgs request) {
        Set<Flat> collection = CollectionManager.getInstance().getCollection();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            String json = objectMapper.writeValueAsString(collection);
            return new Response(json, ResponseType.COLLECTION_UPDATE);

        }catch (Exception e){
            System.out.println("Error during serialization JSON: " + e.getMessage());
            return new Response("Error during serialization collection", ResponseType.ERROR);
        }

    }
}
