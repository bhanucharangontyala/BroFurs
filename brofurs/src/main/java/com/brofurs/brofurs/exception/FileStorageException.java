package com.brofurs.brofurs.exception;

/**
 * FileStorageException
 *
 * Thrown by FileStorageServiceImpl when a file upload operation fails. Common
 * causes: - Unsupported file type (not JPEG / PNG / WebP / GIF) - File size
 * exceeds the 10 MB limit - Disk I/O error writing to the uploads directory -
 * Uploads directory does not exist or is not writable
 *
 * Caught by GlobalExceptionHandler which redirects back to the previous page
 * with an errorMessage flash attribute rather than showing a 500 page.
 */
public class FileStorageException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public FileStorageException(String message) {
		super(message);
	}

	public FileStorageException(String message, Throwable cause) {
		super(message, cause);
	}
}