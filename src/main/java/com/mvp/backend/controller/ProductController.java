package com.mvp.backend.controller;

import com.mvp.backend.controller.request.ProductRequest;
import com.mvp.backend.model.Product;
import com.mvp.backend.model.Role;
import com.mvp.backend.model.User;
import com.mvp.backend.repository.ProductRepository;
import com.mvp.backend.repository.UserRepository;
import com.mvp.backend.security.AuthorizeRole;
import jakarta.persistence.EntityNotFoundException;
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
    public ResponseEntity<Product> addProduct(@Valid @RequestBody ProductRequest product, @RequestHeader("Authorization") String token) {
        Optional<User> user = userRepository.findByToken(token);
        if ((user.isEmpty())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Product entity = mapProductToEntity(product, user);
        productRepository.save(entity);
        return new ResponseEntity<>(entity, HttpStatus.CREATED);
    }

    @PutMapping("/{productId}")
    @AuthorizeRole(role = Role.SELLER)
    public ResponseEntity<Product> updateProduct(@PathVariable("productId") Long productId, @Valid @RequestBody ProductRequest product, @RequestHeader("Authorization") String token) {
        Product foundProduct = productRepository.findById(productId).orElseThrow(() -> new EntityNotFoundException());
        Optional<User> user = userRepository.findByToken(token);
        if ((user.isEmpty()) || !isSellerMatching(foundProduct, user)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        foundProduct.setAmountAvailable(product.amountAvailable());
        foundProduct.setCost(Integer.valueOf(product.cost()));
        foundProduct.setProductName(product.productName());
        productRepository.save(foundProduct);
        return new ResponseEntity<>(foundProduct, HttpStatus.OK);
    }

    @DeleteMapping("/{productId}")
    @AuthorizeRole(role = Role.SELLER)
    public ResponseEntity<String> deleteProduct(@PathVariable("productId") Long productId, @RequestHeader("Authorization") String token) {
        Optional<User> user = userRepository.findByToken(token);
        Product foundProduct = productRepository.findById(productId).orElseThrow(() -> new EntityNotFoundException());
        if ((user.isEmpty()) || !isSellerMatching(foundProduct, user)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        productRepository.delete(foundProduct);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }


    private static boolean isSellerMatching(Product foundProduct, Optional<User> user) {
        return user.isPresent() && foundProduct.getSellerId().equals(user.get().getId());
    }


    private static Product mapProductToEntity(ProductRequest product, Optional<User> user) {
        Product entity = Product.builder().productName(product.productName()).cost(Integer.valueOf(product.cost())).amountAvailable(product.amountAvailable()).sellerId(user.get().getId()).build();
        return entity;
    }

}
