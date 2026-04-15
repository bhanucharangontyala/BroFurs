package com.brofurs.brofurs.services;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.brofurs.brofurs.dto.UserRegistrationDto;
import com.brofurs.brofurs.entity.User;

/**
 * UserService
 *
 * Extends Spring Security's UserDetailsService so it can be used directly as
 * the authentication provider in SecurityConfig.
 *
 * loadUserByUsername(email) is called by Spring Security on every login
 * attempt. All other methods support registration, lookup, and admin user
 * listing.
 */
public interface UserService extends UserDetailsService {

	/**
	 * Register a new user with ROLE_USER. Encodes the password and saves to the
	 * database.
	 */
	User registerUser(UserRegistrationDto dto);

	/**
	 * Find a user by email address. Used by controllers to resolve the currently
	 * logged-in user from the security principal (UserDetails.getUsername() ==
	 * email).
	 */
	Optional<User> findByEmail(String email);

	/**
	 * Find a user by their database ID.
	 */
	Optional<User> findById(Long id);

	/**
	 * Return all registered users — used by AdminUserController.
	 */
	List<User> findAll();

	/**
	 * Check whether an email address is already registered. Used during
	 * registration to prevent duplicate accounts.
	 */
	boolean emailExists(String email);
}
