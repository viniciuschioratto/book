package com.bookstore.book.adapters.in.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CalculatePriceResponse {
    private List<BookResponse> books;
    private boolean hasDiscount;
    private float discount;
    private float totalPriceWithoutDiscount;
    private float totalPriceWithDiscount;
}
