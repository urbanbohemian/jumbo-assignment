package com.trendyol.international.commission.invoice.api.model.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionModel> handleException(Exception exception) {
        return new ResponseEntity<>(ExceptionModel.builder()
                .exception(exception.getClass().getName())
                .message(exception.getMessage())
                .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}