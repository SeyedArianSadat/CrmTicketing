package com.company.crmticketing.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.util.HtmlUtils;

import java.time.LocalDateTime;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private final boolean success;
    private final String message;
    private final T data;
    private final LocalDateTime timestamp;
    private final int statusCode;
    private final String path;

    // ==================== SUCCESS METHODS ====================

    public static <T> ApiResponse<T> success(T data, String message, int statusCode, String path) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(sanitize(message))
                .data(data)
                .timestamp(LocalDateTime.now())
                .statusCode(statusCode)
                .path(sanitize(path))
                .build();
    }

    public static <T> ApiResponse<T> success(T data, String message, int statusCode) {
        return success(data, message, statusCode, null);
    }

    public static <T> ApiResponse<T> success(String message, int statusCode, String path) {
        return success(null, message, statusCode, path);
    }

    public static <T> ApiResponse<T> success(String message, int statusCode) {
        return success(null, message, statusCode, null);
    }

    public static <T> ApiResponse<T> success(String message) {
        return success(null, message, 200, null);
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return success(data, message, 200, null);
    }

    // ==================== ERROR METHODS ====================

    public static <T> ApiResponse<T> error(String message, int statusCode, String path) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(sanitize(message))
                .timestamp(LocalDateTime.now())
                .statusCode(statusCode)
                .path(sanitize(path))
                .build();
    }

    public static <T> ApiResponse<T> error(String message, int statusCode) {
        return error(message, statusCode, null);
    }

    public static <T> ApiResponse<T> error(String message) {
        return error(message, 500, null);
    }

    // ==================== HELPER METHODS ====================

    private static String sanitize(String input) {
        if (input == null) return null;
        return HtmlUtils.htmlEscape(input);
    }

    public static <T> ApiResponse<T> of(boolean success, String message, T data, int statusCode, String path) {
        return ApiResponse.<T>builder()
                .success(success)
                .message(sanitize(message))
                .data(data)
                .timestamp(LocalDateTime.now())
                .statusCode(statusCode)
                .path(sanitize(path))
                .build();
    }
}