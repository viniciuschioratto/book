package com.bookstore.book.adapters.in.api.response;

import com.bookstore.book.adapters.in.api.request.BookTypeCreateRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateBookResponse {
    private Long id;
    private String title;
    private Float base_price;
    private int quantity;
    private BookTypeCreateRequest type;
}
