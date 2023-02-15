package com.mvp.backend.controller;

import com.mvp.backend.model.Product;
import com.mvp.backend.model.Role;
import com.mvp.backend.model.User;
import com.mvp.backend.repository.ProductRepository;
import com.mvp.backend.repository.UserRepository;
import com.mvp.backend.security.TokenProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
public class ProductControllerTest {
    private static final String TOKEN = "Bearer asdasdasd";
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TokenProvider tokenProvider;
    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private ProductController productController;

    @Test
    public void getProductsReturnsListOfProducts() throws Exception {
        when(userRepository.findByToken(TOKEN)).thenReturn(Optional.of(getUser().withRole(Role.BUYER)));
        Product product1 = Product.builder().productName("Chips").amountAvailable(0).cost(10).sellerId(1L).build();
        Product product2 = Product.builder().productName("Mars Bar").amountAvailable(0).cost(5).sellerId(1L).build();
        when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));

        mockMvc.perform(get("/products").contentType(MediaType.APPLICATION_JSON).header("Authorization", TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].productName", is("Chips")))
                .andExpect(jsonPath("$[0].cost", is(10)))
                .andExpect(jsonPath("$[0].amountAvailable", is(0)))
                .andExpect(jsonPath("$[0].sellerId", is(1)))
                .andExpect(jsonPath("$[1].productName", is("Mars Bar")))
                .andExpect(jsonPath("$[1].cost", is(5)))
                .andExpect(jsonPath("$[1].amountAvailable", is(0)))
                .andExpect(jsonPath("$[1].sellerId", is(1)));
    }


    private User getUser() {
        return User.builder().id(1L).username("testuser").password("testpassword").deposit(0).role(Role.BUYER).build();
    }
}