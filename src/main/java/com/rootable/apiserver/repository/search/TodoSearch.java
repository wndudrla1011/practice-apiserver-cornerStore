package com.rootable.apiserver.repository.search;

import com.rootable.apiserver.domain.Todo;
import com.rootable.apiserver.dto.PageRequestDTO;
import org.springframework.data.domain.Page;

public interface TodoSearch {

    Page<Todo> search1(PageRequestDTO pageRequestDTO);

}
