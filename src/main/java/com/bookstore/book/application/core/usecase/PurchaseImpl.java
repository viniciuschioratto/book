package com.bookstore.book.application.core.usecase;

import com.bookstore.book.application.core.domain.*;
import com.bookstore.book.application.core.exceptions.BookNotFoundException;
import com.bookstore.book.application.core.exceptions.PurchaseNotFoundException;
import com.bookstore.book.application.core.exceptions.UserNotFoundException;
import com.bookstore.book.application.ports.in.BookCrudInputPort;
import com.bookstore.book.application.ports.in.PurchaseInputPort;
import com.bookstore.book.application.ports.in.UserCrudInputPort;
import com.bookstore.book.application.ports.out.PurchaseOutputPort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
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
        //List<BookDomain> bookDomains = bookCrudInputPort.getBooksByIds(bookIds);
        List<BookDomain> bookDomains = new ArrayList<>();

        bookIds.forEach(bookId -> {
            try {
                bookDomains.add(bookCrudInputPort.getBookById(bookId));
            } catch (BookNotFoundException e) {
                logger.warning("Book not found with id: " + bookId);
            }
        });

        List<CalculateBookPriceDomain> calculateBookPriceDomains = new ArrayList<>();

        bookDomains.stream()
                .filter(bookDomain -> bookDomain.getType().equals(BookTypeDomain.NEW_RELEASE))
                .forEach(bookDomain -> {
                    calculateBookPriceDomains.add(
                            new CalculateBookPriceDomain.DomainBuilder()
                                    .book(bookDomain)
                                    .loyalty_points(false)
                                    .totalPrice(bookDomain.getBase_price())
                                    .discount(0.0f)
                                    .build()
                    );
                });

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

                calculateBookPriceDomains.add(
                        new CalculateBookPriceDomain.DomainBuilder()
                                .book(bookDiscountFree)
                                .loyalty_points(true)
                                .totalPrice(bookDiscountFree.getBase_price())
                                .discount(bookDiscountFree.getBase_price())
                                .build()
                );
            }
        }

        // Calculate the total price of the books by type
        Float regularTypePrice = regularType.stream().reduce(0.0f, (subtotal, element) -> subtotal + element.getBase_price(), Float::sum);
        Float oldEditionTypePrice = oldEditionType.stream().reduce(0.0f, (subtotal, element) -> subtotal + element.getBase_price(), Float::sum);

        // Apply discount for REGULAR type
        if (regularType.size() >= 3) {
            // 10% discount
            discount += regularTypePrice * 0.1f;

            regularType.forEach(bookDomain -> {
                calculateBookPriceDomains.add(
                        new CalculateBookPriceDomain.DomainBuilder()
                                .book(bookDomain)
                                .loyalty_points(false)
                                .totalPrice(bookDomain.getBase_price())
                                .discount(bookDomain.getBase_price() * 0.1f)
                                .build()
                );
            });
        } else {
            regularType.forEach(bookDomain -> {
                calculateBookPriceDomains.add(
                        new CalculateBookPriceDomain.DomainBuilder()
                                .book(bookDomain)
                                .loyalty_points(false)
                                .totalPrice(bookDomain.getBase_price())
                                .discount(0.0f)
                                .build()
                );
            });
        }

        // Apply discount for OLD_EDITION type
        if (!oldEditionType.isEmpty()) {
            if (oldEditionType.size() >= 3) {
                // 25% discount
                discount += oldEditionTypePrice * 0.25f;

                oldEditionType.forEach(bookDomain -> {
                    calculateBookPriceDomains.add(
                            new CalculateBookPriceDomain.DomainBuilder()
                                    .book(bookDomain)
                                    .loyalty_points(false)
                                    .totalPrice(bookDomain.getBase_price())
                                    .discount(bookDomain.getBase_price() * 0.25f)
                                    .build()
                    );
                });

            } else {
                // 20% discount
                discount += oldEditionTypePrice * 0.2f;

                oldEditionType.forEach(bookDomain -> {
                    calculateBookPriceDomains.add(
                            new CalculateBookPriceDomain.DomainBuilder()
                                    .book(bookDomain)
                                    .loyalty_points(false)
                                    .totalPrice(bookDomain.getBase_price())
                                    .discount(bookDomain.getBase_price() * 0.2f)
                                    .build()
                    );
                });

            }
        }

        return new CalculatePriceDomain.DomainBuilder()
                .bookPriceDomains(calculateBookPriceDomains)
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
        CalculatePriceDomain calculatePriceDomain = purchaseCalculatePrice(bookIds, userEmail);

        UUID transactionId = UUID.randomUUID();

        List<PurchaseDomain> purchaseDomains = calculatePriceDomain.getBookPriceDomains()
                .stream()
                .map(value -> new PurchaseDomain.Builder()
                        .quantity(1)
                        .transaction_id(transactionId)
                        .loyalty_points(value.getLoyalty_points())
                        .price(value.getTotalPrice())
                        .final_price(value.getTotalPrice() - value.getDiscount())
                        .bookEntity(value.getBook())
                        .userEntity(userDomain)
                        .build())
                .toList();

        List<PurchaseDomain> purchaseDomainSaved = purchaseOutputPort.saveListPurchaseDomain(purchaseDomains);

        //Long currentLoyaltyPoints = userDomain.getLoyalty_points();
        AtomicLong currentLoyaltyPoints = new AtomicLong(userDomain.getLoyalty_points());

        // Update book quantity
        purchaseDomainSaved.forEach(purchaseDomain -> {
            bookCrudInputPort.updateBookQuantity(
                    purchaseDomain.getBookEntity().getQuantity() - 1,
                    purchaseDomain.getBookEntity().getId()
            );

            if (purchaseDomain.getloyalty_points()) {
                // Remove 10 loyalty points
                // The book bought with loyalty points does not add points
                currentLoyaltyPoints.addAndGet(-10L);
            } else {
                currentLoyaltyPoints.incrementAndGet();
            }
        });

        // Update user loyalty points
        userCrudInputPort.updateUserLoyaltyPoints(currentLoyaltyPoints.get(), userDomain.getId());

        return purchaseDomainSaved;
    }
}
