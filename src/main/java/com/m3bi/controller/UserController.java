package com.m3bi.controller;

import static com.m3bi.enums.BookingStatusEnum.BOOKED;
import static com.m3bi.enums.BookingStatusEnum.PENDING_APPROVAL;

import java.util.ArrayList;
import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.m3bi.dao.HotelBookingRepository;
import com.m3bi.dao.HotelRoomTypeRepository;
import com.m3bi.dao.UserRepository;
import com.m3bi.model.HotelRoomBooking;
import com.m3bi.model.RoomType;
import com.m3bi.model.User;

@RestController
public class UserController {
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private HotelBookingRepository hotelBookingRepo;
	
	@Autowired
	private HotelRoomTypeRepository hotelRoomTypeDAO;
	
	@PostMapping("/user")
	public void saveUser(@RequestBody User user) {
		userRepo.save(user);
	}
	
	@PutMapping("/user")
	public void updateUser(@RequestBody User user) {
		User existingUser = userRepo.findByName(user.getName());
		if(existingUser != null && existingUser.getBonusPoints() != user.getBonusPoints()) {
			existingUser.setBonusPoints(existingUser.getBonusPoints() + user.getBonusPoints());
			
			List<HotelRoomBooking> userPendingApprovalHotelRoomBookings = hotelBookingRepo.findByUserIdAndBookingStatus(existingUser.getId(), PENDING_APPROVAL.name());
			List<HotelRoomBooking> bookingsToBeSaved = new ArrayList<>();
			for(HotelRoomBooking booking:userPendingApprovalHotelRoomBookings) {
				RoomType roomType = hotelRoomTypeDAO.findByRoomType(booking.getHotelRoomType());
				int roomCost = roomType.getCost().intValue();
				if(roomType.getCost().intValue() <= existingUser.getBonusPoints()) {
					booking.setBookingStatus(BOOKED.name());
					bookingsToBeSaved.add(booking);
					existingUser.setBonusPoints(existingUser.getBonusPoints() - roomCost);
				} else {
					break;
				}
			}
			if(bookingsToBeSaved.size() > 0) {
				hotelBookingRepo.saveAll(bookingsToBeSaved);
			}
			userRepo.save(existingUser);
		}
	}
	
	@GetMapping("/users")
	public List<User> getUsers() {
		return userRepo.findAll();
	}
	
	@GetMapping("/user/{id}")
	public User getUserById(@PathParam(value = "id") int id) {
		return userRepo.findById(id).get();
	}
}
