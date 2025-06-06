package ru.sdmitriy612.io;

import java.io.IOException;

/**
 * The `StringIOController` interface extends the `IOController` interface and is responsible for
 * handling input and output operations with strings.
 * <p>
 * It adds methods for writing, and error handling using strings.
 * </p>
 */
public interface StringIOController extends IOController<String> {
    /**
     * Writes the provided data followed by a newline character to the output.
     *
     * @param data the string data to be written
     */
    default void writeln(String data) throws IOException {
        write(data + System.lineSeparator());
    }
    /**
     * Writes the provided error message to the output.
     * This method is used for error reporting and simply calls `writeln` to output the message.
     *
     * @param message the error message to be written
     */
    default void error(String message) {
        try{
            writeln(message);
        }catch (IOException ignored){

        }
    }
}
