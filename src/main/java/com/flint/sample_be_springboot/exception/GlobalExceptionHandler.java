package com.flint.sample_be_springboot.exception;

import com.flint.sample_be_springboot.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handelCustomException(CustomException customException) {
        log.error("Custom Exception occurred :", customException);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(customException.getMessage())
                .errorCode(customException.getStatus().toString())
                .details(customException.toString())
                .build();
        return ResponseEntity.ok(errorResponse);
    }



    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException methodArgumentNotValidException) {
        log.error("MethodArgumentNotValidException occurred :", methodArgumentNotValidException);

        List<String> errors = methodArgumentNotValidException.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();

        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(errors.toString())
                .errorCode(methodArgumentNotValidException.getStatusCode().toString())
                .details(methodArgumentNotValidException.toString())
                .build();
        return ResponseEntity.ok(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception exception) {
        log.error("Exception occurred :" + exception);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(exception.getMessage())
                .errorCode(exception.toString())
                .details(exception.toString())
                .build();
        return ResponseEntity.ok(errorResponse);
    }
}
