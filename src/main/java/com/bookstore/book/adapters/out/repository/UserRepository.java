package com.bookstore.book.adapters.out.repository;

import com.bookstore.book.adapters.out.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {}
