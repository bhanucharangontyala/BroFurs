package com.brofurs.brofurs.services.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.brofurs.brofurs.exception.FileStorageException;
import com.brofurs.brofurs.services.FileStorageService;

import jakarta.annotation.PostConstruct;

/**
 * FileStorageServiceImpl
 *
 * Stores uploaded product images to a configurable directory on disk. Assigns a
 * UUID-based filename to prevent collisions and path traversal.
 *
 * Configuration (application.properties): app.upload.dir=uploads → relative
 * path (resolves to project root) app.upload.dir=/var/uploads → absolute path
 * (recommended for production)
 *
 * Validation rules: Allowed types : image/jpeg, image/png, image/webp,
 * image/gif Max file size : 10 MB (also enforced by Spring's multipart config)
 *
 * Stored files are served at /uploads/{filename} via the resource handler
 * registered in WebMvcConfig.
 */
@Service
public class FileStorageServiceImpl implements FileStorageService {

	@Value("${app.upload.dir:uploads}")
	private String uploadDir;

	private static final List<String> ALLOWED_CONTENT_TYPES = List.of("image/jpeg", "image/png", "image/webp",
			"image/gif");

	private static final long MAX_FILE_SIZE_BYTES = 10L * 1024 * 1024; // 10 MB

	private Path rootLocation;

	@PostConstruct
	public void init() {
		rootLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
		try {
			Files.createDirectories(rootLocation);
		} catch (IOException e) {
			throw new FileStorageException("Could not create upload directory at: " + rootLocation, e);
		}
	}

	// ── store ─────────────────────────────────────────────────────
	@Override
	public String store(MultipartFile file) {
		if (file == null || file.isEmpty()) {
			throw new FileStorageException("Cannot store an empty file");
		}

		// Validate MIME type
		String contentType = file.getContentType();
		if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType)) {
			throw new FileStorageException(
					"Invalid file type: " + contentType + ". Allowed types: JPEG, PNG, WebP, GIF");
		}

		// Validate file size
		if (file.getSize() > MAX_FILE_SIZE_BYTES) {
			throw new FileStorageException(
					"File size " + (file.getSize() / 1024 / 1024) + " MB exceeds the 10 MB limit");
		}

		// Extract file extension from original filename
		String originalName = StringUtils
				.cleanPath(file.getOriginalFilename() != null ? file.getOriginalFilename() : "upload");
		String extension = "";
		int dotIndex = originalName.lastIndexOf('.');
		if (dotIndex >= 0) {
			extension = originalName.substring(dotIndex).toLowerCase();
		}

		// Generate a unique filename using UUID
		String uniqueFilename = UUID.randomUUID().toString() + extension;

		try {
			Path destination = rootLocation.resolve(uniqueFilename).normalize();

			// Security: ensure destination is inside the root location
			if (!destination.startsWith(rootLocation)) {
				throw new FileStorageException("Cannot store file outside the upload directory");
			}

			Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
			return uniqueFilename;

		} catch (IOException e) {
			throw new FileStorageException("Failed to store file " + originalName, e);
		}
	}

	// ── delete ────────────────────────────────────────────────────
	@Override
	public void delete(String filename) {
		if (filename == null || filename.isBlank())
			return;
		try {
			Path filePath = rootLocation.resolve(filename).normalize();
			Files.deleteIfExists(filePath);
		} catch (IOException e) {
			// Log but do not throw — a missing file should not break any workflow
			System.err.println("Warning: could not delete file " + filename + ": " + e.getMessage());
		}
	}
}
