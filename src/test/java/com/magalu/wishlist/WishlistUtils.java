package com.magalu.wishlist;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.magalu.wishlist.api.DTO.ProductRequestDTO;
import com.magalu.wishlist.model.Customer;
import com.magalu.wishlist.model.Product;
import com.magalu.wishlist.model.Wishlist;

import java.util.HashSet;
import java.util.UUID;

public class WishlistUtils {

    public static final String CUSTOMER_EMAIL = "joao@email.com";

    public static final String CUSTOMER_NAME = "João";

    public static Wishlist createWishlist(int qtProducts) {
        Wishlist wishlist = Wishlist
                .builder()
                .customer(createCustomer())
                .productList(new HashSet<>())
                .build();

        for(int i = 0; i < qtProducts; i++) {
            wishlist.getProductList().add(createProduct(i + 1));
        }

        return wishlist;
    }

    public static Customer createCustomer() {
        return Customer
                .builder()
                .name(CUSTOMER_NAME)
                .email(CUSTOMER_EMAIL)
                .build();
    }

    public static Product createProduct(Integer index) {
        return Product
                .builder()
                .id(UUID.randomUUID())
                .barCode(index.toString())
                .description("Product " + index.toString())
                .build();
    }

    public static ProductRequestDTO createProductRequest(Integer index) {
        return ProductRequestDTO
                .builder()
                .barCode(index.toString())
                .customerEmail(CUSTOMER_EMAIL)
                .customerName(CUSTOMER_NAME)
                .description("Product " + index.toString())
                .build();
    }

    public static String toJson(Object obj) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(obj);
        }
        catch (JsonGenerationException | JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object fromJson(String json, Class clazz) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, clazz);
        }
        catch (JsonGenerationException | JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
