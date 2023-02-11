package com.user.service.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingDTO {

	private String id;
	private String userId;
	private String hotelId;
	private int ratings;
	private String feedback;
	private HotelDTO hotel;

}
