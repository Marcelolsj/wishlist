package com.magalu.wishlist.api.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ErrorMessageResponseDTO {

    private List<ErrorDetailResponseDTO> errors;

}
