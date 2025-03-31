package com.bookstore.book.application.core.domain;

import jakarta.persistence.Column;

import java.time.LocalDateTime;

public class UserDomain {
    private final Long id;
    private final String name;
    private final String email;
    private final Long loyalty_points;
    private final LocalDateTime created_at;
    private final LocalDateTime updated_at;

    public UserDomain(UserDomain.Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.email = builder.email;
        this.loyalty_points = builder.loyalty_points;
        this.created_at = builder.created_at;
        this.updated_at = builder.updated_at;
    }

    public static UserDomain.Builder builder() {
        return new UserDomain.Builder();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Long getLoyalty_points() {
        return loyalty_points;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public LocalDateTime getUpdated_at() {
        return updated_at;
    }

    public static class Builder {
        private Long id;
        private String name;
        private String email;
        private Long loyalty_points;
        private LocalDateTime created_at;
        private LocalDateTime updated_at;

        public UserDomain.Builder id(Long id) {
            this.id = id;
            return this;
        }

        public UserDomain.Builder name(String name) {
            this.name = name;
            return this;
        }

        public UserDomain.Builder email(String email) {
            this.email = email;
            return this;
        }

        public UserDomain.Builder loyalty_points(Long loyalty_points) {
            this.loyalty_points = loyalty_points;
            return this;
        }

        public UserDomain.Builder created_at(LocalDateTime created_at) {
            this.created_at = created_at;
            return this;
        }

        public UserDomain.Builder updated_at(LocalDateTime updated_at) {
            this.updated_at = updated_at;
            return this;
        }

        public UserDomain build() {
            return new UserDomain(this);
        }
    }
}
