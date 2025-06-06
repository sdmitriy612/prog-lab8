package ru.sdmitriy612.command;

import ru.sdmitriy612.interactors.Response;
import ru.sdmitriy612.interactors.ResponseType;


public class History extends Command{

    public History(){
        super("displays history of commands", CommandType.CLIENT);
    }

    @Override
    public Response execute(CommandArgs request) {
        return new Response("", ResponseType.CLIENT);
    }
}
