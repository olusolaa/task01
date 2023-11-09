package com.bloomberg.clustereddatawarehouse.util;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public final class ResponseBuilder {

    public static <T> ResponseEntity<ApiResponse<T>> ok(T data, String message) {
        ApiResponse<T> apiResponse = ApiResponse.<T>builder()
                .data(data)
                .status(HttpStatus.OK)
                .message(message)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    public static <T> ResponseEntity<ApiResponse<T>> created(T data, String message) {
        ApiResponse<T> apiResponse = ApiResponse.<T>builder()
                .data(data)
                .status(HttpStatus.CREATED)
                .message(message)
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    // This method is specifically designed for Page objects, so the generic type is Page<T>
    public static <T> ResponseEntity<ApiResponse<Page<T>>> ok(Page<T> page, String message) {
        ApiResponse<Page<T>> apiResponse = ApiResponse.<Page<T>>builder()
                .data(page)
                .status(HttpStatus.OK)
                .message(message)
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();

        return ResponseEntity.ok(apiResponse);
    }
}

