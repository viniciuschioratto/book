package com.bookstore.book.application.core.usecase;

import com.bookstore.book.application.core.domain.*;
import com.bookstore.book.application.core.exceptions.PurchaseNotFoundException;
import com.bookstore.book.application.core.exceptions.UserNotFoundException;
import com.bookstore.book.application.ports.in.BookCrudInputPort;
import com.bookstore.book.application.ports.in.PurchaseInputPort;
import com.bookstore.book.application.ports.in.UserCrudInputPort;
import com.bookstore.book.application.ports.out.PurchaseOutputPort;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class PurchaseImpl implements PurchaseInputPort {
    private final Logger logger = Logger.getLogger(PurchaseImpl.class.getName());
    private final PurchaseOutputPort purchaseOutputPort;
    private final UserCrudInputPort userCrudInputPort;
    private final BookCrudInputPort bookCrudInputPort;

    public PurchaseImpl(
            PurchaseOutputPort purchaseOutputPort,
            UserCrudInputPort userCrudInputPort,
            BookCrudInputPort bookCrudInputPort
    ) {
        this.purchaseOutputPort = purchaseOutputPort;
        this.userCrudInputPort = userCrudInputPort;
        this.bookCrudInputPort = bookCrudInputPort;
    }

    @Override
    public PurchaseDomain getPurchaseById(Long id) throws PurchaseNotFoundException {
        Optional<PurchaseDomain> purchaseDomain = purchaseOutputPort.getPurchaseById(id);
        return purchaseDomain.orElseThrow(() -> {
            logger.warning("Purchase not found with id: " + id);
            return new PurchaseNotFoundException("Purchase not found with id: " + id);
        });
    }

    @Override
    public CalculatePriceDomain purchaseCalculatePrice(List<Long> bookIds, String userEmail) throws UserNotFoundException {
        UserDomain userDomain = userCrudInputPort.getUserByEmail(userEmail);
        List<BookDomain> bookDomains = bookCrudInputPort.getBooksByIds(bookIds);

        // Calculate total price
        Float totalPriceWithoutDiscount = bookDomains.stream()
                .reduce(0.0f, (subtotal, element) -> subtotal + element.getBase_price(), Float::sum);

        // Filter books by type - REGULAR
        List<BookDomain> regularType = bookDomains
                .stream()
                .filter(bookDomain -> bookDomain.getType().equals(BookTypeDomain.REGULAR))
                .toList();

        // Filter books by type - OLD_EDITION
        List<BookDomain> oldEditionType = bookDomains
                .stream()
                .filter(bookDomain -> bookDomain.getType().equals(BookTypeDomain.OLD_EDITION))
                .toList();

        float discount = 0.0f;

        if (userDomain.getLoyalty_points() >= 10) {
            // Get the cheapest book from REGULAR and OLD_EDITION
            // This is the free book
            BookDomain bookDiscountFree = bookDomains
                    .stream()
                    .filter(bookDomain -> bookDomain.getType().equals(BookTypeDomain.REGULAR) || bookDomain.getType().equals(BookTypeDomain.OLD_EDITION))
                    .min((book1, book2) -> Float.compare(book1.getBase_price(), book2.getBase_price()))
                    .orElse(null);

            if (bookDiscountFree != null) {
                // Remove free book from list
                if (bookDiscountFree.getType().equals(BookTypeDomain.REGULAR)) {
                    regularType = regularType.stream().filter(bookDomain -> !bookDomain.getId().equals(bookDiscountFree.getId())).toList();
                } else {
                    oldEditionType = oldEditionType.stream().filter(bookDomain -> !bookDomain.getId().equals(bookDiscountFree.getId())).toList();
                }

                // Include the value of the free book in the discount
                discount = bookDiscountFree.getBase_price();
            }
        }

        // Calculate the total price of the books by type
        Float regularTypePrice = regularType.stream().reduce(0.0f, (subtotal, element) -> subtotal + element.getBase_price(), Float::sum);
        Float oldEditionTypePrice = oldEditionType.stream().reduce(0.0f, (subtotal, element) -> subtotal + element.getBase_price(), Float::sum);

        // Apply discount for REGULAR type
        if (regularType.size() >= 3) {
            // 10% discount
            discount += regularTypePrice * 0.1f;
        }

        // Apply discount for OLD_EDITION type
        if (!oldEditionType.isEmpty()) {
            if (oldEditionType.size() >= 3) {
                // 25% discount
                discount += oldEditionTypePrice * 0.25f;
            } else {
                // 20% discount
                discount += oldEditionTypePrice * 0.2f;
            }
        }

        return new CalculatePriceDomain.DomainBuilder()
                .books(bookDomains)
                .totalPriceWithoutDiscount(totalPriceWithoutDiscount)
                .hasDiscount(discount > 0)
                .discount(discount)
                .totalPriceWithDiscount(
                        totalPriceWithoutDiscount - discount
                )
                .build();
    }

    @Override
    public List<PurchaseDomain> purchaseBooks(List<Long> bookIds, String userEmail) throws UserNotFoundException {
        UserDomain userDomain = userCrudInputPort.getUserByEmail(userEmail);
        List<BookDomain> bookDomains = bookCrudInputPort.getBooksByIds(bookIds);
        // Need to update the user loyalty points
        // Decrease book quantity

        List<PurchaseDomain> purchaseDomains = bookDomains
                .stream()
                .map(value -> new PurchaseDomain.Builder()
                        .bookEntity(value)
                        .userEntity(userDomain)
                        .build())
                .toList();
        return purchaseOutputPort.saveListPurchaseDomain(purchaseDomains);
    }
}
