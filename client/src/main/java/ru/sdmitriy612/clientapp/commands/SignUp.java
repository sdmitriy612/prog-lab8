package ru.sdmitriy612.clientapp.commands;

import ru.sdmitriy612.clientapp.Session;
import ru.sdmitriy612.clientapp.handling.ServerCommunication;
import ru.sdmitriy612.interactors.Request;
import ru.sdmitriy612.interactors.Response;
import ru.sdmitriy612.interactors.UserAuthorization;
import ru.sdmitriy612.io.console.BufferedConsoleIOController;
import ru.sdmitriy612.io.console.ConsoleIOController;
import ru.sdmitriy612.utils.HashSha512;

public class SignUp extends Command{

    @Override
    public Response execute(Request request) {
       try(ConsoleIOController consoleIOController = new BufferedConsoleIOController()){

           String login = consoleIOController.read("Login: ");
           String password = consoleIOController.read("Password: ");
           if(login.isBlank()) return new Response("Login cannot be empty");
           if(password.isBlank()) return new Response("Password cannot be empty");

           UserAuthorization authorization = new UserAuthorization(login, HashSha512.hash(password));
           Session.getInstance().setUserAuthorization(authorization);
           Request request1 = new Request(request.commandName(), request.args(), authorization);
           return ServerCommunication.serverRequest(request1);

       }catch (Exception e){
           return new Response("Error during authorization");
       }
    }

}
