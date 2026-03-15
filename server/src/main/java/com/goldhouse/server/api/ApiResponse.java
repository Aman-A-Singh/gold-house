package com.goldhouse.server.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private LocalDateTime timestamp;
    private Long totalCount = null;
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

    public static <T> ApiResponse<T> successWithCount(T data, long totalCount) {
        return ApiResponse.<T>builder()
                .success(true)
                .timestamp(LocalDateTime.now())
                .totalCount(totalCount)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> successWithCount(T data, String message, long totalCount) {
        return ApiResponse.<T>builder()
                .success(true)
                .timestamp(LocalDateTime.now())
                .message(message)
                .totalCount(totalCount)
                .data(data)
                .build();
    }

    // Paging response

    @Data
    @AllArgsConstructor
    public static class PagedResponse<D> {
        private List<D> content;
        private int currentPage;
        private long totalItems;
        private int totalPages;
        private boolean isLast;
    }

    public static <D> ApiResponse<PagedResponse<D>> successPaging(Page<D> pageData) {
        var pagedResponse = new PagedResponse<>(
                pageData.getContent(),
                pageData.getNumber(),
                pageData.getTotalElements(),
                pageData.getTotalPages(),
                pageData.isLast()
        );
        return ApiResponse.<PagedResponse<D>>builder()
                .success(true)
                .message("Request processed successfully")
                .data(pagedResponse) // This is now type-safe!
                .timestamp(LocalDateTime.now())
                .build();
    }
}
