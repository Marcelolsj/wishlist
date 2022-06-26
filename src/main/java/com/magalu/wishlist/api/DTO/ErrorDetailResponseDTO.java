package com.magalu.wishlist.api.DTO;

import lombok.Data;
import org.springframework.validation.FieldError;

@Data
public class ErrorDetailResponseDTO {

    private String parameter;
    private String message;

    public ErrorDetailResponseDTO(FieldError fieldError) {
        this.parameter = fieldError.getField();
        this.message = fieldError.getDefaultMessage();
    }

}
