package com.magalu.wishlist.api.resource;

import com.magalu.wishlist.api.DTO.ProductDTO;
import com.magalu.wishlist.api.DTO.ProductRequestDTO;
import com.magalu.wishlist.api.DTO.WishlistResponseDTO;
import com.magalu.wishlist.service.interfaces.WishlistService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/wishlist")
@Slf4j
public class WishlistResource {

    @Autowired
    private WishlistService wishlistService;

    @PostMapping("/products")
    @ApiOperation(value = "Add a product to wishlist")
    public ResponseEntity<ProductDTO> addProduct(@Valid @RequestBody ProductRequestDTO request) {
        log.info("Add a product to wishlist | request={}", request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ProductDTO(wishlistService.addProduct(request)));
    }

    @DeleteMapping("/products/{barCode}")
    @ApiOperation(value = "Delete product from wishlist")
    public ResponseEntity<?> deleteProduct(@PathVariable String barCode,
                                        @RequestHeader String customerEmail) {
        log.info("Delete product from wishlist | barCode={}", barCode);
        wishlistService.deleteProduct(barCode, customerEmail);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/products")
    @ApiOperation(value = "Get all products from wishlist")
    public ResponseEntity<WishlistResponseDTO> getProducts(@RequestHeader String customerEmail) {
        log.info("Get all products from wishlist | customerEmail={}", customerEmail);
        return ResponseEntity.ok().body(new WishlistResponseDTO(wishlistService.findAllProducts(customerEmail)));
    }

    @GetMapping("/products/{barCode}")
    @ApiOperation(value = "Get product from wishlist")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable String barCode,
                                                 @RequestHeader String customerEmail) {
        log.info("Get product from wishlist | barCode={} customerEmail={}", barCode, customerEmail);
        return ResponseEntity.ok().body(new ProductDTO(wishlistService.findProduct(barCode, customerEmail)));
    }

}
