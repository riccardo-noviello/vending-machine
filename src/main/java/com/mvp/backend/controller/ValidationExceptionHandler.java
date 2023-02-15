package com.mvp.backend.controller;

import com.mvp.backend.controller.response.ErrorMessageResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class ValidationExceptionHandler extends
        ResponseEntityExceptionHandler {

    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity<ErrorMessageResponse> handleValidationException(
            DataIntegrityViolationException ex) {

        return new ResponseEntity<>(new ErrorMessageResponse("Invalid constraint validation error"), new HttpHeaders(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({UnauthorizedUserException.class})
    public ResponseEntity<ErrorMessageResponse> handleUnauthorizedUserException(
            UnauthorizedUserException ex) {

        return new ResponseEntity<>(new ErrorMessageResponse(ex.getMessage()), new HttpHeaders(),
                HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<ErrorMessageResponse> handleEntityNotFoundException(
            EntityNotFoundException ex) {

        return new ResponseEntity<>(new ErrorMessageResponse("Entity not found"), new HttpHeaders(),
                HttpStatus.NOT_FOUND);
    }

}