package ru.sdmitriy612.command;

import ru.sdmitriy612.collection.flat.FlatBuilder;
import ru.sdmitriy612.user.User;

import java.util.List;

public record CommandArgs(List<String> args, User user, FlatBuilder builder) {
}
