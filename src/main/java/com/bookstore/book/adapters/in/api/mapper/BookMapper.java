package com.bookstore.book.adapters.in.api.mapper;

import com.bookstore.book.adapters.in.api.request.CreateBookRequest;
import com.bookstore.book.adapters.in.api.response.BookResponse;
import com.bookstore.book.adapters.in.api.response.CreateBookResponse;
import com.bookstore.book.application.core.domain.BookDomain;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookMapper {
    BookDomain createBookRequestToBookDomain(CreateBookRequest createBookRequest);
    CreateBookResponse bookDomainToCreateBookResponse(BookDomain bookDomain);
    BookResponse bookDomainToBookResponse(BookDomain bookDomain);
}
