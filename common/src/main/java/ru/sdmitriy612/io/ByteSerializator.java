package ru.sdmitriy612.io;

import java.io.*;

/**
 * Utility class for serializing and deserializing objects to and from byte arrays.
 *
 * @param <T> the type of object this class handles
 */
public class ByteSerializator<T> {

    /** The class of the object to deserialize. */
    private final Class<T> classT;
    /**
     * Constructs a new ByteSerializator for a given class.
     *
     * @param classT the class type to use for deserialization checks
     */
    public ByteSerializator(Class<T> classT) {
        this.classT = classT;
    }

    /**
     * Deserializes a byte array into an object of type T.
     *
     * @param byteArray the byte array to deserialize
     * @param limit     the number of bytes to read from the array
     * @return the deserialized object
     * @throws IOException if deserialization fails or the object is of the wrong type
     */
    public T deserialize(byte[] byteArray, int limit) throws IOException {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray, 0, limit);
             ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {

            Object object = objectInputStream.readObject();

            return classT.cast(object);

        }catch (ClassCastException e){
            throw new IOException("Object isn't instance of " + classT.getName());
        }catch (ClassNotFoundException e) {
            throw new IOException("Deserializing error: class not found");
        }
    }

    /**
     * Serializes an object of type T into a byte array.
     *
     * @param obj the object to serialize
     * @return the byte array representing the serialized object
     * @throws IOException if serialization fails
     */
    public byte[] serialise(T obj) throws IOException {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {

            objectOutputStream.writeObject(obj);
            objectOutputStream.flush();
            return byteArrayOutputStream.toByteArray();

        } catch (IOException e) {
            throw new IOException("Serializing Interactor error: " + e.getMessage(), e);
        }
    }
}
