package com.bookstore.book.application.core.interfaces;

import com.bookstore.book.application.core.domain.BookDomain;
import com.bookstore.book.application.core.domain.CalculateBookPriceDomain;

public interface BookPriceStrategy {
    CalculateBookPriceDomain calculatePrice(BookDomain bookDomain, Long numberOfBooks);
}
