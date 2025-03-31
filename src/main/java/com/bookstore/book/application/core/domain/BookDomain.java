package com.bookstore.book.application.core.domain;

import java.time.LocalDateTime;

public class BookDomain {
    private final Long id;
    private final String title;
    private final Float base_price;
    private final int quantity;
    private final BookTypeDomain type;
    private final String author;
    private final String description;
    private final LocalDateTime created_at;
    private final LocalDateTime updated_at;

    public BookDomain(BookDomain.Builder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.base_price = builder.base_price;
        this.quantity = builder.quantity;
        this.type = builder.type;
        this.author = builder.author;
        this.description = builder.description;
        this.created_at = builder.created_at;
        this.updated_at = builder.updated_at;
    }

    public static BookDomain.Builder builder() {
        return new BookDomain.Builder();
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Float getBase_price() {
        return base_price;
    }

    public int getQuantity() {
        return quantity;
    }

    public BookTypeDomain getType() {
        return type;
    }

    public String getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public LocalDateTime getUpdated_at() {
        return updated_at;
    }

    public static class Builder {
        private Long id;
        private String title;
        private Float base_price;
        private int quantity;
        private BookTypeDomain type;
        private String author;
        private String description;
        private LocalDateTime created_at;
        private LocalDateTime updated_at;

        public BookDomain.Builder id(Long id) {
            this.id = id;
            return this;
        }

        public BookDomain.Builder title(String title) {
            this.title = title;
            return this;
        }

        public BookDomain.Builder base_price(Float base_price) {
            this.base_price = base_price;
            return this;
        }

        public BookDomain.Builder quantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public BookDomain.Builder type(BookTypeDomain type) {
            this.type = type;
            return this;
        }

        public BookDomain.Builder author(String author) {
            this.author = author;
            return this;
        }

        public BookDomain.Builder description(String description) {
            this.description = description;
            return this;
        }

        public BookDomain.Builder created_at(LocalDateTime created_at) {
            this.created_at = created_at;
            return this;
        }

        public BookDomain.Builder updated_at(LocalDateTime updated_at) {
            this.updated_at = updated_at;
            return this;
        }

        public BookDomain build() {
            return new BookDomain(this);
        }
    }
}
