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

	User registerUser(UserRegistrationDto dto);

	Optional<User> findByEmail(String email);

	Optional<User> findById(Long id);

	List<User> findAll();

	boolean emailExists(String email);
}
