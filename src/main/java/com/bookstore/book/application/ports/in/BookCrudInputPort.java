package com.bookstore.book.application.ports.in;

import com.bookstore.book.application.core.domain.BookDomain;
import com.bookstore.book.application.core.exceptions.BookNotFoundException;

import java.util.List;

public interface BookCrudInputPort {
    BookDomain createBook(BookDomain bookDomain);
    BookDomain getBookById(Long id) throws BookNotFoundException;
    BookDomain updateBook(BookDomain bookDomain);
    void deleteBook(Long id) throws BookNotFoundException;
    List<BookDomain> getAllBooks();
}
