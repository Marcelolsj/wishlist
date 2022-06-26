package com.magalu.wishlist.model;

import lombok.Builder;
import lombok.Data;

import java.util.Objects;
import java.util.UUID;

@Data
@Builder
public class Product {

    private UUID id;

    private String barCode;

    private String description;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return this.barCode.equals(product.getBarCode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBarCode());
    }
}
