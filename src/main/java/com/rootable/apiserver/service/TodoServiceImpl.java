package com.rootable.apiserver.service;

import com.rootable.apiserver.domain.Todo;
import com.rootable.apiserver.dto.PageRequestDTO;
import com.rootable.apiserver.dto.PageResponseDTO;
import com.rootable.apiserver.dto.TodoDTO;
import com.rootable.apiserver.repository.TodoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;

    @Override
    public TodoDTO get(Long tno) {

        Todo result = todoRepository.findById(tno).orElseThrow();

        return entityToDTO(result);

    }

    @Override
    @Transactional
    public Long register(TodoDTO dto) {

        Todo todo = dtoToEntity(dto);

        Todo result = todoRepository.save(todo);

        return result.getTno();

    }

    @Override
    @Transactional
    public Long modify(Long tno, TodoDTO dto) {

        Todo todo = todoRepository.findById(tno).orElseThrow();

        todo.changeTitle(dto.getTitle());
        todo.changeContent(dto.getContent());
        todo.changeComplete(dto.isComplete());
        todo.changeDueDate(dto.getDueDate());

        return tno;

    }

    @Override
    @Transactional
    public void remove(Long tno) {
        todoRepository.deleteById(tno);
    }

    @Override
    public PageResponseDTO<TodoDTO> getList(PageRequestDTO pageRequestDTO) {

        //JPA
        Page<Todo> result = todoRepository.search1(pageRequestDTO);

        /*
        * 화면에는 TodoDTO 가 나가야 함
        * List(Todo -> TodoDTO)
        * */
        List<TodoDTO> dtoList = result.get().map(this::entityToDTO).toList();

        long totalCount = result.getTotalElements(); //전체 데이터 개수

        return PageResponseDTO.<TodoDTO>withAll()
                .dtoList(dtoList)
                .pageRequestDTO(pageRequestDTO)
                .totalCount((int) totalCount)
                .build();

    }

}
