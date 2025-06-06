package ru.sdmitriy612.command;

/**
 * CommandType enumeration
 */
public enum CommandType {
    /**
     * PRIMITIVE type for commands without entering an element.
     */
    PRIMITIVE,
    /**
     * FLAT_ENTER type for commands with entering a flat element.
     */
    FLAT_ENTER,
    /**
     * CLIENT type for commands that executes on client (full command or part of it)
     */
    CLIENT,
    /**
     * SERVER type for commands that executes only on server
     */
    SERVER,
    /**
     * CLIENT_OR_SERVER type for commands that executes on server and on client with different logic
     */
    CLIENT_OR_SERVER,
    AUTHORIZATION

}
