package com.bookstore.book.application.core.usecase;

import com.bookstore.book.application.core.domain.BookDomain;
import com.bookstore.book.application.core.exceptions.BookNotFoundException;
import com.bookstore.book.application.ports.in.BookCrudInputPort;
import com.bookstore.book.application.ports.out.BookCrudOutputPort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class BookCrudImpl implements BookCrudInputPort {
    private final Logger logger = Logger.getLogger(BookCrudImpl.class.getName());
    private final BookCrudOutputPort bookCrudOutputPort;

    public BookCrudImpl(BookCrudOutputPort bookCrudOutputPort) {
        this.bookCrudOutputPort = bookCrudOutputPort;
    }

    @Override
    public BookDomain createBook(BookDomain bookDomain) {
        return bookCrudOutputPort.createBook(bookDomain);
    }

    @Override
    public BookDomain getBookById(Long id) throws BookNotFoundException {
        Optional<BookDomain> book = bookCrudOutputPort.getBookById(id);
        return book.orElseThrow(() -> {
            logger.warning("Book not found");
            return new BookNotFoundException("Book not found with id: " + id);
        });
    }

    @Override
    public BookDomain updateBook(Long bookId, BookDomain bookDomain) throws BookNotFoundException {
        getBookById(bookId);
        return bookCrudOutputPort.updateBook(bookDomain);
    }

    @Override
    public void deleteBook(Long id) throws BookNotFoundException {
        getBookById(id);
        bookCrudOutputPort.deleteBook(id);
    }

    @Override
    public List<BookDomain> getAllBooks() {
        return bookCrudOutputPort.getAllBooks();
    }

    @Override
    public List<BookDomain> getBooksByIds(List<Long> bookIds) {
        return bookCrudOutputPort.getBooksByIds(bookIds);
    }

    @Transactional
    @Override
    public void updateBookQuantity(int quantity, Long bookId) {
        bookCrudOutputPort.updateBookQuantity(quantity, bookId);
    }
}
