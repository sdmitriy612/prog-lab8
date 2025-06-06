package ru.sdmitriy612;

import ru.sdmitriy612.clientapp.io.console.BufferedConsoleIOController;
import ru.sdmitriy612.clientapp.handling.Handler;

/**
 * The {@link Main} class is the entry point of the Flat collector application.
 * <p>
 * This class initializes the application, displays a welcome message, and continuously reads input commands
 * from the console to process them using the {@link Handler}. The results are then displayed back to the user.
 * </p>
 */
public class Main {
    /**
     *  Private constructor to prevent instantiation.
     */
    private Main(){}
    /**
     * The main method, which starts the Flat collector application.
     * <p>
     * It creates an instance of {@link BufferedConsoleIOController} to handle input and output
     * via the console. The application continuously prompts for commands from the user,
     * processes them with the {@link Handler}, and outputs the result back to the console.
     * </p>
     *
     * @param args command-line arguments (not used in this application)
     */
    public static void main(String[] args) {

        System.out.println();
        try(BufferedConsoleIOController consoleIOController = new BufferedConsoleIOController()){
            consoleIOController.writeln("Welcome to Flat collector!");
            String line;
            Handler handler = new Handler(consoleIOController);
            while ((line = consoleIOController.read()) != null)
                consoleIOController.writeln(handler.handle(line));

        }catch (Exception e){
            System.err.println("Error: " + e.getMessage());
        }
    }
}