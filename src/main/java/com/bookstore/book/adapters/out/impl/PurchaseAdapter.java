package com.bookstore.book.adapters.out.impl;

import com.bookstore.book.adapters.out.entity.PurchaseEntity;
import com.bookstore.book.adapters.out.mapper.PurchaseMapper;
import com.bookstore.book.adapters.out.repository.PurchaseRepository;
import com.bookstore.book.application.core.domain.PurchaseDomain;
import com.bookstore.book.application.ports.out.PurchaseOutputPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class PurchaseAdapter implements PurchaseOutputPort {
    private final PurchaseRepository purchaseRepository;
    private final PurchaseMapper purchaseMapper;

    public PurchaseAdapter(
            PurchaseRepository purchaseRepository,
            PurchaseMapper purchaseMapper
    ) {
        this.purchaseRepository = purchaseRepository;
        this.purchaseMapper = purchaseMapper;
    }

    @Override
    public Optional<PurchaseDomain> getPurchaseById(Long id) {
        return purchaseMapper.fromPurchaseEntityOptionalToPurchaseDomainOptional(
                purchaseRepository.findById(id)
        );
    }

    @Override
    public List<PurchaseDomain> saveListPurchaseDomain(List<PurchaseDomain> purchaseDomainList) {
        List<PurchaseEntity> purchaseEntities = purchaseMapper.fromListPurchaseDomainToListPurchaseEntity(purchaseDomainList);
        return purchaseMapper.fromListPurchaseEntityToListPurchaseDomain(
                purchaseRepository.saveAllAndFlush(purchaseEntities)
        );
    }
}
