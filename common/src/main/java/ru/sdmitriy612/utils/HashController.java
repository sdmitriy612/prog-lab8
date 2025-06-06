package ru.sdmitriy612.utils;
/**
 * The {@link HashController} interface defines methods for building and checking hashes for data.
 * <p>
 * This interface provides methods for generating a hash of a given data string and verifying whether
 * a provided hash matches the hash of the data.
 * </p>
 */
public interface HashController {
    /**
     * Builds a hash for the given data.
     * <p>
     * This method generates a hash based on the provided data and returns it as a string.
     * </p>
     *
     * @param data the input data to generate a hash for
     * @return the generated hash as a string
     */
    String buildHash(String data);
    /**
     * Checks whether the given data matches the provided hash.
     * <p>
     * This method compares the hash of the given data with the provided hash to verify its integrity.
     * </p>
     *
     * @param data the input data to check
     * @param hash the checksum to compare with
     * @return true if the hash of the data matches the provided hash, false otherwise
     */
    boolean checkHash(String data, String hash);
}
