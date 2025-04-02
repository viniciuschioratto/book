package com.bookstore.book.adapters.in.out.impl;

import com.bookstore.book.adapters.out.entity.UserEntity;
import com.bookstore.book.adapters.out.impl.UserCrudAdapter;
import com.bookstore.book.adapters.out.mapper.UserEntityMapper;
import com.bookstore.book.adapters.out.repository.UserRepository;
import com.bookstore.book.application.core.domain.UserDomain;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class UserCrudAdapterTest {
    private UserRepository userRepository;
    private UserEntityMapper userEntityMapper;
    private UserCrudAdapter service;

    @BeforeEach
    void setup() {
        userRepository = Mockito.spy(UserRepository.class);
        userEntityMapper = Mockito.spy(UserEntityMapper.class);

        service = new UserCrudAdapter(userRepository, userEntityMapper);
    }

    @DisplayName("Should create a new user")
    @Test
    void should_createANewUser() {
        UserDomain userDomain = UserDomain.builder()
                .name("Name")
                .build();

        UserEntity userEntity = UserEntity.builder()
                .name("Name")
                .build();

        UserDomain newUserDomain = UserDomain.builder()
                .id(1L)
                .name("Name")
                .build();

        Mockito.when(userEntityMapper.fromUserDomainToUserEntityCreate(userDomain)).thenReturn(userEntity);
        Mockito.when(userRepository.saveAndFlush(userEntity)).thenReturn(userEntity);
        Mockito.when(userEntityMapper.fromUserEntityToUserDomain(userEntity)).thenReturn(newUserDomain);

        UserDomain response = service.createUser(userDomain);

        Assertions.assertNotNull(response);
        Mockito.verify(userEntityMapper, Mockito.times(1)).fromUserDomainToUserEntityCreate(userDomain);
        Mockito.verify(userRepository, Mockito.times(1)).saveAndFlush(userEntity);
        Mockito.verify(userEntityMapper, Mockito.times(1)).fromUserEntityToUserDomain(userEntity);
        Mockito.verifyNoMoreInteractions(userEntityMapper);
        Mockito.verifyNoMoreInteractions(userRepository);
    }

    @DisplayName("Should update a user")
    @Test
    void should_updateAUser() {
        UserDomain userDomain = UserDomain.builder()
                .id(1L)
                .name("Updated Name")
                .build();

        UserEntity currentUserEntity = UserEntity.builder()
                .id(1L)
                .name("Old Name")
                .build();

        UserEntity updatedUserEntity = UserEntity.builder()
                .id(1L)
                .name("Updated Name")
                .build();

        UserDomain updatedUserDomain = UserDomain.builder()
                .id(1L)
                .name("Updated Name")
                .build();

        Mockito.when(userRepository.findById(userDomain.getId())).thenReturn(Optional.of(currentUserEntity));
        Mockito.when(userEntityMapper.fromUserDomainToUserEntityCreateUpdate(userDomain, currentUserEntity)).thenReturn(updatedUserEntity);
        Mockito.when(userRepository.saveAndFlush(updatedUserEntity)).thenReturn(updatedUserEntity);
        Mockito.when(userEntityMapper.fromUserEntityToUserDomain(updatedUserEntity)).thenReturn(updatedUserDomain);

        UserDomain response = service.updateUser(userDomain);

        Assertions.assertNotNull(response);
        Mockito.verify(userRepository, Mockito.times(1)).findById(userDomain.getId());
        Mockito.verify(userEntityMapper, Mockito.times(1)).fromUserDomainToUserEntityCreateUpdate(userDomain, currentUserEntity);
        Mockito.verify(userRepository, Mockito.times(1)).saveAndFlush(updatedUserEntity);
        Mockito.verify(userEntityMapper, Mockito.times(1)).fromUserEntityToUserDomain(updatedUserEntity);
        Mockito.verifyNoMoreInteractions(userEntityMapper);
        Mockito.verifyNoMoreInteractions(userRepository);
    }

    @DisplayName("Should get a user by email")
    @Test
    void should_getAUserByEmail() {
        String userEmail = "test@example.com";

        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .name("Name")
                .email(userEmail)
                .build();

        UserDomain userDomain = UserDomain.builder()
                .id(1L)
                .name("Name")
                .email(userEmail)
                .build();

        Mockito.when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(userEntity));
        Mockito.when(userEntityMapper.fromUserEntityOptionalToUserDomainOptional(Optional.of(userEntity))).thenReturn(Optional.of(userDomain));

        Optional<UserDomain> response = service.getUserByEmail(userEmail);

        Assertions.assertNotNull(response);
        Mockito.verify(userRepository, Mockito.times(1)).findByEmail(userEmail);
        Mockito.verify(userEntityMapper, Mockito.times(2)).fromUserEntityOptionalToUserDomainOptional(Optional.of(userEntity));
        Mockito.verifyNoMoreInteractions(userEntityMapper);
        Mockito.verifyNoMoreInteractions(userRepository);
    }

    @DisplayName("Should get a user by Id")
    @Test
    void should_getAUserById() {
        Long userId = 1L;

        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .name("Name")
                .build();

        UserDomain userDomain = UserDomain.builder()
                .id(1L)
                .name("Name")
                .build();

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        Mockito.when(userEntityMapper.fromUserEntityOptionalToUserDomainOptional(Optional.of(userEntity))).thenReturn(Optional.of(userDomain));

        Optional<UserDomain> response = service.getUserById(userId);

        Assertions.assertNotNull(response);
        Mockito.verify(userRepository, Mockito.times(1)).findById(userId);
        Mockito.verify(userEntityMapper, Mockito.times(2)).fromUserEntityOptionalToUserDomainOptional(Optional.of(userEntity));
        Mockito.verifyNoMoreInteractions(userEntityMapper);
        Mockito.verifyNoMoreInteractions(userRepository);
    }

    @DisplayName("Should update user loyalty points")
    @Test
    void should_updateUserLoyaltyPoints() {
        Long userId = 1L;
        Long loyaltyPoints = 100L;

        Mockito.doNothing().when(userRepository).updateUserLoyaltyPoints(loyaltyPoints, userId);

        service.updateUserLoyaltyPoints(loyaltyPoints, userId);

        Mockito.verify(userRepository, Mockito.times(1)).updateUserLoyaltyPoints(loyaltyPoints, userId);
        Mockito.verifyNoMoreInteractions(userRepository);
    }
}
