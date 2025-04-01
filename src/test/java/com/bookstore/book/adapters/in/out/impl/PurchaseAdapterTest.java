package com.bookstore.book.adapters.in.out.impl;

import com.bookstore.book.adapters.out.entity.PurchaseEntity;
import com.bookstore.book.adapters.out.impl.PurchaseAdapter;
import com.bookstore.book.adapters.out.mapper.PurchaseMapper;
import com.bookstore.book.adapters.out.repository.PurchaseRepository;
import com.bookstore.book.application.core.domain.PurchaseDomain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(SpringExtension.class)
public class PurchaseAdapterTest {
    private PurchaseRepository purchaseRepository;
    private PurchaseMapper purchaseMapper;
    private PurchaseAdapter service;

    @BeforeEach
    void setup() {
        purchaseRepository = Mockito.spy(PurchaseRepository.class);
        purchaseMapper = Mockito.spy(PurchaseMapper.class);

        service = new PurchaseAdapter(purchaseRepository, purchaseMapper);
    }

    @DisplayName("Should get a purchase by Id")
    @Test
    void should_getAPurchaseById() {
        Long purchaseId = 1L;

        PurchaseEntity purchaseEntity = PurchaseEntity.builder()
                .id(purchaseId)
                .build();

        PurchaseDomain purchaseDomain = PurchaseDomain.builder()
                .id(purchaseId)
                .build();

        when(purchaseRepository.findById(purchaseId)).thenReturn(Optional.of(purchaseEntity));
        when(purchaseMapper.fromPurchaseEntityOptionalToPurchaseDomainOptional(Optional.of(purchaseEntity))).thenReturn(Optional.of(purchaseDomain));

        Optional<PurchaseDomain> response = service.getPurchaseById(purchaseId);

        assertNotNull(response);
        verify(purchaseRepository, times(1)).findById(purchaseId);
        verify(purchaseMapper, times(2)).fromPurchaseEntityOptionalToPurchaseDomainOptional(Optional.of(purchaseEntity));
        verifyNoMoreInteractions(purchaseRepository);
        verifyNoMoreInteractions(purchaseMapper);
    }

    @DisplayName("Should save a list of purchases")
    @Test
    void should_saveAListOfPurchases() {
        PurchaseDomain purchaseDomain = PurchaseDomain.builder()
                .id(1L)
                .build();

        PurchaseEntity purchaseEntity = PurchaseEntity.builder()
                .id(1L)
                .build();

        List<PurchaseDomain> purchaseDomainList = List.of(purchaseDomain);
        List<PurchaseEntity> purchaseEntityList = List.of(purchaseEntity);

        when(purchaseMapper.fromListPurchaseDomainToListPurchaseEntity(purchaseDomainList)).thenReturn(purchaseEntityList);
        when(purchaseRepository.saveAllAndFlush(purchaseEntityList)).thenReturn(purchaseEntityList);
        when(purchaseMapper.fromListPurchaseEntityToListPurchaseDomain(purchaseEntityList)).thenReturn(purchaseDomainList);

        List<PurchaseDomain> response = service.saveListPurchaseDomain(purchaseDomainList);

        assertNotNull(response);
        verify(purchaseMapper, times(1)).fromListPurchaseDomainToListPurchaseEntity(purchaseDomainList);
        verify(purchaseRepository, times(1)).saveAllAndFlush(purchaseEntityList);
        verify(purchaseMapper, times(1)).fromListPurchaseEntityToListPurchaseDomain(purchaseEntityList);
        verifyNoMoreInteractions(purchaseRepository);
        verifyNoMoreInteractions(purchaseMapper);
    }
}
