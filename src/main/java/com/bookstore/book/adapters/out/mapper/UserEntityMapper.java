package com.bookstore.book.adapters.out.mapper;

import com.bookstore.book.adapters.out.entity.UserEntity;
import com.bookstore.book.application.core.domain.UserDomain;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Optional;

@Mapper(componentModel = "spring")
public interface UserEntityMapper {
    @Mapping(target = "loyalty_points", defaultValue = "0L")
    UserEntity fromUserDomainToUserEntityCreate(UserDomain userDomain);

    @Mapping(target = "loyalty_points", ignore = true)
    UserEntity fromUserDomainToUserEntityCreateUpdate(UserDomain userDomain, @MappingTarget UserEntity userEntityTarget);

    UserEntity fromUserDomainToUserEntity(UserDomain userDomain);
    default Optional<UserDomain> fromUserEntityOptionalToUserDomainOptional(Optional<UserEntity> userEntityOptional) {
        return userEntityOptional.map(this::fromUserEntityToUserDomain);
    }
    UserDomain fromUserEntityToUserDomain(UserEntity userEntity);
}
