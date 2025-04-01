package com.bookstore.book.adapters.in.api;

import com.bookstore.book.adapters.in.api.response.ExceptionResponse;
import com.bookstore.book.application.core.exceptions.BookNotFoundException;
import com.bookstore.book.application.core.exceptions.PurchaseNotFoundException;
import com.bookstore.book.application.core.exceptions.UserNotFoundException;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@Hidden
@RestControllerAdvice
public class ApiAdvice {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionResponse handleException(Exception exception) {
        log.info("Api advice got Exception with message: {}", exception.getMessage());
        return ExceptionResponse.builder()
                .message(exception.getMessage())
                .exceptionType(exception.getClass().getSimpleName())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();
    }

    @ExceptionHandler(BookNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse handleBookNotFoundException(BookNotFoundException exception) {
        log.info("Api advice got BookNotFoundException with message: {}", exception.getMessage());
        return ExceptionResponse.builder()
                .message(exception.getMessage())
                .exceptionType(exception.getClass().getSimpleName())
                .status(HttpStatus.NOT_FOUND.value())
                .build();
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse handleUserNotFoundException(UserNotFoundException exception) {
        log.info("Api advice got UserNotFoundException with message: {}", exception.getMessage());
        return ExceptionResponse.builder()
                .message(exception.getMessage())
                .exceptionType(exception.getClass().getSimpleName())
                .status(HttpStatus.NOT_FOUND.value())
                .build();
    }

    @ExceptionHandler(PurchaseNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse handlePurchaseNotFoundException(PurchaseNotFoundException exception) {
        log.info("Api advice got PurchaseNotFoundException with message: {}", exception.getMessage());
        return ExceptionResponse.builder()
                .message(exception.getMessage())
                .exceptionType(exception.getClass().getSimpleName())
                .status(HttpStatus.NOT_FOUND.value())
                .build();
    }
}
