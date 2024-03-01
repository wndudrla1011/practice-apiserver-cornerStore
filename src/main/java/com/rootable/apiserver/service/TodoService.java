package com.rootable.apiserver.service;

import com.rootable.apiserver.domain.Todo;
import com.rootable.apiserver.dto.PageRequestDTO;
import com.rootable.apiserver.dto.PageResponseDTO;
import com.rootable.apiserver.dto.TodoDTO;

public interface TodoService {

    TodoDTO get(Long tno);

    Long register(TodoDTO dto);

    Long modify(Long tno, TodoDTO dto);

    void remove(Long tno);

    PageResponseDTO<TodoDTO> getList(PageRequestDTO pageRequestDTO);

    default TodoDTO entityToDTO(Todo todoDTO) {

        return TodoDTO.builder()
                .tno(todoDTO.getTno())
                .title(todoDTO.getTitle())
                .content(todoDTO.getContent())
                .complete(todoDTO.isComplete()) //관례: boolean -> is~
                .dueDate(todoDTO.getDueDate())
                .build();

    }

    default Todo dtoToEntity(TodoDTO todoDTO) {

        return Todo.builder()
                .tno(todoDTO.getTno())
                .title(todoDTO.getTitle())
                .content(todoDTO.getContent())
                .complete(todoDTO.isComplete())
                .dueDate(todoDTO.getDueDate())
                .build();

    }

}
