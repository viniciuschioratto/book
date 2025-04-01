package com.bookstore.book.application.core.domain;

import java.util.List;

public class CalculatePriceDomain {
    private final List<BookDomain> books;
    private final boolean hasDiscount;
    private final float discount;
    private final float totalPriceWithoutDiscount;
    private final float totalPriceWithDiscount;

    public CalculatePriceDomain(CalculatePriceDomain.DomainBuilder domainBuilder) {
        this.books = domainBuilder.books;
        this.hasDiscount = domainBuilder.hasDiscount;
        this.discount = domainBuilder.discount;
        this.totalPriceWithoutDiscount = domainBuilder.totalPriceWithoutDiscount;
        this.totalPriceWithDiscount = domainBuilder.totalPriceWithDiscount;
    }

    public List<BookDomain> getBooks() {
        return books;
    }

    public boolean isHasDiscount() {
        return hasDiscount;
    }

    public float getDiscount() {
        return discount;
    }

    public float getTotalPriceWithoutDiscount() {
        return totalPriceWithoutDiscount;
    }

    public float getTotalPriceWithDiscount() {
        return totalPriceWithDiscount;
    }

    public static class DomainBuilder {
        private List<BookDomain> books;
        private boolean hasDiscount;
        private float discount;
        private float totalPriceWithoutDiscount;
        private float totalPriceWithDiscount;

        public DomainBuilder books(List<BookDomain> books) {
            this.books = books;
            return this;
        }

        public DomainBuilder hasDiscount(boolean hasDiscount) {
            this.hasDiscount = hasDiscount;
            return this;
        }

        public DomainBuilder discount(float discount) {
            this.discount = discount;
            return this;
        }

        public DomainBuilder totalPriceWithoutDiscount(float totalPriceWithoutDiscount) {
            this.totalPriceWithoutDiscount = totalPriceWithoutDiscount;
            return this;
        }

        public DomainBuilder totalPriceWithDiscount(float totalPriceWithDiscount) {
            this.totalPriceWithDiscount = totalPriceWithDiscount;
            return this;
        }

        public CalculatePriceDomain build() {
            return new CalculatePriceDomain(this);
        }
    }
}
