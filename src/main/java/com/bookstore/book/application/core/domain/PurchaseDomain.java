package com.bookstore.book.application.core.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public class PurchaseDomain {
    private final Long id;
    private final int quantity;
    private final boolean loyalty_points;
    private final float price;
    private final float final_price;
    private final UUID transaction_id;
    private final LocalDateTime created_at;
    private final LocalDateTime updated_at;
    private final UserDomain userEntity;
    private final BookDomain bookEntity;

    public PurchaseDomain(PurchaseDomain.Builder builder) {
        this.id = builder.id;
        this.quantity = builder.quantity;
        this.loyalty_points = builder.loyalty_points;
        this.price = builder.price;
        this.final_price = builder.final_price;
        this.transaction_id = builder.transaction_id;
        this.created_at = builder.created_at;
        this.updated_at = builder.updated_at;
        this.userEntity = builder.userEntity;
        this.bookEntity = builder.bookEntity;
    }

    public static PurchaseDomain.Builder builder() {
        return new PurchaseDomain.Builder();
    }

    public Long getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean getloyalty_points() {
        return loyalty_points;
    }

    public float getPrice() {
        return price;
    }

    public float getFinal_price() {
        return final_price;
    }

    public UUID getTransaction_id() {
        return transaction_id;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public LocalDateTime getUpdated_at() {
        return updated_at;
    }

    public UserDomain getUserEntity() {
        return userEntity;
    }

    public BookDomain getBookEntity() {
        return bookEntity;
    }

    public static class Builder {
        private Long id;
        private int quantity;
        private boolean loyalty_points;
        private float price;
        private float final_price;
        private UUID transaction_id;
        private LocalDateTime created_at;
        private LocalDateTime updated_at;
        private UserDomain userEntity;
        private BookDomain bookEntity;

        public PurchaseDomain.Builder id(Long id) {
            this.id = id;
            return this;
        }

        public PurchaseDomain.Builder quantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public PurchaseDomain.Builder loyalty_points(boolean loyalty_points) {
            this.loyalty_points = loyalty_points;
            return this;
        }

        public PurchaseDomain.Builder price(float price) {
            this.price = price;
            return this;
        }

        public PurchaseDomain.Builder final_price(float final_price) {
            this.final_price = final_price;
            return this;
        }

        public PurchaseDomain.Builder transaction_id(UUID transaction_id) {
            this.transaction_id = transaction_id;
            return this;
        }

        public PurchaseDomain.Builder created_at(LocalDateTime created_at) {
            this.created_at = created_at;
            return this;
        }

        public PurchaseDomain.Builder updated_at(LocalDateTime updated_at) {
            this.updated_at = updated_at;
            return this;
        }

        public PurchaseDomain.Builder userEntity(UserDomain userEntity) {
            this.userEntity = userEntity;
            return this;
        }

        public PurchaseDomain.Builder bookEntity(BookDomain bookEntity) {
            this.bookEntity = bookEntity;
            return this;
        }

        public PurchaseDomain build() {
            return new PurchaseDomain(this);
        }
    }
}
