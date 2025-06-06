package ru.sdmitriy612.interactors;

import java.io.Serializable;

/**
 * Marker interface for objects exchanged between client and server.
 * Extends {@link Serializable} to ensure all implementing classes can be serialized.
 */
public interface Interactor extends Serializable {
}
