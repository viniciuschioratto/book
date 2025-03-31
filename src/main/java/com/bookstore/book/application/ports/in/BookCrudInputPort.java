package com.bookstore.book.application.ports.in;

import com.bookstore.book.application.core.domain.BookDomain;
import com.bookstore.book.application.core.exceptions.BookNotFoundException;

public interface BookCrudInputPort {
    BookDomain createBook(BookDomain bookDomain);
    BookDomain getBookById(Long id) throws BookNotFoundException;
    BookDomain updateBook(BookDomain bookDomain);
    void deleteBook(Long id);
}
