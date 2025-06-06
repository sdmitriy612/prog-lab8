package ru.sdmitriy612.command;

import ru.sdmitriy612.interactors.Request;
import ru.sdmitriy612.interactors.Response;

import java.util.ArrayList;
import java.util.HashMap;
/**
 * Command that displays descriptions of available commands.
 * <p>
 * This command provides a list of all available commands along with their descriptions.
 * </p>
 */
public class Help extends Command {
    /**
     * Constructs the command with a description and command type.
     */
    public Help(){
        super("displays descriptions of commands", CommandType.PRIMITIVE);
    }
    /**
     * Executes the help command, displaying a list of available commands and their descriptions.
     *
     * @param request the request associated with the help command (not used in this case)
     * @return a {@link Response} containing the descriptions of all commands
     */
    @Override
    public Response execute(CommandArgs request){
        HashMap<String, Command> commandsMap = CommandsMap.getCommands();
        ArrayList<String> commandsDescriptions = new ArrayList<>();
        commandsMap.forEach((commandName, command) -> {
            if(command.getCommandType() != CommandType.SERVER)
                commandsDescriptions.add(commandName + " " + command.getDescription());
        });

        return new Response(String.join(System.lineSeparator(), commandsDescriptions));
    }
}