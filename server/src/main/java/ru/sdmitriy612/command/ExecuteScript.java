package ru.sdmitriy612.command;

import ru.sdmitriy612.interactors.Request;
import ru.sdmitriy612.interactors.Response;
import ru.sdmitriy612.interactors.ResponseType;

/**
 * Command that reads and executes a script from a given file.
 * <p>
 * This command processes the file provided in the argument and executes each line as a command.
 * It also ensures that script execution is not recursive.
 * </p>
 */
public class ExecuteScript extends Command{
    /**
     * Constructs the command with a description and command type.
     */
    protected ExecuteScript() {
        super("reads and executes a script from a given file", CommandType.CLIENT);
    }
    /**
     * Executes the script from the file provided in the request argument.
     * The script is executed line by line, with each line processed as a command.
     * <p>
     * If the script tries to execute another script, an error message is returned.
     * </p>
     *
     * @param request the request containing the file name to execute the script from
     * @return a {@link Response} indicating the result of the script execution
     */
    @Override
    public Response execute(CommandArgs request) {
        return new Response("Command could be executed only on client side", ResponseType.CLIENT);
    }

}
