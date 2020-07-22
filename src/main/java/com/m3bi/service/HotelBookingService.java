package com.m3bi.service;

import com.m3bi.exception.HotelNotFoundException;
import com.m3bi.exception.RoomTypeNotFoundException;
import com.m3bi.exception.UserNotFoundException;
import com.m3bi.model.HotelBookingRequest;

public interface  HotelBookingService {
	
	
	public void bookHotelRoom(HotelBookingRequest hotelBookingRequest) throws HotelNotFoundException, RoomTypeNotFoundException, UserNotFoundException, Exception;

}
