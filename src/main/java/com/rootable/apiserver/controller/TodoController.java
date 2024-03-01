package com.rootable.apiserver.controller;

import com.rootable.apiserver.dto.PageRequestDTO;
import com.rootable.apiserver.dto.PageResponseDTO;
import com.rootable.apiserver.dto.TodoDTO;
import com.rootable.apiserver.service.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/todo")
public class TodoController {

    private final TodoService todoService;

    /*
    * 단건 조회
    * */
    @GetMapping("/{tno}")
    public TodoDTO get(@PathVariable("tno") Long tno) {
        return todoService.get(tno);
    }

    /*
    * 목록 조회
    * */
    @GetMapping("/list")
    public PageResponseDTO<TodoDTO> list(PageRequestDTO pageRequestDTO) {

        log.info("list : " + pageRequestDTO);

        return todoService.getList(pageRequestDTO);

    }

    /*
    * Todo 등록
    * */
    @PostMapping("/")
    public Map<String, Long> register(@RequestBody TodoDTO dto) {

        log.info("todoDTO : " + dto);

        Long tno = todoService.register(dto);

        return Map.of("TNO", tno);

    }

    /*
    * 수정
    * */
    @PutMapping("/{tno}")
    public Map<String, String> modify(@PathVariable("tno") Long tno, @RequestBody TodoDTO dto) {

        dto.setTno(tno);

        todoService.modify(tno, dto);

        return Map.of("RESULT", "SUCCESS");

    }

    /*
    * 삭제
    * */
    @DeleteMapping("/{tno}")
    public Map<String, String> remove(@PathVariable("tno") Long tno) {
        todoService.remove(tno);
        return Map.of("RESULT", "SUCCESS");
    }

}
