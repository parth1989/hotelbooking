package com.m3bi.controller;

import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.m3bi.model.HotelRoom;
import com.m3bi.repository.HotelRoomRepository;

@RestController
public class HotelRoomController {
	@Autowired
	private HotelRoomRepository hotelRoomRepo;
	
	@PostMapping("/hotelRoom")
	public void saveHotelRoomType(@RequestBody HotelRoom hotelRoom) {
		hotelRoomRepo.save(hotelRoom);
	}
	
	@GetMapping("/hotelRooms")
	public List<HotelRoom> getHotelRooms() {
		return hotelRoomRepo.findAll();
	}
	
	@GetMapping("/hotelRoom/{id}")
	public HotelRoom getHotelRoomById(@PathParam(value = "id") int id) {
		return hotelRoomRepo.findById(id).get();
	}
	
	@GetMapping("/hotelRoom/{hotelId}")
	public List<HotelRoom> getHotelRoomsByHotelId(@PathParam(value = "hotelId") int hotelId) {
		return hotelRoomRepo.findByHotelId(hotelId);
	}
}
