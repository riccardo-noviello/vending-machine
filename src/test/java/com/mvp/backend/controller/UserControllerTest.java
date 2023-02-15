package com.mvp.backend.controller;

import com.mvp.backend.model.Role;
import com.mvp.backend.model.User;
import com.mvp.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserControllerTest {
    @Mock
    private UserRepository userRepository;

    private UserController userController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userController = new UserController(userRepository);
    }

    @Test
    public void testRegister() {
        User user = getUser();
        when(userRepository.save(any(User.class))).thenReturn(user);

        ResponseEntity<String> responseEntity = userController.register(user);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        verify(userRepository).save(user);
    }

    private User getUser() {
        return User.builder().id(1L).username("testuser").password("testpassword").deposit(0).role(Role.BUYER).build();
    }

}