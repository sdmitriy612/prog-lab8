package ru.sdmitriy612.io.console;

import ru.sdmitriy612.io.StringIOController;

import java.io.*;

/**
 * Implementation of {@link StringIOController} interface
 * <p>
 *     The class works with the I/O stream in the console using buffering with BufferedReader and BufferedWriter
 * </p>
 *
 * @see StringIOController
 * @see BufferedReader
 * @see BufferedWriter
 *
 * @since 1.0
 */
public final class BufferedConsoleIOController implements ConsoleIOController, HasReadyState {
    private final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    private final BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(System.out));

    @Override
    public boolean ready(){
        try{
            return bufferedReader.ready();
        }catch (IOException e){
            return false;
        }
    }

    /**
     * Writes data to console immediately
     * @param data to write to the console
     */
    @Override
    public void write(String data) {
        try{
            bufferedWriter.write(data);
            bufferedWriter.flush();
        }catch (IOException ignored){

        }
    }

    /**
     * Reads line from console and return it
     * @return read line from user or null
     */
    @Override
    public String read(){
        try {
            return bufferedReader.readLine();
        }catch (IOException e){
            return null;
        }
    }

    /**
     * Close read and write streams
     */
    @Override
    public void close() throws Exception {
        bufferedWriter.close();
        bufferedReader.close();
    }
}
