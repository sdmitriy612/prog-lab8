package ru.sdmitriy612.command;

import java.util.HashMap;
/**
 * Class that stores and manages a map of available commands.
 * <p>
 * The map associates command strings with their corresponding {@link Command} objects.
 * </p>
 */
public class CommandsMap {
    private final static HashMap<String, Command> commands = new HashMap<>();

    static {
        commands.put("/help", new Help());
        commands.put("/add", new Add());
        commands.put("/add_if_max", new AddIfMax());
        commands.put("/clear", new Clear());
        commands.put("/count_by_furnish", new CountByFurnish());
        commands.put("/count_greater_than_furnish", new CountGreaterThanFurnish());
        commands.put("/history", new History());
        commands.put("/update", new Update());
        commands.put("/show", new Show());
        commands.put("/remove_lower", new RemoveLower());
        commands.put("/remove_by_id", new RemoveByID());
        commands.put("/remove_any_by_view", new RemoveAnyByView());
        commands.put("/info", new Info());
        commands.put("/execute_script", new ExecuteScript());
        commands.put("/save", new Save());
        commands.put("/exit", new Exit());
        commands.put("/signup", new SignUp());
        commands.put("/signin", new SignIn());

    }
    /**
     * Returns the map of available commands.
     *
     * @return a {@link HashMap} where keys are command strings and values are the corresponding {@link Command} objects
     */
    public static HashMap<String, Command> getCommands(){
        return commands;
    }
}