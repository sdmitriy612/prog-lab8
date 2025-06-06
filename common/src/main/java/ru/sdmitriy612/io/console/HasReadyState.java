package ru.sdmitriy612.io.console;
/**
 * Interface for components that can check if they are ready for the next operation.
 *
 */
public interface HasReadyState {
    /**
     * Checks if the component is ready for the next operation.
     *
     * @return {@code true} if ready, {@code false} otherwise.
     */
    boolean ready();
}
