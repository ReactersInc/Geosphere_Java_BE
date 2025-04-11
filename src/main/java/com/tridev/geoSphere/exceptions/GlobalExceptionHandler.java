package com.tridev.geoSphere.exceptions;

import com.tridev.geoSphere.common.BaseResponse;
import com.tridev.geoSphere.utils.ResponseBuilder;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse> handleAllExceptions(Exception ex) {
        return ResponseBuilder.error(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong: " + ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<BaseResponse> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseBuilder.error(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<BaseResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return ResponseBuilder.error(HttpStatus.BAD_REQUEST, "Invalid input: " + ex.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<BaseResponse> handleValidation(ConstraintViolationException ex) {
        return ResponseBuilder.error(HttpStatus.BAD_REQUEST, "Validation failed: " + ex.getMessage());
    }



}
