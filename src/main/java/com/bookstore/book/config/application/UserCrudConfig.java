package com.bookstore.book.config.application;

import com.bookstore.book.adapters.out.impl.UserCrudAdapter;
import com.bookstore.book.application.core.usecase.UserCrudImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class UserCrudConfig {

    @Bean
    @Scope("singleton")
    public UserCrudImpl userCrudImpl(UserCrudAdapter userCrudAdapter) {
        return new UserCrudImpl(userCrudAdapter);
    }
}
