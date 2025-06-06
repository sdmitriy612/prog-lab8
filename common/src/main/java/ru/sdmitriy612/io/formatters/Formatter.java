package ru.sdmitriy612.io.formatters;

import ru.sdmitriy612.collection.CollectionManager;
import ru.sdmitriy612.io.IOController;
import ru.sdmitriy612.io.SafeIOController;
/**
 * Interface representing a serializer that can read and write objects of type T.
 * <p>
 * This interface extends both {@link IOController} and {@link SafeIOController}, providing methods to read, write,
 * and safely handle objects of type T.
 * </p>
 *
 * @param <T> the type of object that this serializer works with
 */
public interface Formatter<T> extends IOController<T>, SafeIOController<CollectionManager>  {
}
