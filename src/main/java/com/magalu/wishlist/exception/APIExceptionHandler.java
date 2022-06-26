package com.magalu.wishlist.exception;

import com.magalu.wishlist.api.DTO.ErrorDetailResponseDTO;
import com.magalu.wishlist.api.DTO.ErrorMessageResponseDTO;
import com.magalu.wishlist.api.DTO.ErrorResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class APIExceptionHandler {

    @ExceptionHandler(APIException.class)
    public ResponseEntity<ErrorResponseDTO> businessHandler(APIException ex) {
        log.info(ex.getMessage());
        return ResponseEntity.status(ex.getHttpStatus()).body(new ErrorResponseDTO(ex.getMessage()));
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorMessageResponseDTO> validationHandler(BindException ex) {
        List<ErrorDetailResponseDTO> detailList = ex.getFieldErrors().stream().map(ErrorDetailResponseDTO::new).collect(Collectors.toList());
        log.info("Validation error={}", detailList);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessageResponseDTO(detailList));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponseDTO> genericExceptionHandler(RuntimeException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseDTO("Internal server error"));
    }

}
