package com.m3bi.hotelbooking;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.m3bi.dao.HotelRepository;
import com.m3bi.dao.UserRepository;
import com.m3bi.model.HotelBookingRequest;
import com.m3bi.model.User;
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.DEFINED_PORT)
@TestPropertySource(properties = "server.port=8080")
public class HotelRoomBookingTests {
	
	@Autowired
	TestRestTemplate restTemplate;
	
	@Autowired
	UserRepository userDAO;
	
	@LocalServerPort
	public int serverPort;
	
	
	@Test
	public void hotelRoomBookingErrorHotelName() {
		String url = "http://localhost:" + serverPort + "/hotel/booking";
		HotelBookingRequest hotelBookingRequest = new HotelBookingRequest();
		hotelBookingRequest.setHotelName("Undefined");
		hotelBookingRequest.setRoomType("DELUXE");
		hotelBookingRequest.setBookingDate(new Date(System.currentTimeMillis()));
		User user = userDAO.findByName("Kaushal");
		hotelBookingRequest.setUserId(user.getId());

		try {
			restTemplate.postForEntity(url, hotelBookingRequest, String.class);
		} catch (Exception e) {
			assertEquals("Hotel " + hotelBookingRequest.getHotelName() + " not found.", e.getMessage());
		}
	}
	
	@Test
	public void hotelRoomBookingErrorRoomType() {
		String url = "http://localhost:" + serverPort + "/hotel/booking";
		HotelBookingRequest hotelBookingRequest = new HotelBookingRequest();
		hotelBookingRequest.setHotelName("BBQ");
		hotelBookingRequest.setRoomType("Undefined");
		hotelBookingRequest.setBookingDate(new Date(System.currentTimeMillis()));
		User user = userDAO.findByName("Kaushal");
		hotelBookingRequest.setUserId(user.getId());

		try {
			restTemplate.postForEntity(url, hotelBookingRequest, String.class);
		} catch (Exception e) {
			assertEquals("Room type " + hotelBookingRequest.getRoomType() + " not found.", e.getMessage());
		}
	}

	@Test
	public void hotelRoomBookingErrorUser() {
		String url = "http://localhost:" + serverPort + "/hotel/booking";
		HotelBookingRequest hotelBookingRequest = new HotelBookingRequest();
		hotelBookingRequest.setHotelName("BBQ");
		hotelBookingRequest.setRoomType("DELUXE");
		hotelBookingRequest.setBookingDate(new Date(System.currentTimeMillis()));
		hotelBookingRequest.setUserId(999);
		// ResponseEntity<String> response = null;
		try {
			restTemplate.postForEntity(url, hotelBookingRequest, String.class);
		} catch (Exception e) {
			assertEquals("User with id " + hotelBookingRequest.getUserId() + " not found.", e.getMessage());
		}
	}

	@Test
	public void createHotelRoomBookingInBBQHotel() {
		String url = "http://localhost:" + serverPort + "/hotel/booking";
		HotelBookingRequest hotelBookingRequest = new HotelBookingRequest();
		hotelBookingRequest.setHotelName("BBQ");
		hotelBookingRequest.setRoomType("DELUXE");
		hotelBookingRequest.setBookingDate(new Date(System.currentTimeMillis()));
		User user = userDAO.findByName("Kaushal");
		hotelBookingRequest.setUserId(user.getId());

		restTemplate.postForEntity(url, hotelBookingRequest, String.class);
	}

	

}
