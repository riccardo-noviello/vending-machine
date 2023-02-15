package com.mvp.backend.controller;

import com.mvp.backend.model.Product;
import com.mvp.backend.model.Role;
import com.mvp.backend.model.User;
import com.mvp.backend.repository.ProductRepository;
import com.mvp.backend.repository.UserRepository;
import com.mvp.backend.security.AuthorizeRole;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping()
    @AuthorizeRole(role = Role.BUYER)
    public ResponseEntity<List<Product>> getProducts() {
        List<Product> products = productRepository.findAll();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @PostMapping()
    @AuthorizeRole(role = Role.SELLER)
    public ResponseEntity<String> addProduct(@Valid @RequestBody Product product) {
        productRepository.save(product);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{productId}")
    @AuthorizeRole(role = Role.SELLER)
    public ResponseEntity<String> updateProduct(@PathVariable("productId") Long productId, @RequestBody Product product, @RequestHeader("Authorization") String token) {
        Product foundProduct = productRepository.findById(productId).orElse(null);
        Optional<User> user = userRepository.findByToken(token);
        if ((foundProduct == null && user.isPresent()) || !foundProduct.getSellerId().equals(user.get().getId())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        foundProduct.setAmountAvailable(product.getAmountAvailable());
        foundProduct.setCost(product.getCost());
        foundProduct.setProductName(product.getProductName());
        productRepository.save(foundProduct);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{productId}")
    @AuthorizeRole(role = Role.SELLER)
    public ResponseEntity<String> deleteProduct(@PathVariable("productId") Long productId, @RequestHeader("Authorization") String token) {
        Optional<User> user = userRepository.findByToken(token);
        Product foundProduct = productRepository.findById(productId).orElse(null);
        if ((foundProduct == null && user.isPresent()) || !foundProduct.getSellerId().equals(user.get().getId())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
