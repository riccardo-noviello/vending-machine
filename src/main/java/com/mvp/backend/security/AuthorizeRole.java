package com.mvp.backend.security;

import com.mvp.backend.model.Role;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This is an alternative to using Spring's @PreAuthorize annotation, for simplicity.
 * Using Spring annotation requires a lot of plumbing and configuration to make it work with JWT tokens.
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthorizeRole {
    Role role();
}
