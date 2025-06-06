package ru.sdmitriy612.io.files;
/**
 * Exception thrown when a file's checksum is incorrect or missing.
 * This exception is used to signal that the data read from a file is not valid due to a checksum mismatch.
 */
public class WrongChecksumException extends Exception{

    /**
     * Returns a message indicating that the file's checksum is wrong or missing.
     *
     * @return the error message
     */
    @Override
    public String getMessage(){
        return "File checksum is wrong or missing";
    }
}
