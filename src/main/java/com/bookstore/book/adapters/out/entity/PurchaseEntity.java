package com.bookstore.book.adapters.out.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "purchase_bookstore")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PurchaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "purchase_sequence")
    @SequenceGenerator(name = "purchase_sequence", sequenceName = "purchase_sequence", allocationSize = 1)
    private Long id;

    private int quantity;

    private boolean is_loyalty_points;

    private float price;

    private float final_price;

    private UUID transaction_id;

    @Column(updatable = false)
    private LocalDateTime created_at;

    private LocalDateTime updated_at;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private BookEntity bookEntity;

    @PrePersist
    protected void onInsert() {
        created_at = LocalDateTime.now();
        updated_at = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updated_at = LocalDateTime.now();
    }
}
