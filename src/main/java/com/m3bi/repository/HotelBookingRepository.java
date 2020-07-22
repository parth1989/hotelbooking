package com.m3bi.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.m3bi.model.HotelRoomBooking;

@Repository
public interface HotelBookingRepository extends JpaRepository<HotelRoomBooking, Integer> {
	public HotelRoomBooking findByHotelIdAndBookingDateAndUserIdAndHotelRoomType(int hotelId, Date bookingDate, int userId, String hotelRoomType);
	public List<HotelRoomBooking> findByHotelIdAndBookingDateAndHotelRoomType(int hotelId, Date bookingDate, String hotelRoomType);
	public List<HotelRoomBooking> findByHotelId(int hotelId);
	public List<HotelRoomBooking> findByUserIdAndBookingStatus(int userId, String bookingStatus);
}
