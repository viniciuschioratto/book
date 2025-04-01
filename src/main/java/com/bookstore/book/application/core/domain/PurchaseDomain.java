package com.bookstore.book.application.core.domain;

import java.time.LocalDateTime;

public class PurchaseDomain {
    private final Long id;
    private final LocalDateTime created_at;
    private final LocalDateTime updated_at;
    private final UserDomain userEntity;
    private final BookDomain bookEntity;

    public PurchaseDomain(PurchaseDomain.Builder builder) {
        this.id = builder.id;
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
        private LocalDateTime created_at;
        private LocalDateTime updated_at;
        private UserDomain userEntity;
        private BookDomain bookEntity;

        public PurchaseDomain.Builder id(Long id) {
            this.id = id;
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
