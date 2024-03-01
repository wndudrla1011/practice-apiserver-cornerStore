package com.rootable.apiserver.repository;

import com.rootable.apiserver.domain.Todo;
import com.rootable.apiserver.repository.search.TodoSearch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long>, TodoSearch {
}
