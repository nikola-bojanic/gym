package com.nikolabojanic.exception;

import feign.RetryableException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = ScValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleScValidationException(ScValidationException e, WebRequest request) {
        return handleException(e, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = ScAuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Object> handleScAuthenticationException(ScAuthenticationException e, WebRequest request) {
        return handleException(e, HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(value = ScEntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleScEntityNotFoundException(ScEntityNotFoundException e, WebRequest request) {
        return handleException(e, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = ScNotAuthorizedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<Object> handleScNotAuthorizedException(ScNotAuthorizedException e, WebRequest request) {
        return handleException(e, HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler(value = RetryableException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleRetryableException(RetryableException e, WebRequest request) {
        return handleException(e, request);
    }
    @ExceptionHandler(value = RuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException e, WebRequest request) {
        return handleException(e, HttpStatus.BAD_REQUEST, request);
    }

    private ResponseEntity<Object> handleException(RuntimeException e, HttpStatusCode status, WebRequest request) {
        log.error(e.getMessage(), e);
        return handleExceptionInternal(e, e.getMessage(), new HttpHeaders(), status, request);
    }

    private ResponseEntity<Object> handleException(RetryableException e, WebRequest request) {
        log.error(e.getMessage(), e);
        return handleExceptionInternal(e,
            "Server error.",
            new HttpHeaders(),
            HttpStatus.INTERNAL_SERVER_ERROR,
            request);
    }
}
