package com.m3bi.controller;

import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.m3bi.model.RoomType;
import com.m3bi.repository.HotelRoomTypeRepository;

@RestController
public class HotelRoomTypeController {
	@Autowired
	private HotelRoomTypeRepository hotelRoomTypeRepo;
	
	@PostMapping("/hotelRoomType")
	public void saveHotelRoomType(@RequestBody RoomType hotelRoomType) {
		hotelRoomTypeRepo.save(hotelRoomType);
	}
	
	@GetMapping("/hotelRoomTypes")
	public List<RoomType> getHotelRoomTypes() {
		return hotelRoomTypeRepo.findAll();
	}
	
	@GetMapping("/hotelRoomType/{id}")
	public RoomType getHotelRoomTypeById(@PathParam(value = "id") int id) {
		return hotelRoomTypeRepo.findById(id).get();
	}
}
