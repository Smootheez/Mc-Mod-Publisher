package io.github.smootheez.exception;

/**
 * Exception thrown when an upload attempt to a publishing platform
 * (e.g., CurseForge or Modrinth) fails.
 * <p>
 * This may occur due to invalid metadata, network errors, rejected requests,
 * or server-side failures during file processing.
 */
public class FailedFileUploadException extends RuntimeException {

    /**
     * Constructs a new exception indicating that a file upload operation
     * did not complete successfully.
     *
     * @param message a detailed explanation of the failure
     */
    public FailedFileUploadException(String message) {
        super(message);
    }
}

