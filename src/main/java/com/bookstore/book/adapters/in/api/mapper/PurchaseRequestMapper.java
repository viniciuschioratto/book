package com.bookstore.book.adapters.in.api.mapper;

import com.bookstore.book.adapters.in.api.response.CalculatePriceResponse;
import com.bookstore.book.adapters.in.api.response.PurchaseResponse;
import com.bookstore.book.application.core.domain.CalculatePriceDomain;
import com.bookstore.book.application.core.domain.PurchaseDomain;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PurchaseRequestMapper {
    PurchaseResponse fromPurchaseDomainToPurchaseResponse(PurchaseDomain purchaseDomain);
    List<PurchaseResponse> fromPurchaseDomainListToPurchaseResponseList(List<PurchaseDomain> purchaseDomainList);
    CalculatePriceResponse fromCalculatePriceDomainToCalculatePriceResponse(CalculatePriceDomain calculatePriceDomain);
}
