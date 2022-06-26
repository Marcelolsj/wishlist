package com.magalu.wishlist.exception;

import org.springframework.http.HttpStatus;

public class ProductNotFoundException extends ProductException {

    public ProductNotFoundException(String barCode) {
        super(String.format("Product %s not found in the wishlist", barCode), barCode, HttpStatus.NOT_FOUND);
    }

}
