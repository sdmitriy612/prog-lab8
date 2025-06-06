package ru.sdmitriy612.server.handling;

import ru.sdmitriy612.interactors.Request;
import ru.sdmitriy612.interactors.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles incoming request strings, processes them, and returns appropriate responses.
 *
 */
public class Handler {
    /**
     * Processes a request string, converts it into a {@link Request} object, and returns the server's response message.
     *
     * @param requestString the incoming request string to process
     * @return the response message from the server
     */
    public static String handle(String requestString){

        if (requestString.isBlank()) return "enter correct request";

        ArrayList<String> requestData = new ArrayList<>(List.of(requestString.split(" ")));
        String commandName = requestData.remove(0);

        Request request = new Request(commandName, requestData);
        Response response = Processor.process(request, true);

        return response.message();
    }

}
