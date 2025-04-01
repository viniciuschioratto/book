package com.bookstore.book.application.ports.out;

import com.bookstore.book.application.core.domain.UserDomain;

import java.util.Optional;

public interface UserCrudOutputPort {
    UserDomain createUser(UserDomain userDomain);
    UserDomain updateUser(UserDomain userDomain);
    void deleteUser(Long userId);
    Optional<UserDomain> getUserByEmail(String userEmail);
    Optional<UserDomain> getUserById(Long userId);
    void updateUserLoyaltyPoints(Long loyaltyPoints, Long userId);
}
