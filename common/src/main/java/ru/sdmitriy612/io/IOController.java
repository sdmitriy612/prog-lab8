package ru.sdmitriy612.io;

import java.io.IOException;

/**
 * Interface for handling input and output data
 * @param <T> Type of data
 */
public interface IOController<T> extends AutoCloseable {
    /**
     * Reads inputted data.
     * @return the read data or {@code null}
     */
    T read() throws IOException;

    /**
     * Write prompt for input and reads data.
     * @param prompt prompt that will be written before reading
     * @return the read data or {@code null}
     */
    default T read(T prompt) throws IOException{
        write(prompt);
        return read();
    }

    /**
     * Writes data
     * @param data to write
     */
    void write(T data) throws IOException;

}
