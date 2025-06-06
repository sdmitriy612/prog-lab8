package ru.sdmitriy612.command;


import ru.sdmitriy612.interactors.Response;
import ru.sdmitriy612.interactors.ResponseType;
import ru.sdmitriy612.user.LoginAlreadyExistsException;
import ru.sdmitriy612.user.User;


public class SignUp extends Command {

    public SignUp(){
        super("creates new account", CommandType.AUTHORIZATION);
    }

    @Override
    public Response execute(CommandArgs request){
        User user = request.user();
        if(user.getAuthorization() == null) return new Response("Enter login data", ResponseType.AUTHORIZE);
        try{
            user.createUser();
            return new Response("Account has been created", ResponseType.SUCCESS);
        }catch (LoginAlreadyExistsException e){
            return new Response("Login already exists");
        }
    }
}