package com.company.crmticketing.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.util.HtmlUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private final String error;
    private final String message;
    private final int status;
    private final String path;
    private final LocalDateTime timestamp;
    private final Map<String, String> validationErrors;

    public static ErrorResponse of(String error, String message, int status, String path) {
        return ErrorResponse.builder()
                .error(sanitize(error))
                .message(sanitize(message))
                .status(status)
                .path(sanitize(path))
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static ErrorResponse validationError(Map<String, String> validationErrors, String path) {
        Map<String, String> sanitizedErrors = new HashMap<>();
        validationErrors.forEach((key, value) -> sanitizedErrors.put(sanitize(key), sanitize(value)));

        return ErrorResponse.builder()
                .error("Validation Failed")
                .message("Invalid input data")
                .status(400)
                .path(sanitize(path))
                .timestamp(LocalDateTime.now())
                .validationErrors(sanitizedErrors)
                .build();
    }

    private static String sanitize(String input) {
        if (input == null) return null;
        return HtmlUtils.htmlEscape(input);
    }

    public static ErrorResponse of(String message, int status, String path) {
        return of(null, message, status, path);
    }

    public static ErrorResponse of(String message, int status) {
        return of(message, status, null);
    }

    public static ErrorResponse of(String error, String message, int status) {
        return of(error, message, status, null);
    }
}