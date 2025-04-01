package com.bookstore.book.application.core.usecase;

import com.bookstore.book.application.core.domain.UserDomain;
import com.bookstore.book.application.core.exceptions.UserNotFoundException;
import com.bookstore.book.application.ports.in.UserCrudInputPort;
import com.bookstore.book.application.ports.out.UserCrudOutputPort;

import java.util.Optional;
import java.util.logging.Logger;

public class UserCrudImpl implements UserCrudInputPort {
    private final Logger logger = Logger.getLogger(UserCrudImpl.class.getName());
    private final UserCrudOutputPort userCrudOutputPort;

    public UserCrudImpl(UserCrudOutputPort userCrudOutputPort) {
        this.userCrudOutputPort = userCrudOutputPort;
    }

    @Override
    public UserDomain createUser(UserDomain userDomain) {
        return userCrudOutputPort.createUser(userDomain);
    }

    @Override
    public UserDomain updateUser(Long userId, UserDomain userDomain) throws UserNotFoundException {
        getUserById(userId);
        return userCrudOutputPort.updateUser(userDomain);
    }

    @Override
    public void deleteUser(Long userId) throws UserNotFoundException {
        getUserById(userId);
        userCrudOutputPort.deleteUser(userId);
    }

    @Override
    public UserDomain getUserByEmail(String userEmail) throws UserNotFoundException {
        Optional<UserDomain> userDomain = userCrudOutputPort.getUserByEmail(userEmail);
        return userDomain.orElseThrow(() -> {
            logger.warning("User not found");
            return new UserNotFoundException("User not found with email: " + userEmail);
        });
    }

    @Override
    public void updateUserLoyaltyPoints(Long loyaltyPoints, Long userId) {
        userCrudOutputPort.updateUserLoyaltyPoints(loyaltyPoints, userId);
    }

    private UserDomain getUserById(Long userId) throws UserNotFoundException {
        Optional<UserDomain> userDomain = userCrudOutputPort.getUserById(userId);
        return userDomain.orElseThrow(() -> {
            logger.warning("User not found");
            return new UserNotFoundException("User not found with id: " + userId);
        });
    }
}
