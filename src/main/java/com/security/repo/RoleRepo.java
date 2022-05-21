package com.security.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.security.entities.Role;

public interface RoleRepo extends JpaRepository<Role, Long> {
	Role findByName(String name);
}
