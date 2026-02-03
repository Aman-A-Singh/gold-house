package com.goldhouse.server.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private LocalDateTime timestamp;
    private long totalCount;
    private String path;
    private T data;

    // Success response
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message("Request processed successfully")
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .timestamp(LocalDateTime.now())
                .message(message)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> successWithCount(T data,long totalCount) {
        return ApiResponse.<T>builder()
                .success(true)
                .timestamp(LocalDateTime.now())
                .totalCount(totalCount)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> successWithCount(T data, String message,long totalCount) {
        return ApiResponse.<T>builder()
                .success(true)
                .timestamp(LocalDateTime.now())
                .message(message)
                .totalCount(totalCount)
                .data(data)
                .build();
    }
}
