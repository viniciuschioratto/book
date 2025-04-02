package com.bookstore.book.application.core.usecase.strategy;

import com.bookstore.book.application.core.domain.BookTypeDomain;
import com.bookstore.book.application.core.interfaces.BookPriceStrategy;

import java.util.HashMap;
import java.util.Map;

public class BookPriceStrategyFactory {
    private static final Map<BookTypeDomain, BookPriceStrategy> strategies = new HashMap<>();

    static {
        strategies.put(BookTypeDomain.NEW_RELEASE, new NewReleaseBookPriceStrategy());
        strategies.put(BookTypeDomain.REGULAR, new RegularBookPriceStrategy());
        strategies.put(BookTypeDomain.OLD_EDITION, new OldEditionBookPriceStrategy());
    }

    public static BookPriceStrategy getStrategy(BookTypeDomain bookType) {
        return strategies.get(bookType);
    }
}
