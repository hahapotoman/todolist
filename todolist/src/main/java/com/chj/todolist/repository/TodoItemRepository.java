package com.chj.todolist.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chj.todolist.entity.TodoItem;
import com.chj.todolist.entity.User;

public interface TodoItemRepository extends JpaRepository<TodoItem, Long> {

	// 사용자별 TodoItem을 조회하기 위한 메서드
    List<TodoItem> findByUser(User user);
}