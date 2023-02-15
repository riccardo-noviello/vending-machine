package com.mvp.backend.controller;

import com.mvp.backend.model.Product;
import com.mvp.backend.repository.ProductRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductController productController;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup().setControllerAdvice(new ValidationExceptionHandler()).build();
    }

    @Test
    public void getProductsReturnsListOfProducts() throws Exception {
        Product product1 = Product.builder().productName("Chips").amountAvailable(0).cost(10).sellerId(1L).build();
        Product product2 = Product.builder().productName("Mars Bar").amountAvailable(0).cost(5).sellerId(1L).build();
        productRepository.saveAll(Arrays.asList(product1, product2));

        mockMvc.perform(get("/products").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].productName", is("Product 1")))
                .andExpect(jsonPath("$[0].cost", is(100)))
                .andExpect(jsonPath("$[0].amountAvailable", is(10)))
                .andExpect(jsonPath("$[0].sellerId", is(1)))
                .andExpect(jsonPath("$[1].productName", is("Product 2")))
                .andExpect(jsonPath("$[1].cost", is(200)))
                .andExpect(jsonPath("$[1].amountAvailable", is(20)))
                .andExpect(jsonPath("$[1].sellerId", is(2)));
    }
}