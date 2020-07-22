package com.m3bi.controller;

import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.m3bi.dao.HotelRepository;
import com.m3bi.model.Hotel;

@RestController
public class HotelController {
	
	@Autowired
	private HotelRepository hotelRepo;
	
	@PostMapping("/hotel")
	public void saveHotel(@RequestBody Hotel hotel) {
		hotelRepo.save(hotel);
	}
	
	@GetMapping("/hotels")
	public List<Hotel> getHotels() {
		return hotelRepo.findAll();
	}
	
	@GetMapping("/hotel/{id}")
	public Hotel getHotelById(@PathParam(value = "id") int id) {
		return hotelRepo.findById(id).get();
	}
}
