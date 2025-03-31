package com.bookstore.book.config.application;

import com.bookstore.book.adapters.out.impl.BookCrudAdapter;
import com.bookstore.book.application.core.usecase.BookCrudImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class BookCrudConfig {

    @Bean
    @Scope("singleton")
    public BookCrudImpl bookCrudImpl(BookCrudAdapter bookCrudAdapter) {
        return new BookCrudImpl(bookCrudAdapter);
    }
}
