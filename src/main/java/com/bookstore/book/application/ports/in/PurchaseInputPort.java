package com.bookstore.book.application.ports.in;

import com.bookstore.book.application.core.domain.CalculatePriceDomain;
import com.bookstore.book.application.core.domain.PurchaseDomain;
import com.bookstore.book.application.core.exceptions.PurchaseNotFoundException;
import com.bookstore.book.application.core.exceptions.UserNotFoundException;

import java.util.List;

public interface PurchaseInputPort {
    PurchaseDomain getPurchaseById(Long id) throws PurchaseNotFoundException;
    CalculatePriceDomain purchaseCalculatePrice(List<Long> bookIds, String userEmail) throws UserNotFoundException;
    List<PurchaseDomain> purchaseBooks(List<Long> bookIds, String userEmail) throws UserNotFoundException;
}
