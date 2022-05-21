package com.security.service;

import java.util.List;

import com.security.entities.Role;
import com.security.entities.User;

public interface UserService {
	User saveUser(User user);
	Role saveRole(Role role);
	void addRoleToUser(String username, String roleName);
	User getUser(String username);
	List<User> getUsers();
}
