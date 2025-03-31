package com.bookstore.book.adapters.out.mapper;

import com.bookstore.book.adapters.out.entity.BookEntity;
import com.bookstore.book.application.core.domain.BookDomain;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring")
public interface BookEntityMapper {
    BookEntity fromBookDomainToBookEntity(BookDomain bookDomain);
    default Optional<BookDomain> fromBookEntityOptionalToBookDomainOptional(Optional<BookEntity> bookEntityOptional) {
        return bookEntityOptional.map(this::fromBookEntityToBookDomain);
    }
    BookDomain fromBookEntityToBookDomain(BookEntity bookEntity);
    List<BookDomain> fromBookEntitiesToBookDomains(List<BookEntity> bookEntities);
}
