package com.m3bi.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.m3bi.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	public User findByName(String name);
}
