package com.bookstore.book.adapters.out.repository;

import com.bookstore.book.adapters.out.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<BookEntity, Long> {}
