package ru.sdmitriy612.utils;
/**
 * The `WrongScriptDataError` class is a custom error that is thrown when there is an issue with the data in a script.
 */
public class WrongScriptDataError extends Error {
    /**
     * Constructs a new `WrongScriptDataError` with the specified detail message.
     * This message provides information about the nature of the error that occurred while processing script data.
     *
     * @param message the detail message that describes the error
     */
    public WrongScriptDataError(String message){
        super(message);
    }
}
