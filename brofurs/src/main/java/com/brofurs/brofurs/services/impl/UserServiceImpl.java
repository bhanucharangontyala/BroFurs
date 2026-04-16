package com.brofurs.brofurs.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.brofurs.brofurs.dto.UserRegistrationDto;
import com.brofurs.brofurs.entity.Role;
import com.brofurs.brofurs.entity.User;
import com.brofurs.brofurs.repository.RoleRepository;
import com.brofurs.brofurs.repository.UserRepository;
import com.brofurs.brofurs.services.UserService;

/**
 * UserServiceImpl
 *
 * Implements both UserService and Spring Security's UserDetailsService.
 *
 * loadUserByUsername() is called by the DaoAuthenticationProvider on every
 * login attempt. It looks up the user by email and converts their roles to
 * Spring Security GrantedAuthority objects.
 *
 * registerUser() hashes the password using BCrypt, assigns ROLE_USER, and saves
 * the new user account.
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;

	public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
			PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
	}

	// ── Spring Security ───────────────────────────────────────────
	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("No account found with email: " + email));

		List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
				.map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());

		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
				user.isEnabled(), true, true, true, authorities);
	}

	// ── Registration ──────────────────────────────────────────────
	@Override
	public User registerUser(UserRegistrationDto dto) {
		Role userRole = roleRepository.findByName("ROLE_USER")
				.orElseThrow(() -> new RuntimeException("ROLE_USER not found. Ensure DataInitializer has run."));

		User user = new User();
		user.setFullName(dto.getFullName());
		user.setEmail(dto.getEmail().toLowerCase().trim());
		user.setPhone(dto.getPhone());
		user.setAddress(dto.getAddress());
		user.setPassword(passwordEncoder.encode(dto.getPassword()));
		user.setEnabled(true);
		user.addRole(userRole);

		return userRepository.save(user);
	}

	// ── Lookups ───────────────────────────────────────────────────
	@Override
	@Transactional(readOnly = true)
	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<User> findById(Long id) {
		return userRepository.findById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<User> findAll() {
		return userRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public boolean emailExists(String email) {
		return userRepository.existsByEmail(email);
	}
}