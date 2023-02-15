package com.mvp.backend.controller;

import com.mvp.backend.model.User;
import com.mvp.backend.repository.ProductRepository;
import com.mvp.backend.repository.UserRepository;
import com.mvp.backend.security.TokenProvider;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenProvider tokenProvider;


    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody User user) {
        userRepository.save(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam("username") String username, @RequestParam("password") String password) {
        Optional<User> user = userRepository.findByUsernameAndPassword(username, password);
        if (user.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if (user.get().getToken() != null) {
            return new ResponseEntity<>("There is already an active session using your account", HttpStatus.CONFLICT);
        }
        String token = tokenProvider.generateToken(username);
        user.get().setToken(token);
        userRepository.save(user.get());
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('SELLER', 'BUYER')")
    @PostMapping("/logout/all")
    public ResponseEntity<String> logoutAll() {
        // TODO:
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
