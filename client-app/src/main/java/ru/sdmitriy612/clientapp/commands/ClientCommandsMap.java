package ru.sdmitriy612.clientapp.commands;

import java.util.HashMap;

/**
 * Class that stores and manages a map of available commands on client side.
 * <p>
 * The map associates command strings with their corresponding {@link Command} objects.
 * </p>
 */
public class ClientCommandsMap {
    private final static HashMap<String, Command> commands = new HashMap<>();

    static {
        commands.put("/execute_script", new ExecuteScript());
        commands.put("/exit", new Exit());
        commands.put("/signin", new SignIn());
        commands.put("/signup", new SignUp());
        commands.put("/history", new History());

    }
    /**
     * Returns the map of available client commands.
     *
     * @return a {@link HashMap} where keys are command strings and values are the corresponding {@link Command} objects
     */
    public static HashMap<String, Command> getCommands(){
        return commands;
    }
}