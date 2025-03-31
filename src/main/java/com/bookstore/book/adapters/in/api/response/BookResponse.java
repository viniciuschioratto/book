package com.bookstore.book.adapters.in.api.response;

import com.bookstore.book.adapters.in.api.request.BookTypeCreateRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookResponse {
    private Long id;
    private String title;
    private Float base_price;
    private int quantity;
    private BookTypeCreateRequest type;
    private String author;
    private String description;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
}
