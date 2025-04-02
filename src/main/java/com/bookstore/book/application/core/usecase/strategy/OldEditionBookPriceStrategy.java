package com.bookstore.book.application.core.usecase.strategy;

import com.bookstore.book.application.core.domain.BookDomain;
import com.bookstore.book.application.core.domain.CalculateBookPriceDomain;
import com.bookstore.book.application.core.interfaces.BookPriceStrategy;

public class OldEditionBookPriceStrategy implements BookPriceStrategy {
    @Override
    public CalculateBookPriceDomain calculatePrice(BookDomain bookDomain, Long numberOfBooks) {
        return new CalculateBookPriceDomain.DomainBuilder()
                .book(bookDomain)
                .loyalty_points(false)
                .totalPrice(bookDomain.getBase_price())
                .discount(numberOfBooks >= 3 ? bookDomain.getBase_price() * 0.25f : bookDomain.getBase_price() * 0.20f)
                .build();
    }
}
