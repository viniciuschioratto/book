package com.bookstore.book.application.ports.in;

import com.bookstore.book.application.core.domain.UserDomain;
import com.bookstore.book.application.core.exceptions.UserNotFoundException;

public interface UserCrudInputPort {
    UserDomain createUser(UserDomain userDomain);
    UserDomain updateUser(Long userId, UserDomain userDomain) throws UserNotFoundException;
    void deleteUser(Long userId) throws UserNotFoundException;
    UserDomain getUserByEmail(String userEmail) throws UserNotFoundException;
}
