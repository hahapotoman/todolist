package com.chj.todolist.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.chj.todolist.dto.TodoItemDTO;
import com.chj.todolist.entity.User;
import com.chj.todolist.repository.UserRepository;
import com.chj.todolist.service.TodoItemService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class TodoController {

	private final TodoItemService todoItemService;
	private final UserRepository userRepository;
	
	// Todo 아이템 리스트를 가져와서 화면에 표시하는 메서드입니다.
    // @param model Thymeleaf 모델 객체
    // @return todoList.html 템플릿 이름
    @GetMapping("/")
	public String getAllTodoItems(Model model, Principal principal, @RequestParam(name = "filter", required = false) String filter) {
    	String username = principal.getName();
        User user = userRepository.findByUsername(username);
        
    	// 서비스를 통해 모든 Todo 아이템을 가져옴
		List<TodoItemDTO> todoItems;
		if ("completed".equals(filter)) {
            todoItems = todoItemService.getCompletedTodoItemsByUser(user);
        } else if ("incomplete".equals(filter)) {
            todoItems = todoItemService.getIncompleteTodoItemsByUser(user);
        } else {
            todoItems = todoItemService.getTodoItemsByUser(user);
        }
		
		// 모델에 Todo 아이템 리스트를 추가하여 뷰로 전달
		model.addAttribute("todoItems", todoItems);
		model.addAttribute("filter", filter);
		
		// todoList.html 템플릿으로 이동
		return "todoList";
	}
	
    // Todo 아이템의 완료 여부를 토글하는 메서드입니다.
    // @param id 토글할 Todo 아이템의 ID
    // @return 리스트 화면으로 리다이렉트
	@PostMapping("/toggle/{id}")
	public String toggleTodoItemComplete(@PathVariable Long id) {
		// 서비스를 통해 Todo 아이템의 완료 여부 토글
		todoItemService.toggleTodoItemComplete(id);
		
		// 리스트 화면으로 리다이렉트
		return "redirect:/";
	}
	
	// 사용자로부터 입력받은 설명으로 새로운 Todo 아이템을 추가하는 메서드입니다.
    // @param description 새로운 Todo 아이템의 설명
    // @return 리스트 화면으로 리다이렉트
	@PostMapping("/add")
    public String addTodoItem(@RequestParam String description, @RequestParam String category, @AuthenticationPrincipal User user) {
		// 서비스를 통해 새로운 Todo 아이템 추가
        todoItemService.addTodoItem(description, category, user);
        
     // 리스트 화면으로 리다이렉트
        return "redirect:/";
    }
	
	@PostMapping("/delete/{id}")
	public String deleteTodoItem(@PathVariable Long id) {
		todoItemService.deleteTodoItem(id);
		
		return "redirect:/";
	}
}