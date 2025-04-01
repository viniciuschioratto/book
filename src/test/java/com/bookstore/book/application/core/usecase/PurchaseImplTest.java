package com.bookstore.book.application.core.usecase;

import com.bookstore.book.application.core.domain.*;
import com.bookstore.book.application.core.exceptions.BookNotFoundException;
import com.bookstore.book.application.core.exceptions.PurchaseNotFoundException;
import com.bookstore.book.application.core.exceptions.UserNotFoundException;
import com.bookstore.book.application.ports.in.BookCrudInputPort;
import com.bookstore.book.application.ports.in.UserCrudInputPort;
import com.bookstore.book.application.ports.out.PurchaseOutputPort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(SpringExtension.class)
public class PurchaseImplTest {
    private PurchaseOutputPort purchaseOutputPort;
    private UserCrudInputPort userCrudInputPort;
    private BookCrudInputPort bookCrudInputPort;
    private PurchaseImpl service;

    @BeforeEach
    void setup() {
        purchaseOutputPort = Mockito.spy(PurchaseOutputPort.class);
        userCrudInputPort = Mockito.spy(UserCrudInputPort.class);
        bookCrudInputPort = Mockito.spy(BookCrudInputPort.class);

        service = new PurchaseImpl(purchaseOutputPort, userCrudInputPort, bookCrudInputPort);
    }

    @DisplayName("Should get a purchase by id")
    @Test
    void should_getPurchaseById() throws PurchaseNotFoundException {
        Long purchaseId = 1L;
        PurchaseDomain purchaseDomain = new PurchaseDomain.Builder().id(purchaseId).build();

        Mockito.when(purchaseOutputPort.getPurchaseById(purchaseId)).thenReturn(Optional.of(purchaseDomain));

        PurchaseDomain response = service.getPurchaseById(purchaseId);

        Assertions.assertNotNull(response);
        Mockito.verify(purchaseOutputPort, Mockito.times(1)).getPurchaseById(purchaseId);
        Mockito.verifyNoMoreInteractions(purchaseOutputPort);
    }

    @DisplayName("Should throw exception when purchase not found by id")
    @Test
    void should_throwExceptionWhenPurchaseNotFoundById() {
        Long purchaseId = 1L;

        Mockito.when(purchaseOutputPort.getPurchaseById(purchaseId)).thenReturn(Optional.empty());

        Assertions.assertThrows(PurchaseNotFoundException.class, () -> service.getPurchaseById(purchaseId));

        Mockito.verify(purchaseOutputPort, Mockito.times(1)).getPurchaseById(purchaseId);
        Mockito.verifyNoMoreInteractions(purchaseOutputPort);
    }

    @DisplayName("Should calculate purchase price")
    @Test
    void should_calculatePurchasePrice() throws UserNotFoundException, BookNotFoundException {
        String userEmail = "user@example.com";
        Long bookId = 1L;
        List<Long> bookIds = List.of(bookId);

        UserDomain userDomain = UserDomain.builder().email(userEmail).loyalty_points(10L).build();
        BookDomain bookDomain = BookDomain.builder().id(bookId).base_price(10.0f).type(BookTypeDomain.REGULAR).build();

        Mockito.when(userCrudInputPort.getUserByEmail(userEmail)).thenReturn(userDomain);
        Mockito.when(bookCrudInputPort.getBookById(bookId)).thenReturn(bookDomain);

        CalculatePriceDomain response = service.purchaseCalculatePrice(bookIds, userEmail);

        Assertions.assertNotNull(response);
        Mockito.verify(userCrudInputPort, Mockito.times(1)).getUserByEmail(userEmail);
        Mockito.verify(bookCrudInputPort, Mockito.times(1)).getBookById(bookId);
        Mockito.verifyNoMoreInteractions(userCrudInputPort);
        Mockito.verifyNoMoreInteractions(bookCrudInputPort);
    }

    @DisplayName("Should throw exception when user not found for purchase price calculation")
    @Test
    void should_throwExceptionWhenUserNotFoundForPurchasePriceCalculation() throws UserNotFoundException {
        String userEmail = "user@example.com";
        List<Long> bookIds = List.of(1L);

        Mockito.when(userCrudInputPort.getUserByEmail(userEmail)).thenThrow(new UserNotFoundException("User not found"));

        Assertions.assertThrows(UserNotFoundException.class, () -> service.purchaseCalculatePrice(bookIds, userEmail));

        Mockito.verify(userCrudInputPort, Mockito.times(1)).getUserByEmail(userEmail);
        Mockito.verifyNoMoreInteractions(userCrudInputPort);
    }

    @DisplayName("Should purchase books")
    @Test
    void should_purchaseBooks() throws UserNotFoundException, BookNotFoundException {
        String userEmail = "user@example.com";
        Long bookId = 1L;
        List<Long> bookIds = List.of(bookId);

        UserDomain userDomain = UserDomain.builder().email(userEmail).loyalty_points(10L).build();
        BookDomain bookDomain = BookDomain.builder().id(bookId).base_price(10.0f).type(BookTypeDomain.REGULAR).quantity(5).build();
        PurchaseDomain purchaseDomain = new PurchaseDomain.Builder().id(1L).transaction_id(UUID.randomUUID()).build();

        Mockito.when(userCrudInputPort.getUserByEmail(userEmail)).thenReturn(userDomain);
        Mockito.when(bookCrudInputPort.getBookById(bookId)).thenReturn(bookDomain);
        Mockito.when(purchaseOutputPort.saveListPurchaseDomain(Mockito.anyList())).thenReturn(List.of(purchaseDomain));

        List<PurchaseDomain> response = service.purchaseBooks(bookIds, userEmail);

        Assertions.assertNotNull(response);
        Mockito.verify(userCrudInputPort, Mockito.times(1)).getUserByEmail(userEmail);
        Mockito.verify(bookCrudInputPort, Mockito.times(1)).getBookById(bookId);
        Mockito.verify(purchaseOutputPort, Mockito.times(1)).saveListPurchaseDomain(Mockito.anyList());
        Mockito.verifyNoMoreInteractions(userCrudInputPort);
        Mockito.verifyNoMoreInteractions(bookCrudInputPort);
        Mockito.verifyNoMoreInteractions(purchaseOutputPort);
    }

    @DisplayName("Should throw exception when user not found for purchasing books")
    @Test
    void should_throwExceptionWhenUserNotFoundForPurchasingBooks() throws UserNotFoundException {
        String userEmail = "user@example.com";
        List<Long> bookIds = List.of(1L);

        Mockito.when(userCrudInputPort.getUserByEmail(userEmail)).thenThrow(new UserNotFoundException("User not found"));

        Assertions.assertThrows(UserNotFoundException.class, () -> service.purchaseBooks(bookIds, userEmail));

        Mockito.verify(userCrudInputPort, Mockito.times(1)).getUserByEmail(userEmail);
        Mockito.verifyNoMoreInteractions(userCrudInputPort);
    }
}
