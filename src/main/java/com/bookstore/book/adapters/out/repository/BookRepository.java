package com.bookstore.book.adapters.out.repository;

import com.bookstore.book.adapters.out.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository<BookEntity, Long> {

    @Modifying
    @Query(value = "UPDATE book_bookstore SET active = false WHERE id = :id", nativeQuery = true)
    void logicalDeleteById(Long id);

    List<BookEntity> findAllByActiveTrue();

    @Modifying
    @Query(value = "UPDATE book_bookstore SET quantity = :quantity WHERE id = :id", nativeQuery = true)
    void updateBookQuantity(int quantity, Long id);
}
