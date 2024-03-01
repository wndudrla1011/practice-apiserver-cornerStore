package com.rootable.apiserver.service;

import com.rootable.apiserver.dto.PageRequestDTO;
import com.rootable.apiserver.dto.TodoDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
@Log4j2
public class TodoServiceTests {

    @Autowired
    TodoService todoService;

    @Test
    public void get() throws Exception{

        Long tno = 600L;

        log.info(todoService.get(tno));

    }

    @Test
    public void register() throws Exception{

        TodoDTO todoDTO = TodoDTO.builder()
                .title("Title")
                .content("Content")
                .dueDate(LocalDate.of(2024, 2, 17))
                .build();

        log.info(todoService.register(todoDTO));

    }

    @Test
    public void updateDTO() throws Exception{

        //given
        TodoDTO todoDTO = TodoDTO.builder()
                .title("Title")
                .content("Content")
                .complete(false)
                .dueDate(LocalDate.of(2024, 2, 17))
                .build();

        TodoDTO updateDTO = TodoDTO.builder()
                .title("New Title")
                .content("New Content")
                .complete(true)
                .dueDate(LocalDate.of(2024, 2, 17))
                .build();

        Long savedId = todoService.register(todoDTO);

        //when
        todoService.modify(savedId, updateDTO);

        //then
        log.info(todoService.get(savedId));

    }
    
    @Test
    public void getList() throws Exception{

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(11).build();

        log.info(todoService.getList(pageRequestDTO));

    }

}
