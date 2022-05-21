package com.security;

import java.util.ArrayList;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.security.entities.Role;
import com.security.entities.User;
import com.security.service.UserService;

@SpringBootApplication
public class DemoSpringSecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoSpringSecurityApplication.class, args);
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	

	@Bean
	CommandLineRunner run(UserService userService) {
		return args -> {
			userService.saveRole(new Role(null,"ROLE_USER"));
			userService.saveRole(new Role(null,"ROLE_MANAGER"));
			userService.saveRole(new Role(null,"ROLE_ADMIN"));
			userService.saveRole(new Role(null,"ROLE_SUPER_ADMIN"));
			
			userService.saveUser(new User(null,"Minh Tran", "minh", "1234", "avater1.jpg", new ArrayList<>()));
			userService.saveUser(new User(null,"Dang Quang", "quang", "1234", "avater2.png", new ArrayList<>()));
			userService.saveUser(new User(null,"Nguyen Linh", "linh", "1234", "avater3.jpg", new ArrayList<>()));
			userService.saveUser(new User(null,"Hoang Hung", "hung", "1234","avater4.png", new ArrayList<>()));
		
			userService.addRoleToUser("minh", "ROLE_USER");
			userService.addRoleToUser("minh", "ROLE_MANAGER");
			userService.addRoleToUser("minh", "ROLE_ADMIN");
			userService.addRoleToUser("quang", "ROLE_SUPER_ADMIN");
			userService.addRoleToUser("linh", "ROLE_ADMIN");
			userService.addRoleToUser("hung", "ROLE_USER");
			userService.addRoleToUser("linh", "ROLE_USER");
			userService.addRoleToUser("linh", "ROLE_MANAGER");
			userService.addRoleToUser("quang", "ROLE_USER");
		};
	}
}
