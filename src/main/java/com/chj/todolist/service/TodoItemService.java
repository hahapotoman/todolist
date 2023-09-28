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

    public List<TodoItemDTO> getAllTodoItems() {
        return todoItemRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // 필터링된 Todo 아이템을 가져오는 메서드
    public List<TodoItemDTO> getTodoItemsByUser(User user) {
        return todoItemRepository.findByUser(user)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // 완료된 Todo 아이템을 가져오는 메서드
    public List<TodoItemDTO> getCompletedTodoItemsByUser(User user) {
        return todoItemRepository.findByUser(user)
                .stream()
                .filter(TodoItem::isCompleted)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // 완료되지 않은 Todo 아이템을 가져오는 메서드
    public List<TodoItemDTO> getIncompleteTodoItemsByUser(User user) {
        return todoItemRepository.findByUser(user)
                .stream()
                .filter(item -> !item.isCompleted())
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void toggleTodoItemComplete(Long id) {
        TodoItem todoItem = todoItemRepository.findById(id).orElse(null);
        if (todoItem != null) {
            todoItem.setCompleted(!todoItem.isCompleted());
            todoItemRepository.save(todoItem);
        }
    }

    @Transactional
    public void addTodoItem(String description, String category, User user) {
        TodoItem newTodoItem = TodoItem.builder()
                .description(description)
                .category(category)
                .completed(false)
                .user(user)
                .build();
        
        todoItemRepository.save(newTodoItem);
    }

    @Transactional
    public void deleteTodoItem(Long id) {
        todoItemRepository.deleteById(id);
    }

    private TodoItemDTO convertToDTO(TodoItem todoItem) {
        return TodoItemDTO.builder()
                .id(todoItem.getId())
                .description(todoItem.getDescription())
                .category(todoItem.getCategory())
                .completed(todoItem.isCompleted())
                .build();
    }
}