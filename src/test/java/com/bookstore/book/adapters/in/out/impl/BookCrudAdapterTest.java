package com.bookstore.book.adapters.in.out.impl;

import com.bookstore.book.adapters.out.entity.BookEntity;
import com.bookstore.book.adapters.out.impl.BookCrudAdapter;
import com.bookstore.book.adapters.out.mapper.BookEntityMapper;
import com.bookstore.book.adapters.out.repository.BookRepository;
import com.bookstore.book.application.core.domain.BookDomain;
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
public class BookCrudAdapterTest {
    private BookRepository bookRepository;
    private BookEntityMapper bookEntityMapper;
    private BookCrudAdapter service;

    @BeforeEach
    void setup() {
        bookRepository = Mockito.spy(BookRepository.class);
        bookEntityMapper = Mockito.spy(BookEntityMapper.class);

        service = new BookCrudAdapter(bookRepository, bookEntityMapper);
    }

    @DisplayName("Should create a new book")
    @Test
    void should_createANewBook() {
        BookDomain bookDomain = BookDomain.builder()
                .author("Author")
                .base_price(10.0f)
                .build();

        BookEntity bookEntity = BookEntity.builder()
                .author("Author")
                .base_price(10.0f)
                .build();

        BookDomain newBookDomain = BookDomain.builder()
                .id(1L)
                .author("Author")
                .base_price(10.0f)
                .build();

        Mockito.when(bookEntityMapper.fromBookDomainToBookEntityCreate(bookDomain)).thenReturn(bookEntity);
        Mockito.when(bookRepository.saveAndFlush(bookEntity)).thenReturn(bookEntity);
        Mockito.when(bookEntityMapper.fromBookEntityToBookDomain(bookEntity)).thenReturn(newBookDomain);

        BookDomain response = service.createBook(bookDomain);

        Assertions.assertNotNull(response);
        Mockito.verify(bookEntityMapper, Mockito.times(1)).fromBookDomainToBookEntityCreate(bookDomain);
        Mockito.verify(bookRepository, Mockito.times(1)).saveAndFlush(bookEntity);
        Mockito.verify(bookEntityMapper, Mockito.times(1)).fromBookEntityToBookDomain(bookEntity);
        Mockito.verifyNoMoreInteractions(bookEntityMapper);
        Mockito.verifyNoMoreInteractions(bookRepository);
    }

    @DisplayName("Should get a book by Id")
    @Test
    void should_getABookById() {

        Long bookId = 1L;

        BookEntity bookEntity = BookEntity.builder()
                .id(1L)
                .author("Author")
                .base_price(10.0f)
                .build();

        BookDomain bookDomain = BookDomain.builder()
                .id(1L)
                .author("Author")
                .base_price(10.0f)
                .build();

        Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.of(bookEntity));
        Mockito.when(bookEntityMapper.fromBookEntityOptionalToBookDomainOptional(Optional.of(bookEntity))).thenReturn(Optional.of(bookDomain));

        Optional<BookDomain> response = service.getBookById(bookId);

        Assertions.assertNotNull(response);
        Mockito.verify(bookRepository, Mockito.times(1)).findById(bookId);
        Mockito.verify(bookEntityMapper, Mockito.times(2)).fromBookEntityOptionalToBookDomainOptional(Optional.of(bookEntity));
        Mockito.verifyNoMoreInteractions(bookEntityMapper);
        Mockito.verifyNoMoreInteractions(bookRepository);
    }

    @DisplayName("Should update a book")
    @Test
    void should_updateABook() {
        BookDomain newBookDomain = BookDomain.builder()
                .id(1L)
                .author("Author updated")
                .base_price(10.0f)
                .build();

        BookEntity newBookEntity = BookEntity.builder()
                .id(1L)
                .author("Author updated")
                .base_price(10.0f)
                .build();

        Mockito.when(bookEntityMapper.fromBookDomainToBookEntity(newBookDomain)).thenReturn(newBookEntity);
        Mockito.when(bookRepository.saveAndFlush(newBookEntity)).thenReturn(newBookEntity);
        Mockito.when(bookEntityMapper.fromBookEntityToBookDomain(newBookEntity)).thenReturn(newBookDomain);

        BookDomain response = service.updateBook(newBookDomain);

        Assertions.assertNotNull(response);
        Mockito.verify(bookEntityMapper, Mockito.times(1)).fromBookDomainToBookEntity(newBookDomain);
        Mockito.verify(bookRepository, Mockito.times(1)).saveAndFlush(newBookEntity);
        Mockito.verify(bookEntityMapper, Mockito.times(1)).fromBookEntityToBookDomain(newBookEntity);
        Mockito.verifyNoMoreInteractions(bookEntityMapper);
        Mockito.verifyNoMoreInteractions(bookRepository);
    }

    @DisplayName("Should delete a book")
    @Test
    void should_deleteABook() {

        Long bookId = 1L;

        Mockito.doNothing().when(bookRepository).logicalDeleteById(bookId);

        service.deleteBook(bookId);

        Mockito.verify(bookRepository, Mockito.times(1)).logicalDeleteById(bookId);
        Mockito.verifyNoMoreInteractions(bookRepository);
    }

    @DisplayName("Should get a list of book")
    @Test
    void should_getAListOfBook() {

        BookEntity bookEntity = BookEntity.builder()
                .id(1L)
                .author("Author")
                .base_price(10.0f)
                .build();

        BookDomain bookDomain = BookDomain.builder()
                .id(1L)
                .author("Author")
                .base_price(10.0f)
                .build();

        Mockito.when(bookRepository.findAllByActiveTrue()).thenReturn(List.of(bookEntity));
        Mockito.when(bookEntityMapper.fromBookEntitiesToBookDomains(List.of(bookEntity))).thenReturn(List.of(bookDomain));

        List<BookDomain> response = service.getAllBooks();

        Assertions.assertNotNull(response);
        Mockito.verify(bookRepository, Mockito.times(1)).findAllByActiveTrue();
        Mockito.verify(bookEntityMapper, Mockito.times(1)).fromBookEntitiesToBookDomains(List.of(bookEntity));
        Mockito.verifyNoMoreInteractions(bookEntityMapper);
        Mockito.verifyNoMoreInteractions(bookRepository);
    }
}
