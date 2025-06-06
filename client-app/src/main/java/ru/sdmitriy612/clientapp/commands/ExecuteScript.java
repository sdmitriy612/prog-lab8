package ru.sdmitriy612.clientapp.commands;

import ru.sdmitriy612.clientapp.handling.Handler;
import ru.sdmitriy612.interactors.Request;
import ru.sdmitriy612.interactors.Response;
import ru.sdmitriy612.interactors.ResponseType;
import ru.sdmitriy612.io.files.FileIOController;
import ru.sdmitriy612.utils.WrongScriptDataError;

import java.io.IOException;

/**
 * Command that reads and executes a script from a given file.
 * <p>
 * This command processes the file provided in the argument and executes each line as a command.
 * It also ensures that script execution is not recursive.
 * </p>
 */
public class ExecuteScript extends Command{
    private static int scriptRecursionCount = 0;
    private static final int scriptRecursionLimit = 5;
    /**
     * Executes the script from the file provided in the request argument.
     * The script is executed line by line, with each line processed as a command.
     * <p>
     * If the script tries to execute another script more than {@code scriptRecursionLimit} times, an error message is returned.
     * </p>
     *
     * @param request the request containing the file name to execute the script from
     * @return a {@link Response} indicating the result of the script execution
     */
    @Override
    public Response execute(Request request) {
        if(request.args() == null || request.args().size() != 1) return new Response("enter a file name");
        StringBuilder answers = new StringBuilder();

        try(FileIOController fileIOController = new FileIOController(request.args().get(0))){
            scriptRecursionCount++;

            String line;
            Handler handler = new Handler(fileIOController);
            while ((line = fileIOController.read()) != null){
                if(scriptRecursionCount > scriptRecursionLimit) return new Response("recursion limit exceed");
                answers.append(handler.handle(line).message()).append(System.lineSeparator());
            }

        }catch (IOException | WrongScriptDataError e){
            return new Response("Executing script error: " + e.getMessage());
        }

        scriptRecursionCount--;
        return new Response(answers.append("script has ended").toString(), ResponseType.COLLECTION_UPDATE);

    }

}
