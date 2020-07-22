package com.m3bi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.m3bi.model.HotelRoom;

@Repository
public interface HotelRoomRepository extends JpaRepository<HotelRoom, Integer> {
	public List<HotelRoom> findByHotelId(int hotelId);
//	public List<HotelRoom> findByHotelIdAndBookingStatus(int hotelId, String bookingStatus);
	public List<HotelRoom> findByHotelIdAndRoomTypeId(int hotelId, int roomTypeId);
}
