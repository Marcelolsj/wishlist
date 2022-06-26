package com.magalu.wishlist.exception;

import org.springframework.http.HttpStatus;

public class ProductInWishlistException extends ProductException {

    public ProductInWishlistException(String barCode) {
        super(String.format("Product %s is already in the wishlist", barCode), barCode, HttpStatus.BAD_REQUEST);
    }

}
