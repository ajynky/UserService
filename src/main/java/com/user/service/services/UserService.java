package com.user.service.services;

import java.util.List;

import com.user.service.entities.User;

public interface UserService {

	User saveUser(User user);

	List<User> getAllUsers();

	User getUser(String userId);

	String deleteUser(String userId);

	User updateUser(String userId);

}
