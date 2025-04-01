package com.bookstore.book.adapters.out.repository;

import com.bookstore.book.adapters.out.entity.PurchaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<PurchaseEntity, Long> {}
