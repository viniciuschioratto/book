package com.bookstore.book.adapters.out.mapper;

import com.bookstore.book.adapters.out.entity.PurchaseEntity;
import com.bookstore.book.application.core.domain.PurchaseDomain;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring")
public interface PurchaseMapper {
    default Optional<PurchaseDomain> fromPurchaseEntityOptionalToPurchaseDomainOptional(Optional<PurchaseEntity> purchaseEntity) {
        return purchaseEntity.map(this::fromPurchaseEntityToPurchaseDomain);
    }
    PurchaseDomain fromPurchaseEntityToPurchaseDomain(PurchaseEntity purchaseEntity);
    List<PurchaseEntity> fromListPurchaseDomainToListPurchaseEntity(List<PurchaseDomain> purchaseDomainList);
    List<PurchaseDomain> fromListPurchaseEntityToListPurchaseDomain(List<PurchaseEntity> purchaseEntityList);
}
