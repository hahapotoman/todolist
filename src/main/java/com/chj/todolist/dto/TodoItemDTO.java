package com.chj.todolist.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TodoItemDTO {

	private Long id;
	private String description;
	private String category;
	private boolean completed;
	
}