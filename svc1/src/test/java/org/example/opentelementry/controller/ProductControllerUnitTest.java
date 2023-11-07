package org.example.opentelementry.controller;

import org.example.opentelementry.api.client.PriceClient;
import org.example.opentelementry.exception.ProductNotFoundException;
import org.example.opentelementry.model.Price;
import org.example.opentelementry.model.Product;
import org.example.opentelementry.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpServerErrorException;


import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@WebMvcTest(ProductController.class)
class ProductControllerUnitTest {

    @MockBean
    private PriceClient priceCLient;

    @MockBean
    private ProductRepository productRepository;

    @Autowired
    private MockMvc mockMvc;


    @Test
    void givenProductandPriceDataAvailable_whenGetProductCalled_thenReturnProductDetails() throws Exception {
        long productId = 100000L;

        Price price = createPrice(productId);
        Product product = createProduct(productId);
        product.setPrice(price);

        when(productRepository.getProduct(productId)).thenReturn(product);
        when(priceCLient.getPrice(productId)).thenReturn(price);

        mockMvc.perform(get("/product/" + productId))
            .andExpect(status().is(HttpStatus.OK.value()));
    }

    @Test
    void givenProductNotFound_whenGetProductCalled_thenReturnInternalServerError() throws Exception {
        long productId = 100000L;
        Price price = createPrice(productId);

        when(productRepository.getProduct(productId)).thenThrow(ProductNotFoundException.class);
        when(priceCLient.getPrice(productId)).thenReturn(price);

        mockMvc.perform(get("/product/" + productId))
            .andExpect(status().is(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    void givenPriceServiceNotAvailable_whenGetProductCalled_thenReturnInternalServerError() throws Exception {
        long productId = 100000L;
        Product product = createProduct(productId);

        when(productRepository.getProduct(productId)).thenReturn(product);
        when(priceCLient.getPrice(productId)).thenThrow(HttpServerErrorException.ServiceUnavailable.class);

        mockMvc.perform(get("/product/" + productId))
                .andExpect(status().is(HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }

    private static Product createProduct(long productId) {
        Product product = new Product();
        product.setId(productId);
        product.setName("test");
        return product;
    }

    private static Price createPrice(long productId) {
        Price price = new Price();
        price.setProductId(productId);
        price.setPriceAmount(12.00);
        price.setDiscount(2.5);
        return price;
    }
}
