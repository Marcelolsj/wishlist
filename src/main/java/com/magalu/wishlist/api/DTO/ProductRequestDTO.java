package com.magalu.wishlist.api.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDTO {

    @NotBlank(message = "Must have a value")
    private String barCode;

    @NotBlank(message = "Must have a value")
    private String description;

    @NotBlank
    @Pattern(regexp = "^(.+)@(.+)$", message = "Must be a valid email")
    private String customerEmail;

    @NotBlank(message = "Must have a value")
    private String customerName;

}
