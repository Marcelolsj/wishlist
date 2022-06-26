package com.magalu.wishlist.service.interfaces;

import com.magalu.wishlist.api.DTO.ProductRequestDTO;
import com.magalu.wishlist.model.Product;
import com.magalu.wishlist.model.Wishlist;

public interface WishlistService {

    Product addProduct(ProductRequestDTO request);

    Wishlist deleteProduct(String barCode, String customerEmail);

    Wishlist findAllProducts(String customerEmail);

    Product findProduct(String barCode, String customerEmail);

    Wishlist findWishlistByCustomer(String customerEmail);
}
