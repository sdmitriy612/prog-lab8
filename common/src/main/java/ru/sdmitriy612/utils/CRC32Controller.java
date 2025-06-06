package ru.sdmitriy612.utils;

import java.util.zip.CRC32;
/**
 * The {@link CRC32Controller} class implements the {@link HashController} interface and provides functionality
 * for generating and verifying CRC32 checksums (hashes).
 * <p>
 * CRC32 is a checksum algorithm used for detecting errors in data. It generates a 32-bit hash value based on the input data.
 * </p>
 */
public class CRC32Controller implements HashController{
    CRC32 crc32 = new CRC32();
    /**
     * Builds a CRC32 hash (checksum) for the given data.
     * This method calculates a CRC32 checksum for the provided string data and returns it as a string.
     *
     * @param data the input data to generate the checksum for
     * @return the CRC32 checksum as a string
     */
    @Override
    public String buildHash(String data) {
        crc32.update(data.getBytes());
        long checkSum = crc32.getValue();
        return Long.toString(checkSum);
    }
    /**
     * Checks whether the given data matches the provided checksum.
     * This method compares the hash of the provided data with the given hash to verify integrity.
     *
     * @param data the input data to check
     * @param hash the checksum to compare with
     * @return true if the hash of the data matches the given hash, false otherwise
     */
    @Override
    public boolean checkHash(String data, String hash) {
        String hashOfData = buildHash(data);
        return hashOfData.equals(hash);
    }
}
