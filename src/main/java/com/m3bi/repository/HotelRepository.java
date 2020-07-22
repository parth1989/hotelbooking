package com.m3bi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.m3bi.model.Hotel;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Integer> {
	public Hotel findByName(String name);
}
