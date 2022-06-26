package com.magalu.wishlist.api.DTO;

import com.magalu.wishlist.model.Wishlist;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WishlistResponseDTO {

    private String customer;

    private List<ProductDTO> productList;

    public WishlistResponseDTO(Wishlist wishlist) {
        this.customer = wishlist.getCustomer().getEmail();
        if (Objects.nonNull(wishlist.getProductList())) {
            this.productList = wishlist.getProductList().stream()
                    .map(ProductDTO::new)
                    .collect(Collectors.toList());
        }
    }

}
