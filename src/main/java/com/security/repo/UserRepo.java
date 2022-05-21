package com.security.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.security.entities.User;

public interface UserRepo extends JpaRepository<User, Long> {
	User findByUsername(String username);
}
