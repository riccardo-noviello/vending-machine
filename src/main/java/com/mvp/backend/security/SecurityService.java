package com.mvp.backend.security;

import com.mvp.backend.model.Role;
import com.mvp.backend.model.User;
import com.mvp.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class SecurityService {

    @Autowired
    private UserRepository userRepository;


    public boolean isBuyer(String userToken) {
        return isRole(userToken, Arrays.asList(Role.BUYER));
    }

    public boolean isSeller(String userToken) {
        return isRole(userToken, Arrays.asList(Role.SELLER));
    }

    public boolean isAny(String userToken){
        return isRole(userToken, Arrays.asList(Role.SELLER, Role.BUYER));
    }

    private boolean isRole(String userToken, List<Role> roles) {
        Optional<User> user = this.userRepository.findByToken(userToken);
        if (user.isEmpty()) return false;

        return roles.contains(user.get().getRole());
    }
}