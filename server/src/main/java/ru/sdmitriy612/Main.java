package ru.sdmitriy612;

import ru.sdmitriy612.io.console.BufferedConsoleIOController;
import ru.sdmitriy612.server.UDPServer;
import ru.sdmitriy612.server.handling.Handler;

/**
 * Main entry point for the server application.
 * Initializes and starts the server on port 8081.
 */
public class Main {

    private Main(){}
    /**
     * Main method that starts the server.
     * Initializes the Server class and runs it.
     *
     * @param args Command-line arguments (not used)
     */
    public static void main(String[] args) {
        System.out.println("Server started");
        UDPServer server = new UDPServer(8081);
        server.start();

        try(BufferedConsoleIOController consoleIOController = new BufferedConsoleIOController()){
            String line;
            while ((line = consoleIOController.read()) != null)
                consoleIOController.writeln(Handler.handle(line));

        }catch (Exception e){
            System.err.println("Error: " + e.getMessage());
        }
    }
}