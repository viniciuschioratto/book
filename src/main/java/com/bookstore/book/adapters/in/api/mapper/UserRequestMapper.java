package com.bookstore.book.adapters.in.api.mapper;

import com.bookstore.book.adapters.in.api.request.CreateUserRequest;
import com.bookstore.book.adapters.in.api.request.UpdateUserRequest;
import com.bookstore.book.adapters.in.api.response.UserResponse;
import com.bookstore.book.application.core.domain.UserDomain;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserRequestMapper {
    UserDomain fromCreateUserRequestToUserDomain(CreateUserRequest createUserRequest);
    UserResponse fromUserDomainToUserResponse(UserDomain userDomain);
    UserDomain fromUpdateUserRequestToUserDomain(UpdateUserRequest updateUserRequest);
}
