package ru.sdmitriy612.interactors;

import java.io.Serializable;

public record UserAuthorization(String login, String hashedPassword) implements Serializable {
}
