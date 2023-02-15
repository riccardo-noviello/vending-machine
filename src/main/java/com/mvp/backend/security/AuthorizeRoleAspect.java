package com.mvp.backend.security;

import com.mvp.backend.model.Role;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * This is an alternative to customise several Spring's config classes.
 * We kept it simple by handling Authorization Headers with custom logic when the
 * {@link com.mvp.backend.security.AuthorizeRole} annotation is present.
 */
@Aspect
@Component
public class AuthorizeRoleAspect {

    @Autowired
    private SecurityService securityService;

    @Before(value = "@annotation(authorizeRole)", argNames = "authorizeRole")
    public void preHandle(AuthorizeRole authorizeRole) {
        Role role = authorizeRole.role();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String authorizationHeader = request.getHeader("Authorization");
        var isValidAuthentication = switch (role) {
            case BUYER -> securityService.isBuyer(authorizationHeader);
            case SELLER -> securityService.isSeller(authorizationHeader);
            case ANY -> securityService.isAny(authorizationHeader);
        };

        if (!isValidAuthentication) {
            throw new UnauthorizedUserException(String.format("User is required the role: '%s' for this endpoint", role));
        }

    }
}