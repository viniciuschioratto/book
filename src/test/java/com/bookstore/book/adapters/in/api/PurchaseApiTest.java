package com.bookstore.book.adapters.in.api;

import com.bookstore.book.adapters.in.api.mapper.PurchaseRequestMapper;
import com.bookstore.book.adapters.in.api.response.CalculatePriceResponse;
import com.bookstore.book.adapters.in.api.response.PurchaseResponse;
import com.bookstore.book.application.core.domain.CalculatePriceDomain;
import com.bookstore.book.application.core.domain.PurchaseDomain;
import com.bookstore.book.application.ports.in.PurchaseInputPort;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

@WebMvcTest(PurchaseApi.class)
@ExtendWith(SpringExtension.class)
public class PurchaseApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PurchaseInputPort purchaseInputPort;

    @MockitoBean
    private PurchaseRequestMapper purchaseRequestMapper;

    @DisplayName("Should Get Purchase by ID")
    @Test
    void should_getPurchaseById() throws Exception {
        Long purchaseId = 123L;

        PurchaseDomain purchaseDomain = PurchaseDomain.builder()
                .id(purchaseId)
                .build();

        PurchaseResponse purchaseResponse = PurchaseResponse.builder()
                .id(purchaseId)
                .build();

        Mockito.when(purchaseInputPort.getPurchaseById(purchaseId)).thenReturn(purchaseDomain);
        Mockito.when(purchaseRequestMapper.fromPurchaseDomainToPurchaseResponse(purchaseDomain)).thenReturn(purchaseResponse);

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/purchase/v1/{purchaseId}", purchaseId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(purchaseId.intValue())));

        Mockito.verify(purchaseInputPort, Mockito.times(1)).getPurchaseById(purchaseId);
        Mockito.verifyNoMoreInteractions(purchaseInputPort);

        Mockito.verify(purchaseRequestMapper, Mockito.times(1)).fromPurchaseDomainToPurchaseResponse(ArgumentMatchers.any());
        Mockito.verifyNoMoreInteractions(purchaseRequestMapper);
    }

    @DisplayName("Should Calculate Purchase Price")
    @Test
    void should_calculatePurchasePrice() throws Exception {
        String email = "test@example.com";
        List<Long> booksId = List.of(1L, 2L);

        CalculatePriceDomain calculatePriceDomain = new CalculatePriceDomain.DomainBuilder()
                .totalPriceWithDiscount(100.0f)
                .build();

        CalculatePriceResponse calculatePriceResponse = CalculatePriceResponse.builder()
                .totalPriceWithDiscount(100.0f)
                .build();

        Mockito.when(purchaseInputPort.purchaseCalculatePrice(booksId, email)).thenReturn(calculatePriceDomain);
        Mockito.when(purchaseRequestMapper.fromCalculatePriceDomainToCalculatePriceResponse(calculatePriceDomain)).thenReturn(calculatePriceResponse);

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/purchase/v1/calculate-price")
                        .param("email", email)
                        .param("books", "1", "2"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPriceWithDiscount", Matchers.is(100.0)));

        Mockito.verify(purchaseInputPort, Mockito.times(1)).purchaseCalculatePrice(booksId, email);
        Mockito.verifyNoMoreInteractions(purchaseInputPort);

        Mockito.verify(purchaseRequestMapper, Mockito.times(1)).fromCalculatePriceDomainToCalculatePriceResponse(ArgumentMatchers.any());
        Mockito.verifyNoMoreInteractions(purchaseRequestMapper);
    }

    @DisplayName("Should Purchase Books")
    @Test
    void should_purchaseBooks() throws Exception {
        String email = "test@example.com";
        List<Long> booksId = List.of(1L, 2L);

        PurchaseDomain purchaseDomain = PurchaseDomain.builder()
                .id(1L)
                .build();

        PurchaseResponse purchaseResponse = PurchaseResponse.builder()
                .id(1L)
                .build();

        List<PurchaseDomain> purchaseDomainList = List.of(purchaseDomain);
        List<PurchaseResponse> purchaseResponseList = List.of(purchaseResponse);

        Mockito.when(purchaseInputPort.purchaseBooks(booksId, email)).thenReturn(purchaseDomainList);
        Mockito.when(purchaseRequestMapper.fromPurchaseDomainListToPurchaseResponseList(purchaseDomainList)).thenReturn(purchaseResponseList);

        this.mockMvc
                .perform(MockMvcRequestBuilders.put("/purchase/v1/purchase")
                        .param("email", email)
                        .param("books", "1", "2"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)));

        Mockito.verify(purchaseInputPort, Mockito.times(1)).purchaseBooks(booksId, email);
        Mockito.verifyNoMoreInteractions(purchaseInputPort);

        Mockito.verify(purchaseRequestMapper, Mockito.times(1)).fromPurchaseDomainListToPurchaseResponseList(ArgumentMatchers.any());
        Mockito.verifyNoMoreInteractions(purchaseRequestMapper);
    }
}