package ru.sdmitriy612.clientapp.handling;

import ru.sdmitriy612.clientapp.io.network.ClientConnection;
import ru.sdmitriy612.interactors.Interactor;
import ru.sdmitriy612.interactors.Request;
import ru.sdmitriy612.interactors.Response;
import ru.sdmitriy612.interactors.ResponseType;

import java.io.IOException;

public class ServerCommunication  {
    /**
     * Sends the given request to the server and reads the response.
     * If the response is not an instance of {@link Response}, returns an error response.
     *
     * @param request the request to send to the server
     * @return the response received from the server
     */
    public static Response serverRequest(Request request){
        try(ClientConnection clientConnection = new ClientConnection()){
            clientConnection.write(request);
            Interactor response = clientConnection.read();

            if (!(response instanceof Response)) {
                throw new IOException("Interactor from server isn't instance of Response");
            }

            return (Response) response;
        }catch (Exception e){
            return new Response(e.getMessage(), ResponseType.ERROR);
        }
    }
}
