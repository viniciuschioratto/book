package com.bookstore.book.application.core.exceptions;

public class PurchaseNotFoundException extends Exception {
    public PurchaseNotFoundException(String message) {
        super(message);
    }
}
