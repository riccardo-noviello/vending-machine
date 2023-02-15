package com.mvp.backend.controller;

import com.mvp.backend.model.Role;
import com.mvp.backend.model.User;
import com.mvp.backend.repository.UserRepository;
import com.mvp.backend.security.TokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class LoginControllerTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenProvider tokenProvider;

    private LoginController loginController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        loginController = new LoginController(userRepository, tokenProvider);
    }

    @Test
    public void shouldReturnOkWhenLoginWithValidCredentials() {
        User user = getUser();
        Optional<User> optionalUser = Optional.of(user);

        when(userRepository.findByUsernameAndPassword("testuser", "testpassword")).thenReturn(optionalUser);
        when(tokenProvider.generateToken("testuser")).thenReturn("testtoken");

        ResponseEntity<String> response = loginController.login("testuser", "testpassword");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("testtoken", response.getBody());
        verify(userRepository, times(1)).save(user);
    }


    @Test
    public void shouldReturnHttpStatusUnauthorizedWhenLoginWithInvalidCredentials() {
        Optional<User> optionalUser = Optional.empty();

        when(userRepository.findByUsernameAndPassword("testuser", "testpassword")).thenReturn(optionalUser);

        ResponseEntity<String> response = loginController.login("testuser", "testpassword");

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void shouldReturnHttpStatusConflictWhenLoginWithUserWithActiveSession() {
        User user = getUser();
        user.setToken("testtoken");
        Optional<User> optionalUser = Optional.of(user);

        when(userRepository.findByUsernameAndPassword("testuser", "testpassword")).thenReturn(optionalUser);

        ResponseEntity<String> response = loginController.login("testuser", "testpassword");

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void shouldReturnHttpStatusAcceptedWhenLogoutAll() {
        var entitiesWithToken = Arrays.asList(getUser().withToken("someToken"));
        var entitiesWithoutToken = Arrays.asList(getUser());

        when(userRepository.findAll()).thenReturn(entitiesWithToken);
        ResponseEntity<String> response = loginController.logoutAll();
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        verify(userRepository, times(1)).saveAll(entitiesWithoutToken);
    }

    private User getUser() {
        return User.builder().id(1L).username("testuser").password("testpassword").deposit(0).role(Role.BUYER).build();
    }
}