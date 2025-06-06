package ru.sdmitriy612.clientapp;

import ru.sdmitriy612.interactors.UserAuthorization;

import java.util.ArrayDeque;

public class Session {
    private static Session INSTANCE;
    private UserAuthorization userAuthorization;
    private final ArrayDeque<String> commandsHistory = new ArrayDeque<>();

    private Session(){}

    public static Session getInstance(){
        return INSTANCE == null ? INSTANCE = new Session() : INSTANCE;
    }

    public UserAuthorization getUserAuthorization() {
        return userAuthorization;
    }

    public void setUserAuthorization(UserAuthorization userAuthorization) {
        this.userAuthorization = userAuthorization;
    }

    public void updateHistory(String commandName){
        if(commandsHistory.size() == 12) commandsHistory.removeFirst();
        if(!commandName.equals("/history")) commandsHistory.addLast(commandName);
    }

    public ArrayDeque<String> getCommandsHistory(){
        return commandsHistory;
    }
}
