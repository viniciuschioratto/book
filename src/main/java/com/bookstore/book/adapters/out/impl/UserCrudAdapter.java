package com.bookstore.book.adapters.out.impl;

import com.bookstore.book.adapters.out.entity.UserEntity;
import com.bookstore.book.adapters.out.mapper.UserEntityMapper;
import com.bookstore.book.adapters.out.repository.UserRepository;
import com.bookstore.book.application.core.domain.UserDomain;
import com.bookstore.book.application.ports.out.UserCrudOutputPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Component
public class UserCrudAdapter implements UserCrudOutputPort {
    private final UserRepository userRepository;
    private final UserEntityMapper userEntityMapper;

    public UserCrudAdapter(UserRepository userRepository, UserEntityMapper userEntityMapper) {
        this.userRepository = userRepository;
        this.userEntityMapper = userEntityMapper;
    }

    @Override
    public UserDomain createUser(UserDomain userDomain) {
        UserEntity userEntity = userEntityMapper.fromUserDomainToUserEntityCreate(userDomain);
        return userEntityMapper.fromUserEntityToUserDomain(userRepository.saveAndFlush(userEntity));
    }

    @Override
    public UserDomain updateUser(UserDomain userDomain) {
        UserEntity currentUserEntity = userRepository.findById(userDomain.getId()).orElseThrow();
        UserEntity userEntity = userEntityMapper.fromUserDomainToUserEntityCreateUpdate(userDomain, currentUserEntity);
        return userEntityMapper.fromUserEntityToUserDomain(userRepository.saveAndFlush(userEntity));
    }

    @Override
    public Optional<UserDomain> getUserByEmail(String userEmail) {
        return userEntityMapper.fromUserEntityOptionalToUserDomainOptional(userRepository.findByEmail(userEmail));
    }

    @Override
    public Optional<UserDomain> getUserById(Long userId) {
        return userEntityMapper.fromUserEntityOptionalToUserDomainOptional(userRepository.findById(userId));
    }

    @Transactional
    @Override
    public void updateUserLoyaltyPoints(Long loyaltyPoints, Long userId) {
        userRepository.updateUserLoyaltyPoints(loyaltyPoints, userId);
    }
}
