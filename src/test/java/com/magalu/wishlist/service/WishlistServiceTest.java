package com.magalu.wishlist.service;

import com.magalu.wishlist.WishlistUtils;
import com.magalu.wishlist.api.DTO.ProductRequestDTO;
import com.magalu.wishlist.exception.APIException;
import com.magalu.wishlist.exception.ProductInWishlistException;
import com.magalu.wishlist.exception.ProductNotFoundException;
import com.magalu.wishlist.exception.WishlistNotFoundException;
import com.magalu.wishlist.model.Product;
import com.magalu.wishlist.model.Wishlist;
import com.magalu.wishlist.repository.WishlistRepository;
import com.magalu.wishlist.service.impl.WishlistServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.mongodb.assertions.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WishlistServiceTest {

    @Mock
    private WishlistRepository wishlistRepository;

    @InjectMocks
    private WishlistServiceImpl wishListService;

    @BeforeEach
    void startUp() {
        wishListService.setLimitProducts(20);
    }

    @Test
    void shouldAddBookWhenRequestIsValidAndWishlistIsEmpty() {
        ProductRequestDTO productRequestDTO = WishlistUtils.createProductRequest(1);
        Product productExpectedResponse = WishlistUtils.createProduct(1);

        Product product = wishListService.addProduct(productRequestDTO);

        assertNotNull(product);
        assertEquals(product.getBarCode(), productExpectedResponse.getBarCode());
        assertEquals(product.getDescription(), productExpectedResponse.getDescription());

        Mockito.verify(wishlistRepository, times(1)).save(any());
    }

    @Test
    void shouldAddBookWhenRequestIsValidAndWishlistIsNotEmpty() {
        ProductRequestDTO productRequestDTO = WishlistUtils.createProductRequest(2);
        Product productExpectedResponse = WishlistUtils.createProduct(2);
        Wishlist wishlist = WishlistUtils.createWishlist(1);

        when(wishlistRepository.findByCustomerEmail(any())).thenReturn(Optional.of(wishlist));

        Product product = wishListService.addProduct(productRequestDTO);

        assertNotNull(product);
        assertEquals(product.getBarCode(), productExpectedResponse.getBarCode());
        assertEquals(product.getDescription(), productExpectedResponse.getDescription());
        Mockito.verify(wishlistRepository, times(1)).save(any());
    }

    @Test
    void shouldThrowErrorWhenLimitReached() {
        ProductRequestDTO productRequestDTO = WishlistUtils.createProductRequest(21);
        Wishlist wishlist = WishlistUtils.createWishlist(20);

        when(wishlistRepository.findByCustomerEmail(any())).thenReturn(Optional.of(wishlist));

        APIException exception = assertThrows(APIException.class,
                () -> wishListService.addProduct(productRequestDTO)
        );

        assertNotNull(exception);
        assertEquals("Product limit has been reached", exception.getMessage());
        Mockito.verify(wishlistRepository, times(0)).save(any());
    }

    @Test
    void shouldThrowErrorWhenProductIsAlreadyInWishlist() {
        ProductRequestDTO productRequestDTO = WishlistUtils.createProductRequest(1);
        Wishlist wishlist = WishlistUtils.createWishlist(1);

        when(wishlistRepository.findByCustomerEmail(any())).thenReturn(Optional.of(wishlist));

        ProductInWishlistException exception = assertThrows(ProductInWishlistException.class,
                () -> wishListService.addProduct(productRequestDTO)
        );

        assertNotNull(exception);
        assertEquals("Product 1 is already in the wishlist", exception.getMessage());
        Mockito.verify(wishlistRepository, times(0)).save(any());
    }

    @Test
    void shouldDeleteProductFromWishlist() {
        Wishlist wishlist = WishlistUtils.createWishlist(1);
        Wishlist wishlistResponse = WishlistUtils.createWishlist(0);

        when(wishlistRepository.findByCustomerEmail(any())).thenReturn(Optional.of(wishlist));
        when(wishlistRepository.save(any())).thenReturn(wishlistResponse);

        wishlist = wishListService.deleteProduct("1", wishlist.getCustomer().getEmail());

        assertNotNull(wishlist);
        assertEquals(wishlist.getProductList().size(), 0);
        Mockito.verify(wishlistRepository, times(1)).save(any());
    }

    @Test
    void shouldThrowErrorWhenProductNotFoundDelete() {
        Wishlist wishlist = WishlistUtils.createWishlist(1);

        when(wishlistRepository.findByCustomerEmail(any())).thenReturn(Optional.of(wishlist));

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class,
                () -> wishListService.deleteProduct("2", wishlist.getCustomer().getEmail())
        );

        assertNotNull(exception);
        assertEquals("Product 2 not found in the wishlist", exception.getMessage());
        Mockito.verify(wishlistRepository, times(0)).save(any());
    }

    @Test
    void shouldReturnAllProducts() {
        Wishlist wishlist = WishlistUtils.createWishlist(2);

        when(wishlistRepository.findByCustomerEmail(any())).thenReturn(Optional.of(wishlist));

        Wishlist wishlistResponse = wishListService.findAllProducts(wishlist.getCustomer().getEmail());

        assertNotNull(wishlistResponse);
        assertEquals(wishlistResponse.getCustomer().getEmail(), wishlist.getCustomer().getEmail());
        assertEquals(wishlistResponse.getProductList().size(), 2);
    }

    @Test
    void shouldThrowErrorWhenWishlistNotFound() {
        when(wishlistRepository.findByCustomerEmail(any())).thenReturn(Optional.empty());

        WishlistNotFoundException exception = assertThrows(WishlistNotFoundException.class,
                () -> wishListService.findAllProducts(WishlistUtils.CUSTOMER_EMAIL)
        );

        assertNotNull(exception);
        assertEquals("Wishlist not found or is empty", exception.getMessage());
    }

    @Test
    void shouldReturnProduct() {
        Wishlist wishlist = WishlistUtils.createWishlist(3);

        when(wishlistRepository.findByCustomerEmail(any())).thenReturn(Optional.of(wishlist));

        Product product = wishListService.findProduct("2", wishlist.getCustomer().getEmail());

        assertNotNull(product);
        assertEquals(product.getBarCode(), "2");
    }

    @Test
    void shouldThrowErrorWhenProductNotFoundFind() {
        Wishlist wishlist = WishlistUtils.createWishlist(3);

        when(wishlistRepository.findByCustomerEmail(any())).thenReturn(Optional.of(wishlist));

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class,
                () -> wishListService.findProduct("4", wishlist.getCustomer().getEmail())
        );

        assertNotNull(exception);
        assertEquals("Product 4 not found in the wishlist", exception.getMessage());
    }

}
