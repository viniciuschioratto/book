package com.bookstore.book.application.core.usecase;

import com.bookstore.book.application.core.domain.UserDomain;
import com.bookstore.book.application.core.exceptions.UserNotFoundException;
import com.bookstore.book.application.ports.out.UserCrudOutputPort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class UserCrudImplTest {
    private UserCrudOutputPort userCrudOutputPort;
    private UserCrudImpl service;

    @BeforeEach
    void setup() {
        userCrudOutputPort = Mockito.spy(UserCrudOutputPort.class);

        service = new UserCrudImpl(userCrudOutputPort);
    }

    @DisplayName("Should create a new user")
    @Test
    void should_createANewUser() {
        UserDomain userDomain = UserDomain.builder()
                .email("user@example.com")
                .build();

        Mockito.when(userCrudOutputPort.createUser(userDomain)).thenReturn(userDomain);

        UserDomain response = service.createUser(userDomain);

        Assertions.assertNotNull(response);
        Mockito.verify(userCrudOutputPort, Mockito.times(1)).createUser(userDomain);
        Mockito.verifyNoMoreInteractions(userCrudOutputPort);
    }

    @DisplayName("Should update a user")
    @Test
    void should_updateAUser() throws UserNotFoundException {
        Long userId = 1L;
        UserDomain userDomain = UserDomain.builder()
                .id(userId)
                .email("user@example.com")
                .build();

        UserDomain userDomainUpdate = UserDomain.builder()
                .id(userId)
                .email("updated@example.com")
                .build();

        Mockito.when(userCrudOutputPort.getUserById(userId)).thenReturn(Optional.of(userDomain));
        Mockito.when(userCrudOutputPort.updateUser(userDomainUpdate)).thenReturn(userDomainUpdate);

        UserDomain response = service.updateUser(userId, userDomainUpdate);

        Assertions.assertNotNull(response);
        Mockito.verify(userCrudOutputPort, Mockito.times(1)).getUserById(userId);
        Mockito.verify(userCrudOutputPort, Mockito.times(1)).updateUser(userDomainUpdate);
        Mockito.verifyNoMoreInteractions(userCrudOutputPort);
    }

    @DisplayName("Should get a user by email")
    @Test
    void should_getAUserByEmail() throws UserNotFoundException {
        String userEmail = "user@example.com";
        UserDomain userDomain = UserDomain.builder()
                .email(userEmail)
                .build();

        Mockito.when(userCrudOutputPort.getUserByEmail(userEmail)).thenReturn(Optional.of(userDomain));

        UserDomain response = service.getUserByEmail(userEmail);

        Assertions.assertNotNull(response);
        Mockito.verify(userCrudOutputPort, Mockito.times(1)).getUserByEmail(userEmail);
        Mockito.verifyNoMoreInteractions(userCrudOutputPort);
    }

    @DisplayName("Should update user loyalty points")
    @Test
    void should_updateUserLoyaltyPoints() {
        Long userId = 1L;
        Long loyaltyPoints = 100L;

        Mockito.doNothing().when(userCrudOutputPort).updateUserLoyaltyPoints(loyaltyPoints, userId);

        service.updateUserLoyaltyPoints(loyaltyPoints, userId);

        Mockito.verify(userCrudOutputPort, Mockito.times(1)).updateUserLoyaltyPoints(loyaltyPoints, userId);
        Mockito.verifyNoMoreInteractions(userCrudOutputPort);
    }
}
