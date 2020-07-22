package com.m3bi.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.m3bi.exception.HotelNotFoundException;
import com.m3bi.exception.RoomTypeNotFoundException;
import com.m3bi.exception.UserNotFoundException;
import com.m3bi.model.Hotel;
import com.m3bi.model.HotelBookingRequest;
import com.m3bi.model.HotelRoomBooking;
import com.m3bi.repository.HotelBookingRepository;
import com.m3bi.repository.HotelRepository;
import com.m3bi.repository.UserRepository;
import com.m3bi.service.HotelBookingService;

@RestController
public class HotelBookingController {

	@Autowired
	private HotelBookingRepository hotelBookingRepo;

	@Autowired
	private HotelRepository hotelRepo;

	@Autowired
	private HotelBookingService hotelBookingService;

	@Autowired
	private UserRepository userRepo;

	@GetMapping("/hotel")
	public String base() {
		return "Welcome to our hotel";
	}

	@GetMapping("/hotel/{hotelId}/bookings")
	public List<HotelRoomBooking> getHotelRoomBookingsByHotelId(@RequestParam int hotelId) {
		List<HotelRoomBooking> hotelRoomBookings = new ArrayList<>();
		hotelBookingRepo.findByHotelId(hotelId).forEach(hotelRoomBookings::add);
		return hotelRoomBookings;
	}

	@GetMapping("/hotel/bookings")
	public List<HotelRoomBooking> getHotelRoomBookings() {
		List<HotelRoomBooking> hotelRoomBookings = new ArrayList<>();
		hotelBookingRepo.findAll().forEach(hotelRoomBookings::add);
		return hotelRoomBookings;
	}

	
	@GetMapping("/hotel/hotel")
	public List<Hotel> getHotelRoom() {
		List<Hotel> hotelRoomBookings = new ArrayList<>();
		hotelRepo.findAll().forEach(hotelRoomBookings::add);
		return hotelRoomBookings;
	}

	@PostMapping("/hotel/booking")
	public void bookHotelRoom(@Valid @RequestBody HotelBookingRequest hotelBookingRequest)
			throws HotelNotFoundException, RoomTypeNotFoundException, UserNotFoundException, Exception {
		hotelBookingService.bookHotelRoom(hotelBookingRequest);
	}
}
