package ru.sdmitriy612.command;


import ru.sdmitriy612.interactors.Response;
import ru.sdmitriy612.interactors.ResponseType;
import ru.sdmitriy612.user.User;
import ru.sdmitriy612.user.UserNotFoundException;



public class SignIn extends Command {

    public SignIn(){
        super("log in your account", CommandType.AUTHORIZATION);
    }

    @Override
    public Response execute(CommandArgs request){
        User user = request.user();
        try{
            user.authorize();
            return new Response("Success", ResponseType.SUCCESS);
        }catch (UserNotFoundException e){
            return new Response("Login or password is incorrect", ResponseType.AUTHORIZE);
        }
    }
}