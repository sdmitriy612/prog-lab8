package ru.sdmitriy612.clientapp.commands;

import java.util.List;

public record CommandArgs(List<String> args, List<Object> extra) {
}
