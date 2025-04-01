package com.bookstore.book.application.core.domain;

public class CalculateBookPriceDomain {
    private final BookDomain book;
    private final float totalPrice;
    private final float discount;
    private final boolean loyalty_points;

    public CalculateBookPriceDomain(CalculateBookPriceDomain.DomainBuilder domainBuilder) {
        this.book = domainBuilder.book;
        this.totalPrice = domainBuilder.totalPrice;
        this.discount = domainBuilder.discount;
        this.loyalty_points = domainBuilder.loyalty_points;
    }

    public BookDomain getBook() {
        return book;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public float getDiscount() {
        return discount;
    }

    public boolean getLoyalty_points() {
        return loyalty_points;
    }

    public static class DomainBuilder {
        private BookDomain book;
        private float totalPrice;
        private float discount;
        private boolean loyalty_points;

        public DomainBuilder book(BookDomain book) {
            this.book = book;
            return this;
        }

        public DomainBuilder totalPrice(float totalPrice) {
            this.totalPrice = totalPrice;
            return this;
        }

        public DomainBuilder discount(float discount) {
            this.discount = discount;
            return this;
        }

        public DomainBuilder loyalty_points(boolean loyalty_points) {
            this.loyalty_points = loyalty_points;
            return this;
        }

        public CalculateBookPriceDomain build() {
            return new CalculateBookPriceDomain(this);
        }
    }
}
