package com.user.service.services.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.user.service.entities.User;
import com.user.service.entities.dto.RatingDTO;
import com.user.service.exceptions.ResourceNotFoundException;
import com.user.service.external.service.HotelService;
import com.user.service.repositories.UserRepository;
import com.user.service.services.UserService;
import com.user.service.utility.IdGenerator;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	HotelService hotelService;

	private List<RatingDTO> ratingOfUserList;

	@Override
	public User saveUser(User user) {
		user.setUserId(IdGenerator.generateUniqueId());
		return userRepository.save(user);
	}

	@Override
	public List<User> getAllUsers() {
		// @formatter:off
		return userRepository
				.findAll()
				.stream()
				.map(user -> getUser(user.getUserId())).toList();
		// @formatter:on
	}

	@SuppressWarnings("unchecked")
	@Override
	public User getUser(String userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User with given ID not found on server"));

		ResponseEntity<RatingDTO[]> responseEntity = restTemplate
				.getForEntity("http://RATINGSERVICE/ratings/users/" + userId, RatingDTO[].class);
		ratingOfUserList = Arrays.asList(responseEntity.getBody());

		if (null != ratingOfUserList) {
			ratingOfUserList.forEach(ratingDTO -> {
				/*
				 * ResponseEntity<HotelDTO> forEntity = restTemplate
				 * .getForEntity("http://HOTELSERVICE/hotels/" + ratingDTO.getHotelId(),
				 * HotelDTO.class);
				 */
				ratingDTO.setHotel(hotelService.getHotel(ratingDTO.getHotelId()));
			});
			user.setRatings(ratingOfUserList);
		}

		return user;
	}

	@Override
	public String deleteUser(String userId) {
		return null;
	}

	@Override
	public User updateUser(String userId) {
		return null;
	}

}
