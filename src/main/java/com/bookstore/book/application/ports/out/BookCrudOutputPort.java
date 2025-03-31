package com.bookstore.book.application.ports.out;

import com.bookstore.book.application.core.domain.BookDomain;

import java.util.List;
import java.util.Optional;

public interface BookCrudOutputPort {
    BookDomain createBook(BookDomain bookDomain);
    Optional<BookDomain> getBookById(Long id);
    BookDomain updateBook(BookDomain bookDomain);
    void deleteBook(Long id);
    List<BookDomain> getAllBooks();
}
