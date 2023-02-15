package com.mvp.backend.security;

import com.mvp.backend.controller.UserController;
import com.mvp.backend.model.Role;
import com.mvp.backend.model.User;
import com.mvp.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

public class SecurityServiceTest {

    @Mock
    private UserRepository userRepository;
    private SecurityService securityService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        securityService = new SecurityService(userRepository);
    }

    @Test
    public void testIsBuyer() {
        User user = getUser().withRole(Role.BUYER);
        when(userRepository.findByToken("testtoken")).thenReturn(Optional.of(user));

        boolean isBuyer = securityService.isBuyer("testtoken");

        assertTrue(isBuyer);
    }

    @Test
    public void testIsSeller() {
        User user = getUser().withRole(Role.SELLER);
        when(userRepository.findByToken("testtoken")).thenReturn(Optional.of(user));

        boolean isSeller = securityService.isSeller("testtoken");

        assertTrue(isSeller);
    }

    @Test
    public void testIsAny() {
        User user = getUser().withRole(Role.BUYER);
        when(userRepository.findByToken("testtoken")).thenReturn(Optional.of(user));

        boolean isAny = securityService.isAny("testtoken");

        assertTrue(isAny);
    }

    @Test
    public void testIsRoleInvalidUser() {
        when(userRepository.findByToken("testtoken")).thenReturn(Optional.empty());

        boolean isBuyer = securityService.isBuyer("testtoken");
        boolean isSeller = securityService.isSeller("testtoken");
        boolean isAny = securityService.isAny("testtoken");

        assertFalse(isBuyer);
        assertFalse(isSeller);
        assertFalse(isAny);
    }

    @Test
    public void testIsRoleInvalidRole() {
        User user = getUser().withRole(null);
        when(userRepository.findByToken("testtoken")).thenReturn(Optional.of(user));

        boolean isBuyer = securityService.isBuyer("testtoken");
        boolean isSeller = securityService.isSeller("testtoken");
        boolean isAny = securityService.isAny("testtoken");

        assertFalse(isBuyer);
        assertFalse(isSeller);
        assertFalse(isAny);
    }


    private User getUser() {
        return User.builder().id(1L).username("testuser").password("testpassword").deposit(0).role(Role.BUYER).build();
    }
}
