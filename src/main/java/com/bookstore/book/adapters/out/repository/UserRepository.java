package com.bookstore.book.adapters.out.repository;

import com.bookstore.book.adapters.out.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);

    @Modifying
    @Query(value = "UPDATE user_bookstore SET loyalty_points = :loyaltyPoints WHERE id = :userId", nativeQuery = true)
    void updateUserLoyaltyPoints(Long loyaltyPoints, Long userId);
}
