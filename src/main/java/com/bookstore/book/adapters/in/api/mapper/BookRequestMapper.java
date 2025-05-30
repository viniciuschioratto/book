package com.bookstore.book.adapters.in.api.mapper;

import com.bookstore.book.adapters.in.api.request.CreateBookRequest;
import com.bookstore.book.adapters.in.api.request.UpdateBookRequest;
import com.bookstore.book.adapters.in.api.response.BookResponse;
import com.bookstore.book.application.core.domain.BookDomain;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookRequestMapper {
    BookDomain createBookRequestToBookDomain(CreateBookRequest createBookRequest);
    BookResponse bookDomainToBookResponse(BookDomain bookDomain);
    List<BookResponse> bookDomainsToBookResponses(List<BookDomain> bookDomains);
    BookDomain updateBookRequestToBookDomain(UpdateBookRequest updateBookRequest);
}
