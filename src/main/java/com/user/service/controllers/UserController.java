package com.user.service.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.user.service.entities.User;
import com.user.service.services.UserService;

import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping
	public ResponseEntity<User> createUser(@RequestBody User user) {

		User savedUser = userService.saveUser(user);
		return new ResponseEntity<User>(savedUser, HttpStatus.CREATED);
	}

	int retryCount = 1;

	@GetMapping("/{userId}")
	// @CircuitBreaker(name = "ratingHotelBreaker", fallbackMethod =
	// "ratingHotelFallback")
	@Retry(name = "ratingHotelService", fallbackMethod = "ratingHotelFallback")
	public ResponseEntity<User> getSingleUser(@PathVariable String userId) {
		log.info("Retry Count: {}", retryCount);
		retryCount++;
		User user = userService.getUser(userId);
		return ResponseEntity.status(HttpStatus.FOUND).body(user);
	}

	// creating fallback method for circuit breaker
	public ResponseEntity<User> ratingHotelFallback(String userId, Exception ex) {
		// @formatter:off
		// log.info("Fallback method is called because service is down",ex.getMessage());
		User user = User.builder()
					.email("dummy@email.com")
					.name("Dummy")
					.about("this user is return because the service is down")
					.userId("123")
					.build();
		// @formatter:off
		return new ResponseEntity<>(user,HttpStatus.OK);

	}

	@GetMapping
	public ResponseEntity<List<User>> getAllUser() {
		List<User> allUsers = userService.getAllUsers();
		return ResponseEntity.ok(allUsers);
	}

}
