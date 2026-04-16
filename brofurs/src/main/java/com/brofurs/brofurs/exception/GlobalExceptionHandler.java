package com.brofurs.brofurs.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * GlobalExceptionHandler
 *
 * Centralised exception handling for the entire application
 * using @ControllerAdvice. Maps exceptions to the appropriate Thymeleaf error
 * pages or redirect responses.
 *
 * Handler summary:
 *
 * ResourceNotFoundException → HTTP 404 → error/404.html NoHandlerFoundException
 * → HTTP 404 → error/404.html AccessDeniedException → HTTP 403 → error/403.html
 * FileStorageException → redirect back with flash error message
 * IllegalArgumentException → redirect back with flash error message Exception
 * (catch-all) → HTTP 500 → error/500.html
 */
@ControllerAdvice
public class GlobalExceptionHandler {

	// ── 404 Not Found ─────────────────────────────────────────────
	@ExceptionHandler(ResourceNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String handleResourceNotFound(ResourceNotFoundException ex, Model model) {
		model.addAttribute("errorMessage", ex.getMessage());
		return "error/404";
	}

	@ExceptionHandler(NoHandlerFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String handleNoHandlerFound(NoHandlerFoundException ex, Model model) {
		model.addAttribute("errorMessage", "The page you are looking for does not exist: " + ex.getRequestURL());
		return "error/404";
	}

	// ── 403 Access Denied ──────────────────────────────────────────
	@ExceptionHandler(AccessDeniedException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public String handleAccessDenied(AccessDeniedException ex, Model model) {
		model.addAttribute("errorMessage", "You do not have permission to access this page.");
		return "error/403";
	}

	// ── File Upload Errors → redirect back with flash message ──────
	@ExceptionHandler(FileStorageException.class)
	public String handleFileStorage(FileStorageException ex, HttpServletRequest request, RedirectAttributes attrs) {
		attrs.addFlashAttribute("errorMessage", "File upload failed: " + ex.getMessage());
		String referer = request.getHeader("Referer");
		return "redirect:" + (referer != null ? referer : "/");
	}

	// ── Illegal Argument → redirect back with flash message ────────
	@ExceptionHandler(IllegalArgumentException.class)
	public String handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request,
			RedirectAttributes attrs) {
		attrs.addFlashAttribute("errorMessage", ex.getMessage());
		String referer = request.getHeader("Referer");
		return "redirect:" + (referer != null ? referer : "/");
	}

	// ── 500 Catch-All ──────────────────────────────────────────────
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public String handleGeneral(Exception ex, Model model) {
		model.addAttribute("errorMessage", "An unexpected error occurred. Please try again later.");
		return "error/500";
	}
}