package ru.sdmitriy612.utils;

public class EnvironmentVariableNotFoundException extends RuntimeException {
    public EnvironmentVariableNotFoundException(String message) {
        super(message);
    }
}
