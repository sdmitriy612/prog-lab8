package ru.sdmitriy612.io.files;

import ru.sdmitriy612.utils.WrongScriptDataError;
import ru.sdmitriy612.io.SafeIOController;
import ru.sdmitriy612.io.StringIOController;
import ru.sdmitriy612.utils.CRC32Controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

/**
 * The `FileIOController` class implements both {@link StringIOController} and {@link SafeIOController<String>} interfaces.
 * It provides functionality to safely read from and write to a file, including checksum verification to ensure data integrity.
 * <p>
 * This class allows reading data from a file, writing data to a file, and checking the integrity of data using CRC32 hashes.
 * </p>
 */
public class FileIOController implements StringIOController, SafeIOController<String> {
    final private Scanner scanner;
    final private FileOutputStream fileOutputStream;

    /**
     * Creates a new instance of {@code FileIOController} with the specified file path
     * <p>
     *     If file not exists - creates new file with given path.
     * </p>
     * @param filePath the path to the file
     * @param append if true, then data will be written to the end of the file rather than the beginning
     * @throws IOException if an I/O error occurs while opening the file
     */
    public FileIOController(String filePath, boolean append) throws IOException{
        Path path = Path.of(filePath);
        if(!Files.exists(path)){
            File file = new File(filePath);
            if(!file.createNewFile()) throw new IOException("cannot create file " + filePath);
        }
        String fileContent = Files.readString(path);

        this.scanner = new Scanner(fileContent);
        this.fileOutputStream = new FileOutputStream(filePath, append);
    }

    /**
     * Creates a new instance of {@code FileIOController} with the specified file path.
     * <p>
     * Data will be added to the file by default
     * </p>
     * @param filePath the path to the file
     * @throws IOException if an I/O error occurs while opening the file
     */
    public FileIOController(String filePath) throws IOException{
        this(filePath,true);
    }


    /**
     * Writes data to file immediately.
     * @param data to write
     */
    public void write(String data) throws IOException{
        fileOutputStream.write(data.getBytes());
        fileOutputStream.flush();
    }

    /**
     * Safely writes data to the file, including checksum for integrity verification.
     *
     * @param data the data to write
     */
    public void safeWrite(String data) throws IOException{
        CRC32Controller crc32Controller = new CRC32Controller();
        String checkSum = "checksum" + crc32Controller.buildHash(data);
        data = checkSum + System.lineSeparator() + data;
        write(data);
    }
    /**
     * Throws a {@link WrongScriptDataError} exception with the given message.
     *
     * @param message the error message
     */
    @Override
    public void error(String message){
        throw new WrongScriptDataError(message);
    }
    /**
     * Reads a line of input from the file.
     *
     * @param prompt the prompt message (not used in this implementation)
     * @return the read line, or null if the end of the file is reached
     */
    @Override
    public String read(String prompt){
        return read();
    }

    /**
     * Reads 1 line from file.
     * @return current line from the file, or null if an error occurs or the end of file is reached
     */
    public String read(){
        if(!scanner.hasNext()) return null;

        return scanner.nextLine();
    }

    /**
     * Reads all text from file.
     * @return all text from the file, or null if an error occurs or the end of file is reached
     */
    public String readAll(){
        StringBuilder result = new StringBuilder();
        String line;
        while ((line = read()) != null) {
            result.append(line).append(System.lineSeparator());
        }

        return result.isEmpty() ? null : result.toString();
    }
    /**
     * Safely reads data from the file and verifies the checksum.
     *
     * @return the file text data, if the checksum is valid
     * @throws WrongChecksumException if the checksum is invalid
     */
    public String safeRead() throws WrongChecksumException{

        String text = readAll();
        if(text == null || text.isBlank()) return null;

        String[] lines = text.split(System.lineSeparator());
        String firstLine = lines[0];

        if(!firstLine.contains("checksum")) throw new WrongChecksumException();

        StringBuilder stringBuilder = new StringBuilder();
        for(int i=1; i<lines.length; i++) stringBuilder.append(lines[i]);

        CRC32Controller crc32Controller = new CRC32Controller();
        String fileTextData = stringBuilder.toString();

        if(crc32Controller.checkHash(fileTextData, firstLine)) throw new WrongChecksumException();

//        String checkSum = "checksum" + crc32Controller.buildHash(fileTextData);
//        if(!checkSum.equals(firstLine)) throw new WrongChecksumException();

        return fileTextData;

    }
    /**
     * Closes the file input and output streams.
     */
    public void close(){
        scanner.close();
        try {
            fileOutputStream.close();
        }catch (IOException ignored){

        }
    }
}
