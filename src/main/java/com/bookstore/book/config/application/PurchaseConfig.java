package com.bookstore.book.config.application;

import com.bookstore.book.adapters.out.impl.PurchaseAdapter;
import com.bookstore.book.application.core.usecase.BookCrudImpl;
import com.bookstore.book.application.core.usecase.PurchaseImpl;
import com.bookstore.book.application.core.usecase.UserCrudImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class PurchaseConfig {

    @Bean
    @Scope("singleton")
    public PurchaseImpl purchaseImpl(
            PurchaseAdapter purchaseAdapter,
            UserCrudImpl userCrudImpl,
            BookCrudImpl bookCrudImpl
    ) {
        return new PurchaseImpl(purchaseAdapter, userCrudImpl, bookCrudImpl);
    }
}
