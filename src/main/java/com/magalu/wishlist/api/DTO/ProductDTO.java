package com.magalu.wishlist.api.DTO;

import com.magalu.wishlist.model.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

    private String barCode;

    private String description;

    public ProductDTO(Product product) {
        this.barCode = product.getBarCode();
        this.description = product.getDescription();
    }

}
