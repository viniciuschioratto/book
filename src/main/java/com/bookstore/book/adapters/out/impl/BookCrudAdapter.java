package com.bookstore.book.adapters.out.impl;

import com.bookstore.book.adapters.out.entity.BookEntity;
import com.bookstore.book.adapters.out.mapper.BookEntityMapper;
import com.bookstore.book.adapters.out.repository.BookRepository;
import com.bookstore.book.application.core.domain.BookDomain;
import com.bookstore.book.application.ports.out.BookCrudOutputPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class BookCrudAdapter implements BookCrudOutputPort {
    private final BookRepository bookRepository;
    private final BookEntityMapper bookEntityMapper;

    public BookCrudAdapter(
            BookRepository bookRepository,
            BookEntityMapper bookEntityMapper
    ) {
        this.bookRepository = bookRepository;
        this.bookEntityMapper = bookEntityMapper;
    }

    @Override
    public BookDomain createBook(BookDomain bookDomain) {
        BookEntity entity = bookEntityMapper.fromBookDomainToBookEntityCreate(bookDomain);
        BookEntity newEntity = bookRepository.saveAndFlush(entity);
        return bookEntityMapper.fromBookEntityToBookDomain(newEntity);
    }

    @Override
    public Optional<BookDomain> getBookById(Long id) {
        return bookEntityMapper.fromBookEntityOptionalToBookDomainOptional(bookRepository.findById(id));
    }

    @Override
    public BookDomain updateBook(BookDomain bookDomain) {
        BookEntity entity = bookEntityMapper.fromBookDomainToBookEntity(bookDomain);
        return bookEntityMapper.fromBookEntityToBookDomain(bookRepository.saveAndFlush(entity));
    }

    @Transactional
    @Override
    public void deleteBook(Long id) {
        bookRepository.logicalDeleteById(id);
    }

    @Override
    public List<BookDomain> getAllBooks() {
        return bookEntityMapper.fromBookEntitiesToBookDomains(bookRepository.findAllByActiveTrue());
    }

    @Override
    public List<BookDomain> getBooksByIds(List<Long> bookIds) {
        return bookEntityMapper.fromBookEntitiesToBookDomains(
                bookRepository.findAllById(bookIds)
        );
    }

    @Override
    public void updateBookQuantity(int quantity, Long bookId) {
        bookRepository.updateBookQuantity(quantity, bookId);
    }
}
