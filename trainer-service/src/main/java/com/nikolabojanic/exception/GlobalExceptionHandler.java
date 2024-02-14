package com.nikolabojanic.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(TsEntityNotFoundException.class)
    public ResponseEntity<Object> handleTsEntityNotFoundException(TsEntityNotFoundException e, WebRequest request) {
        return handle(e, HttpStatus.NOT_FOUND, request);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(TsValidationException.class)
    public ResponseEntity<Object> handleTsValidationException(TsValidationException e, WebRequest request) {
        return handle(e, HttpStatus.BAD_REQUEST, request);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(TsIllegalOperationException.class)
    public ResponseEntity<Object> handleTsIllegalOperationException(TsIllegalOperationException e, WebRequest request) {
        return handle(e, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
        HttpMessageNotReadableException e, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return handle(e, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return handle(ex, request);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException e, WebRequest request) {
        return handle(e, HttpStatus.BAD_REQUEST, request);
    }

    private ResponseEntity<Object> handle(RuntimeException e, HttpStatusCode status, WebRequest request) {
        log.error(e.getMessage(), e);
        return handleExceptionInternal(e, e.getMessage(), new HttpHeaders(), status, request);
    }

    private ResponseEntity<Object> handle(Exception e, WebRequest request) {
        log.error(e.getMessage(), e);
        ProblemDetail body = createProblemDetail(
            e, HttpStatus.BAD_REQUEST,
            "Incorrect input values.",
            null,
            null,
            request);
        return handleExceptionInternal(e, body, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

}
