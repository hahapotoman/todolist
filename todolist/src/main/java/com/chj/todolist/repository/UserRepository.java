package com.chj.todolist.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chj.todolist.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	User findByUsername(String username);
	
}