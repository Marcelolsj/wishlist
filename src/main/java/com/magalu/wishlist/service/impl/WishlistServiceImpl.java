package com.magalu.wishlist.service.impl;

import com.magalu.wishlist.api.DTO.ProductRequestDTO;
import com.magalu.wishlist.exception.*;
import com.magalu.wishlist.model.Customer;
import com.magalu.wishlist.model.Product;
import com.magalu.wishlist.model.Wishlist;
import com.magalu.wishlist.repository.WishlistRepository;
import com.magalu.wishlist.service.interfaces.WishlistService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class WishlistServiceImpl implements WishlistService {

    @Autowired
    private WishlistRepository wishlistRepository;

    @Value("wishlist.limit-products")
    @Setter
    private Integer limitProducts;

    @Transactional
    @Override
    public Product addProduct(ProductRequestDTO request) {
        Wishlist wishlist = findWishlistByCustomer(request.getCustomerEmail());
        if (Objects.isNull(wishlist)) {
            Customer customer = Customer.builder()
                    .email(request.getCustomerEmail())
                    .name(request.getCustomerName())
                    .build();

            wishlist = Wishlist.builder()
                    .customer(customer)
                    .productList(new HashSet<>())
                    .build();
        }

        if (Objects.nonNull(this.limitProducts) && (wishlist.getProductList().size() >= this.limitProducts)) {
            throw new APIException("Product limit has been reached", HttpStatus.BAD_REQUEST);
        }

        Product product = Product.builder()
                .barCode(request.getBarCode())
                .description(request.getDescription())
                .id(UUID.randomUUID())
                .build();

        if (wishlist.getProductList().contains(product)) {
            throw new ProductInWishlistException(request.getBarCode());
        }

        wishlist.getProductList().add(product);
        wishlistRepository.save(wishlist);

        return product;
    }

    @Transactional
    @Override
    public Wishlist deleteProduct(String barCode, String customerEmail) {
        Wishlist wishlist = findWishlistByCustomer(customerEmail);
        if (Objects.isNull(wishlistContainsProduct(wishlist, barCode))) {
            throw new ProductNotFoundException(barCode);
        }

        Product product = Product.builder()
                .barCode(barCode)
                .build();

        wishlist.getProductList().remove(product);
        return wishlistRepository.save(wishlist);
    }

    @Override
    public Wishlist findAllProducts(String customerEmail) {
        Wishlist wishlist = findWishlistByCustomer(customerEmail);
        if (Objects.isNull(wishlist)) {
            throw new WishlistNotFoundException();
        }

        return wishlist;
    }

    @Override
    public Product findProduct(String barCode, String customerEmail) {
        Wishlist wishlist = findWishlistByCustomer(customerEmail);
        if (Objects.isNull(wishlist)) {
            throw new ProductNotFoundException(barCode);
        }

        Product product = wishlistContainsProduct(wishlist, barCode);
        if (Objects.isNull(product)) {
            throw new ProductNotFoundException(barCode);
        }

        return product;
    }

    @Override
    public Wishlist findWishlistByCustomer(String customerEmail) {
        return wishlistRepository.findByCustomerEmail(customerEmail).orElse(null);
    }

    private Product wishlistContainsProduct(Wishlist wishlist, String barCode) {
        if (Objects.isNull(wishlist)) {
            return null;
        }

        List<Product> productList = wishlist.getProductList().stream()
                .filter(p -> p.getBarCode().equals(barCode))
                .collect(Collectors.toList());

        return productList.isEmpty() ? null : productList.get(0);
    }
}
