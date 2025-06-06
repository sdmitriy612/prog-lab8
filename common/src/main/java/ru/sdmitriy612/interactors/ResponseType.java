package ru.sdmitriy612.interactors;
/**
 * Defines the type of server response to guide client behavior.
 */
public enum ResponseType {
    SUCCESS,
    /** Final response; no further action required. */
    FINAL,
    /** Server requests the client to provide an object (data input). */
    ENTER_OBJECT,
    /** Indicates an error occurred during processing. */
    ERROR,
    /** Command should be handled by the client-side logic. */
    CLIENT,
    AUTHORIZE,
    COLLECTION_UPDATE

}
