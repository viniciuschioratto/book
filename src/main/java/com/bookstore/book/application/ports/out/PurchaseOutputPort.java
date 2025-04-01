package com.bookstore.book.application.ports.out;

import com.bookstore.book.application.core.domain.PurchaseDomain;

import java.util.List;
import java.util.Optional;

public interface PurchaseOutputPort {
    Optional<PurchaseDomain> getPurchaseById(Long id);
    List<PurchaseDomain> saveListPurchaseDomain(List<PurchaseDomain> purchaseDomainList);
}
