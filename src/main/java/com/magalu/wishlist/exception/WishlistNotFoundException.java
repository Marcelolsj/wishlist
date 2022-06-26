package com.magalu.wishlist.exception;

import org.springframework.http.HttpStatus;

public class WishlistNotFoundException extends APIException {

    public WishlistNotFoundException() {
        super("Wishlist not found or is empty", HttpStatus.NOT_FOUND);
    }
}
