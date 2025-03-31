package com.bookstore.book.adapters.out.mapper;

import com.bookstore.book.adapters.out.entity.UserEntity;
import com.bookstore.book.application.core.domain.UserDomain;
import org.mapstruct.Mapper;

import java.util.Optional;

@Mapper(componentModel = "spring")
public interface UserEntityMapper {
    UserEntity fromUserDomainToUserEntity(UserDomain userDomain);
    default Optional<UserDomain> fromUserEntityOptionalToUserDomainOptional(Optional<UserEntity> userEntityOptional) {
        return userEntityOptional.map(this::fromUserEntityToUserDomain);
    }
    UserDomain fromUserEntityToUserDomain(UserEntity userEntity);
}
