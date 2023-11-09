package com.bloomberg.clustereddatawarehouse.exceptionhandler;

import com.bloomberg.clustereddatawarehouse.exceptions.DuplicateFXDealException;
import com.bloomberg.clustereddatawarehouse.exceptions.InvalidRequestException;
import com.bloomberg.clustereddatawarehouse.exceptions.NotFoundException;
import com.bloomberg.clustereddatawarehouse.util.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateFXDealException.class)
    public ResponseEntity<Object> handleDuplicateFXDealException(DuplicateFXDealException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.builder()
                .message(ex.getMessage())
                .status(HttpStatus.CONFLICT)
                .build()
        );
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<Object> handleInvalidException(InvalidRequestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.builder()
                .message(ex.getMessage())
                .status(HttpStatus.BAD_REQUEST)
                .build()
        );
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.builder()
                .message(ex.getMessage())
                .status(HttpStatus.NOT_FOUND)
                .build()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGlobalException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.builder()
                .message(ex.getMessage())
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build()
        );
    }

}

