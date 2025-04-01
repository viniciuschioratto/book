package com.bookstore.book.adapters.in.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CalculateBookPriceResponse {
    private BookResponse book;
    private float totalPrice;
    private float discount;
    private boolean loyalty_points;
}
