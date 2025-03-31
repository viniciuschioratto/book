package com.bookstore.book.application.core.usecase;

import com.bookstore.book.application.core.domain.BookDomain;
import com.bookstore.book.application.core.exceptions.BookNotFoundException;
import com.bookstore.book.application.ports.out.BookCrudOutputPort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class BookCrudImplTest {
    private BookCrudOutputPort bookCrudOutputPort;
    private BookCrudImpl service;

    @BeforeEach
    void setup() {
        bookCrudOutputPort = Mockito.spy(BookCrudOutputPort.class);

        service = new BookCrudImpl(bookCrudOutputPort);
    }

    @DisplayName("Should create a new book")
    @Test
    void should_createANewBook() {
        BookDomain bookDomain = BookDomain.builder()
                .author("Author")
                .base_price(10.0f)
                .build();

        Mockito.when(bookCrudOutputPort.createBook(bookDomain)).thenReturn(bookDomain);

        BookDomain response = service.createBook(bookDomain);

        Assertions.assertNotNull(response);
        Mockito.verify(bookCrudOutputPort, Mockito.times(1)).createBook(bookDomain);
        Mockito.verifyNoMoreInteractions(bookCrudOutputPort);
    }

    @DisplayName("Should get a book by Id")
    @Test
    void should_getABookById() throws BookNotFoundException {
        Long bookId = 1L;

        BookDomain bookDomain = BookDomain.builder()
                .id(1L)
                .author("Author")
                .base_price(10.0f)
                .build();

        Mockito.when(bookCrudOutputPort.getBookById(bookId)).thenReturn(Optional.of(bookDomain));

        BookDomain response = service.getBookById(bookId);

        Assertions.assertNotNull(response);
        Mockito.verify(bookCrudOutputPort, Mockito.times(1)).getBookById(bookId);
        Mockito.verifyNoMoreInteractions(bookCrudOutputPort);
    }

    @DisplayName("Should get a book by Id - Exception because book not found")
    @Test
    void should_getABookById_exception() {
        Long bookId = 1L;

        Mockito.when(bookCrudOutputPort.getBookById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(BookNotFoundException.class, () -> service.getBookById(bookId));

        Mockito.verify(bookCrudOutputPort, Mockito.times(1)).getBookById(1L);
        Mockito.verifyNoMoreInteractions(bookCrudOutputPort);
    }

    @DisplayName("Should update a book")
    @Test
    void should_updateABook() throws BookNotFoundException {
        Long bookId = 1L;

        BookDomain bookDomain = BookDomain.builder()
                .id(1L)
                .author("Author")
                .base_price(10.0f)
                .build();

        BookDomain bookDomainUpdate = BookDomain.builder()
                .id(1L)
                .author("Author update")
                .base_price(10.0f)
                .build();

        Mockito.when(bookCrudOutputPort.getBookById(bookId)).thenReturn(Optional.of(bookDomain));
        Mockito.when(bookCrudOutputPort.updateBook(bookDomainUpdate)).thenReturn(bookDomainUpdate);

        BookDomain response = service.updateBook(bookId, bookDomainUpdate);

        Assertions.assertNotNull(response);
        Mockito.verify(bookCrudOutputPort, Mockito.times(1)).getBookById(bookId);
        Mockito.verify(bookCrudOutputPort, Mockito.times(1)).updateBook(bookDomainUpdate);
        Mockito.verifyNoMoreInteractions(bookCrudOutputPort);
    }

    @DisplayName("Should delete a book")
    @Test
    void should_deleteABook() throws BookNotFoundException {

        Long bookId = 1L;

        BookDomain bookDomain = BookDomain.builder()
                .id(1L)
                .author("Author")
                .base_price(10.0f)
                .build();


        Mockito.when(bookCrudOutputPort.getBookById(bookId)).thenReturn(Optional.of(bookDomain));
        Mockito.doNothing().when(bookCrudOutputPort).deleteBook(bookId);

        service.deleteBook(bookId);

        Mockito.verify(bookCrudOutputPort, Mockito.times(1)).getBookById(bookId);
        Mockito.verify(bookCrudOutputPort, Mockito.times(1)).deleteBook(bookId);
        Mockito.verifyNoMoreInteractions(bookCrudOutputPort);
    }

    @DisplayName("Should delete a book - Exception because book not found")
    @Test
    void should_deleteABook_exception() {
        Long bookId = 1L;

        Mockito.when(bookCrudOutputPort.getBookById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(BookNotFoundException.class, () -> service.deleteBook(bookId));

        Mockito.verify(bookCrudOutputPort, Mockito.times(1)).getBookById(1L);
        Mockito.verifyNoMoreInteractions(bookCrudOutputPort);
    }

    @DisplayName("Should get a list of book")
    @Test
    void should_getAListOfBook() {

        BookDomain bookDomain = BookDomain.builder()
                .id(1L)
                .author("Author")
                .base_price(10.0f)
                .build();

        Mockito.when(bookCrudOutputPort.getAllBooks()).thenReturn(List.of(bookDomain));

        List<BookDomain> response = service.getAllBooks();

        Assertions.assertNotNull(response);
        Mockito.verify(bookCrudOutputPort, Mockito.times(1)).getAllBooks();
        Mockito.verifyNoMoreInteractions(bookCrudOutputPort);
    }
}
