package com.brofurs.brofurs.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.brofurs.brofurs.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

	Optional<Role> findByName(String name);
}
