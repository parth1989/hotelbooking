package com.m3bi.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.m3bi.model.RoomType;

@Repository
public interface HotelRoomTypeRepository extends JpaRepository<RoomType, Integer> {
	public RoomType findByRoomType(String roomType);
}
