package com.magalu.wishlist.api.resource;

import com.magalu.wishlist.WishlistUtils;
import com.magalu.wishlist.api.DTO.ProductRequestDTO;
import com.magalu.wishlist.api.DTO.WishlistResponseDTO;
import com.magalu.wishlist.model.Product;
import com.magalu.wishlist.model.Wishlist;
import com.magalu.wishlist.service.interfaces.WishlistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class WishlistResourceTest {

    private MockMvc mockMvc;

    @InjectMocks
    private WishlistResource wishlistResource;

    @Mock
    private WishlistService wishlistService;

    private static final String URL_WISHLIST = "/wishlist/products";

    private static final String URL_WISHLIST_PRODUCT = "/wishlist/products/1";

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(wishlistResource).build();
    }

    @Test
    void shouldAddProductAndReturnCreated() throws Exception {
        ProductRequestDTO productRequest = WishlistUtils.createProductRequest(1);
        Product product = WishlistUtils.createProduct(1);

        when(wishlistService.addProduct(any())).thenReturn(product);

        mockMvc.perform(MockMvcRequestBuilders.post(URL_WISHLIST)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(WishlistUtils.toJson(productRequest))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.barCode", is(productRequest.getBarCode())))
                .andExpect(jsonPath("$.description", is(productRequest.getDescription())));

        verify(wishlistService, times(1)).addProduct(any());
    }

    @Test
    void shouldDeleteProductAndReturnNoContent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URL_WISHLIST_PRODUCT)
                        .header("customerEmail", WishlistUtils.CUSTOMER_EMAIL)
                )
                .andExpect(status().isNoContent());

        verify(wishlistService, times(1)).deleteProduct(any(), any());
    }

    @Test
    void shouldReturnAllProductAndReturnOk() throws Exception {
        Wishlist wishlist = WishlistUtils.createWishlist(3);

        when(wishlistService.findAllProducts(WishlistUtils.CUSTOMER_EMAIL)).thenReturn(wishlist);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(URL_WISHLIST)
                        .header("customerEmail", WishlistUtils.CUSTOMER_EMAIL)
                )
                .andExpect(status().isOk())
                .andReturn();

        WishlistResponseDTO wishlistResponseDTO = (WishlistResponseDTO) WishlistUtils.
                fromJson(result.getResponse().getContentAsString(), WishlistResponseDTO.class);

        assertNotNull(wishlistResponseDTO);
        assertEquals(wishlistResponseDTO.getProductList().size(), 3);

        verify(wishlistService, times(1)).findAllProducts(any());
    }

    @Test
    void shouldReturnProductAndReturnOk() throws Exception {
        Product product = WishlistUtils.createProduct(1);

        when(wishlistService.findProduct(product.getBarCode(), WishlistUtils.CUSTOMER_EMAIL)).thenReturn(product);

        mockMvc.perform(MockMvcRequestBuilders.get(URL_WISHLIST_PRODUCT)
                        .header("customerEmail", WishlistUtils.CUSTOMER_EMAIL)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.barCode", is(product.getBarCode())))
                .andExpect(jsonPath("$.description", is(product.getDescription())));

        verify(wishlistService, times(1)).findProduct(product.getBarCode(), WishlistUtils.CUSTOMER_EMAIL);
    }

}
