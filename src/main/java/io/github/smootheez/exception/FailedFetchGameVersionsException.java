package io.github.smootheez.exception;

/**
 * Exception thrown when the publisher fails to retrieve the list of
 * valid game versions from the CurseForge API.
 * <p>
 * This typically occurs due to network issues, invalid API credentials,
 * or unexpected responses from the remote service.
 */
public class FailedFetchGameVersionsException extends RuntimeException {

    /**
     * Creates a new exception indicating that fetching game versions has failed.
     *
     * @param message detailed error description returned by the caller
     */
    public FailedFetchGameVersionsException(String message) {
        super(message);
    }
}

