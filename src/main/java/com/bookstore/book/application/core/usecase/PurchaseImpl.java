package com.bookstore.book.application.core.usecase;

import com.bookstore.book.application.core.domain.*;
import com.bookstore.book.application.core.exceptions.BookNotFoundException;
import com.bookstore.book.application.core.exceptions.PurchaseNotFoundException;
import com.bookstore.book.application.core.exceptions.UserNotFoundException;
import com.bookstore.book.application.core.interfaces.BookPriceStrategy;
import com.bookstore.book.application.core.usecase.strategy.BookPriceStrategyFactory;
import com.bookstore.book.application.ports.in.BookCrudInputPort;
import com.bookstore.book.application.ports.in.PurchaseInputPort;
import com.bookstore.book.application.ports.in.UserCrudInputPort;
import com.bookstore.book.application.ports.out.PurchaseOutputPort;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
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
        // Get user
        UserDomain userDomain = userCrudInputPort.getUserByEmail(userEmail);
        // Get books
        List<BookDomain> bookDomains = getBooks(bookIds);

        List<CalculateBookPriceDomain> calculateBookPriceDomains = new ArrayList<>();

        // Get a new List after apply user free book discount
        List<BookDomain> userFreeBook = userFreeBook(userDomain, bookDomains, calculateBookPriceDomains);

        // A map to count the number of books by type
        Map<BookTypeDomain, Long> countByBookTypeDomain = new HashMap<>();
        countByBookTypeDomain.put(BookTypeDomain.NEW_RELEASE, countByBookTypeDomain(bookDomains, BookTypeDomain.NEW_RELEASE));
        countByBookTypeDomain.put(BookTypeDomain.REGULAR, countByBookTypeDomain(bookDomains, BookTypeDomain.REGULAR));
        countByBookTypeDomain.put(BookTypeDomain.OLD_EDITION, countByBookTypeDomain(bookDomains, BookTypeDomain.OLD_EDITION));

        // Build the CalculateBookPriceDomain from the strategy
        for (BookDomain bookDomain : userFreeBook) {
            // Get the strategy by book type
            BookPriceStrategy strategy = BookPriceStrategyFactory.getStrategy(bookDomain.getType());

            // Calculate the price
            CalculateBookPriceDomain calculateBookPriceDomain = strategy.calculatePrice(bookDomain, countByBookTypeDomain.get(bookDomain.getType()));

            // Add to the list
            calculateBookPriceDomains.add(calculateBookPriceDomain);
        }

        // Get Total price without discount
        Float totalPriceWithoutDiscount = sumTotalPriceWithoutDiscount(calculateBookPriceDomains);

        // Get the discount
        Float discount = sumDiscount(calculateBookPriceDomains);

        return new CalculatePriceDomain.DomainBuilder()
                .bookPriceDomains(calculateBookPriceDomains)
                .totalPriceWithoutDiscount(totalPriceWithoutDiscount)
                .hasDiscount(discount > 0)
                .discount(discount)
                .totalPriceWithDiscount(totalPriceWithoutDiscount - discount)
                .build();
    }

    private List<BookDomain> userFreeBook(UserDomain userDomain, List<BookDomain> bookDomains, List<CalculateBookPriceDomain> calculateBookPriceDomains) {
        // If user has less than 10 loyalty points, return the current list of books
        if (userDomain.getLoyalty_points() < 10) {
            return bookDomains;
        }

        // Get the cheapest book from REGULAR and OLD_EDITION
        // This is the free book
        BookDomain bookDiscountFree = getFreeBook(bookDomains);

        // Add free book to list
        calculateBookPriceDomains.add(
                buildCalculateBookPriceDomain(bookDiscountFree, true, bookDiscountFree.getBase_price(), bookDiscountFree.getBase_price())
        );

        // Remove free book from original list
        // Only the first book found will be removed
        AtomicBoolean removed = new AtomicBoolean(false);
        return bookDomains.stream()
                .filter(bookDomain -> {
                    if (!removed.get() && bookDomain.getId().equals(bookDiscountFree.getId())) {
                        removed.set(true);
                        return false;
                    }
                    return true;
                }).toList();
    }

    private BookDomain getFreeBook(List<BookDomain> bookDomains) {
        return bookDomains
                .stream()
                .filter(bookDomain -> bookDomain.getType().equals(BookTypeDomain.REGULAR) || bookDomain.getType().equals(BookTypeDomain.OLD_EDITION))
                .min((book1, book2) -> Float.compare(book1.getBase_price(), book2.getBase_price()))
                .orElse(null);
    }

    private Float sumTotalPriceWithoutDiscount(List<CalculateBookPriceDomain> calculateBookPriceDomains) {
        return calculateBookPriceDomains.stream().reduce(0.0f, (subtotal, element) -> subtotal + element.getTotalPrice(), Float::sum);
    }

    private Float sumDiscount(List<CalculateBookPriceDomain> calculateBookPriceDomains) {
        return calculateBookPriceDomains.stream().reduce(0.0f, (subtotal, element) -> subtotal + element.getDiscount(), Float::sum);
    }

    private long countByBookTypeDomain(List<BookDomain> bookDomains, BookTypeDomain bookTypeDomain) {
        return bookDomains
                .stream()
                .filter(bookDomain -> bookDomain.getType().equals(bookTypeDomain))
                .count();
    }

    private List<BookDomain> getBooks(List<Long> bookIds) {
        List<BookDomain> bookDomains = new ArrayList<>();

        bookIds.forEach(bookId -> {
            try {
                bookDomains.add(bookCrudInputPort.getBookById(bookId));
            } catch (BookNotFoundException e) {
                logger.warning("Book not found with id: " + bookId);
            }
        });

        return bookDomains;
    }

    private CalculateBookPriceDomain buildCalculateBookPriceDomain(BookDomain bookDomain, boolean loyaltyPoints, float totalPrice, float discount) {
        return new CalculateBookPriceDomain.DomainBuilder()
                .book(bookDomain)
                .loyalty_points(loyaltyPoints)
                .totalPrice(totalPrice)
                .discount(discount)
                .build();
    }

    @Override
    public List<PurchaseDomain> purchaseBooks(List<Long> bookIds, String userEmail) throws UserNotFoundException {
        UserDomain userDomain = userCrudInputPort.getUserByEmail(userEmail);
        CalculatePriceDomain calculatePriceDomain = purchaseCalculatePrice(bookIds, userEmail);

        // Create purchase
        List<PurchaseDomain> purchaseDomains = createPurchaseDomain(calculatePriceDomain, userDomain);

        // Save purchase
        List<PurchaseDomain> purchaseDomainSaved = purchaseOutputPort.saveListPurchaseDomain(purchaseDomains);

        // Get current loyalty points
        AtomicLong currentLoyaltyPoints = new AtomicLong(userDomain.getLoyalty_points());

        // Update book quantity
        updateBookQuantity(purchaseDomainSaved, currentLoyaltyPoints);

        // Update user loyalty points
        userCrudInputPort.updateUserLoyaltyPoints(currentLoyaltyPoints.get(), userDomain.getId());

        return purchaseDomainSaved;
    }

    private List<PurchaseDomain> createPurchaseDomain(CalculatePriceDomain calculatePriceDomain, UserDomain userDomain) {
        // Generate a transaction id
        // It is an id to group all purchases
        UUID transactionId = UUID.randomUUID();

        return calculatePriceDomain.getBookPriceDomains()
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
    }

    private void updateBookQuantity(List<PurchaseDomain> purchaseDomainSaved, AtomicLong currentLoyaltyPoints) {
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
    }
}
