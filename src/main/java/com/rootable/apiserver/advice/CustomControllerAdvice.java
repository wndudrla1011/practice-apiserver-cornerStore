package com.rootable.apiserver.advice;

import com.rootable.apiserver.util.CustomJWTException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class CustomControllerAdvice {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> notExist(NoSuchElementException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("msg", e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> notValid(MethodArgumentNotValidException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("msg", e.getMessage()));
    }

    @ExceptionHandler(CustomJWTException.class)
    public ResponseEntity<?> handleJWTException(CustomJWTException e) {
        return ResponseEntity.ok().body(Map.of("error", e.getMessage()));
    }

}
