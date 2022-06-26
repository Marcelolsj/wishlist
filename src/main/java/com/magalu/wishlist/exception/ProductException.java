package com.magalu.wishlist.exception;

import org.springframework.http.HttpStatus;

public class ProductException extends APIException {

    private String barCode;

    public ProductException(String message, String barCode, HttpStatus httpStatus) {
        super(message, httpStatus);
        this.barCode = barCode;
    }
}
