package ru.sdmitriy612.interactors;

/**
 * Represents a response from the server to the client.
 * <p>
 * Contains a response message and a {@link ResponseType} indicating the type of response.
 *
 * @param message       the response message
 * @param responseType  the type of the response (e.g., FINAL, ERROR, etc.)
 */
public record Response(String message, ResponseType responseType) implements Interactor {
    /**
     * Constructs a response with type {@link ResponseType#FINAL}.
     *
     * @param message the response message
     */
    public Response(String message){
        this(message, ResponseType.FINAL);
    }
}
