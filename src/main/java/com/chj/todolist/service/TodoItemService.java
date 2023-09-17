package com.chj.todolist.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.chj.todolist.dto.TodoItemDTO;
import com.chj.todolist.entity.TodoItem;
import com.chj.todolist.entity.User;
import com.chj.todolist.repository.TodoItemRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TodoItemService {

	private final TodoItemRepository todoItemRepository;
	
	// 모든 Todo 아이템을 가져와서 DTO 형태로 변환하여 반환하는 메서드
	public List<TodoItemDTO> getAllTodoItems() {
		return todoItemRepository.findAll()
				.stream()
				.map(this::convertToDTO) // Entity를 DTO로 변환하는 메서드 호출
				.collect(Collectors.toList());
	}
	
	// Todo 아이템의 완료 여부를 토글하는 메서드
	@Transactional
	public void toggleTodoItemComplete(Long id) {
		TodoItem todoItem = todoItemRepository.findById(id).orElse(null);
		if(todoItem != null) {
			todoItem.setCompleted(!todoItem.isCompleted()); // 완료 여부 반전
			todoItemRepository.save(todoItem); // 변경사항 저장
		}
	}
	
	// Todo 아이템을 추가하는 메서드입니다.
	// 사용자로부터 입력받은 description을 받아와 새로운 Todo 아이템을 생성하고 데이터베이스에 저장합니다.
	// @param description 새로운 Todo 아이템의 설명
	@Transactional
    public void addTodoItem(String description, User user) {
		// 새로운 TodoItem 객체 생성
		TodoItem newTodoItem = TodoItem.builder()
                .description(description)
                .completed(false)
                .user(user)
                .build();
        
        // 생성한 TodoItem을 데이터베이스에 저장
        todoItemRepository.save(newTodoItem);
    }
	
	@Transactional
	public void deleteTodoItem(Long id) {
		todoItemRepository.deleteById(id);
	}
	
	public List<TodoItemDTO> getTodoItemsByUser(User user) {
        return todoItemRepository.findByUser(user)
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
	
	// Entity 객체를 DTO로 변환하는 메서드
	private TodoItemDTO convertToDTO(TodoItem todoItem) {
		return TodoItemDTO.builder()
				.id(todoItem.getId())
				.description(todoItem.getDescription())
				.completed(todoItem.isCompleted())
				.build();
	}
	
}