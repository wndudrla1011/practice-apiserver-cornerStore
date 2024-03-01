package com.rootable.apiserver.repository;

import com.rootable.apiserver.domain.Todo;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
public class TodoRepositoryTests {

    @Autowired
    private TodoRepository todoRepository;

    @Test
    public void test1() throws Exception{

        assertNotNull(todoRepository);

        log.info(">>>> " + todoRepository.getClass().getName());

    }
    
    @Test
    public void save() throws Exception{
        for (int i = 0; i < 100; i++) {
            Todo todo = Todo.builder()
                    .title("Title " + i)
                    .content("Content..." + i)
                    .complete(false)
                    .dueDate(LocalDate.of(2024, 2, 29))
                    .build();

            Todo result = todoRepository.save(todo);

            log.info(result);
        }
    }

    @Test
    public void update() throws Exception{
        //given
        Todo init_todo = Todo.builder()
                .title("Title")
                .content("Content...")
                .complete(false)
                .dueDate(LocalDate.of(2024, 2, 29))
                .build();

        //when
        init_todo.changeTitle("Update title");
        init_todo.changeContent("Update content");
        init_todo.changeComplete(true);

        Long savedId = todoRepository.save(init_todo).getTno();

        //then
        Todo updatedTodo = todoRepository.findById(savedId)
                .orElseThrow(() -> new IllegalArgumentException("해당 객체가 존재하지 않습니다."));

        assertThat(updatedTodo.getTitle()).isEqualTo("Update title");
        assertThat(updatedTodo.getContent()).isEqualTo("Update content");
        assertThat(updatedTodo.isComplete()).isEqualTo(true);

    }

    @Test
    public void paging() throws Exception{

        Pageable pageable = PageRequest.of(0, 10, Sort.by("tno").descending());

        Page<Todo> result = todoRepository.findAll(pageable);

        log.info(result.getTotalElements());

        log.info(result.getContent());

    }

//    @Test
//    public void search1() throws Exception {
//
//        todoRepository.search1();
//
//    }

}
