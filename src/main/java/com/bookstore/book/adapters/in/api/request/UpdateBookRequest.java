package com.bookstore.book.adapters.in.api.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateBookRequest {
    private Long id;
    private String title;
    private Float base_price;
    private int quantity;
    private BookTypeCreateRequest type;
    private String author;
    private String description;
}
