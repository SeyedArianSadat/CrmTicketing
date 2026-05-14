package com.company.crmticketing.exception;

import com.company.crmticketing.dto.response.ApiResponse;
import com.company.crmticketing.dto.response.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ==================== AUTHENTICATION & AUTHORIZATION ====================

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadCredentials(
            BadCredentialsException ex,
            WebRequest request
    ) {
        log.error("❌ Bad credentials: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error(
                        "Invalid username or password",
                        HttpStatus.UNAUTHORIZED.value(),
                        getPath(request)
                ));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleUsernameNotFound(
            UsernameNotFoundException ex,
            WebRequest request
    ) {
        log.error("❌ User not found: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(
                        ex.getMessage(),
                        HttpStatus.NOT_FOUND.value(),
                        getPath(request)
                ));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccessDenied(
            AccessDeniedException ex,
            WebRequest request
    ) {
        log.error("❌ Access denied: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.error(
                        "Access denied. You don't have permission to access this resource.",
                        HttpStatus.FORBIDDEN.value(),
                        getPath(request)
                ));
    }

    // ==================== JWT EXCEPTIONS ====================

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ApiResponse<Void>> handleExpiredJwt(
            ExpiredJwtException ex,
            WebRequest request
    ) {
        log.error("❌ JWT token expired: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error(
                        "Token has expired. Please login again.",
                        HttpStatus.UNAUTHORIZED.value(),
                        getPath(request)
                ));
    }

    @ExceptionHandler({MalformedJwtException.class, SignatureException.class})
    public ResponseEntity<ApiResponse<Void>> handleInvalidJwt(
            Exception ex,
            WebRequest request
    ) {
        log.error("❌ Invalid JWT token: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error(
                        "Invalid token. Please login again.",
                        HttpStatus.UNAUTHORIZED.value(),
                        getPath(request)
                ));
    }

    // ==================== VALIDATION EXCEPTIONS ====================

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex,
            WebRequest request
    ) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        log.error("❌ Validation failed: {}", errors);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.validationError(errors, getPath(request)));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(
            ConstraintViolationException ex,
            WebRequest request
    ) {
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(violation -> {
            String fieldName = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            errors.put(fieldName, errorMessage);
        });

        log.error("❌ Constraint violation: {}", errors);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.validationError(errors, getPath(request)));
    }

    // ==================== DATA INTEGRITY EXCEPTIONS ====================

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataIntegrityViolation(
            DataIntegrityViolationException ex,
            WebRequest request
    ) {
        log.error("❌ Data integrity violation: {}", ex.getMessage());

        String message = "Database error occurred";
        if (ex.getMessage().contains("Unique constraint") || ex.getMessage().contains("Duplicate entry")) {
            message = "Duplicate entry. This value already exists.";
        }

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(
                        message,
                        HttpStatus.CONFLICT.value(),
                        getPath(request)
                ));
    }

    // ==================== ARGUMENT & REQUEST EXCEPTIONS ====================

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex,
            WebRequest request
    ) {
        log.error("❌ Type mismatch: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(
                        String.format("Invalid value '%s' for parameter '%s'", ex.getValue(), ex.getName()),
                        HttpStatus.BAD_REQUEST.value(),
                        getPath(request)
                ));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleMessageNotReadable(
            HttpMessageNotReadableException ex,
            WebRequest request
    ) {
        log.error("❌ Malformed JSON: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(
                        "Malformed JSON request. Please check your request body.",
                        HttpStatus.BAD_REQUEST.value(),
                        getPath(request)
                ));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgument(
            IllegalArgumentException ex,
            WebRequest request
    ) {
        log.error("❌ Illegal argument: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(
                        ex.getMessage(),
                        HttpStatus.BAD_REQUEST.value(),
                        getPath(request)
                ));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalState(
            IllegalStateException ex,
            WebRequest request
    ) {
        log.error("❌ Illegal state: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(
                        ex.getMessage(),
                        HttpStatus.CONFLICT.value(),
                        getPath(request)
                ));
    }

    // ==================== RESOURCE NOT FOUND ====================

    @ExceptionHandler(jakarta.persistence.EntityNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleEntityNotFound(
            jakarta.persistence.EntityNotFoundException ex,
            WebRequest request
    ) {
        log.error("❌ Entity not found: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(
                        ex.getMessage(),
                        HttpStatus.NOT_FOUND.value(),
                        getPath(request)
                ));
    }

    // ==================== GENERAL EXCEPTIONS ====================

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericException(
            Exception ex,
            WebRequest request
    ) {
        log.error("❌ Unexpected error occurred: ", ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(
                        "An unexpected error occurred. Please try again later.",
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        getPath(request)
                ));
    }

    @ExceptionHandler(TicketNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleTicketNotFound(TicketNotFoundException ex, WebRequest request) {
        log.error("❌ticket not found", ex);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(
                        ex.getMessage(),
                        HttpStatus.NOT_FOUND.value(),
                        getPath(request)
                ));
    }

    @ExceptionHandler(TicketAlreadyExistException.class)
    public ResponseEntity<ApiResponse<Void>> handleTicketAlreadyExist(TicketAlreadyExistException ex, WebRequest request) {
        log.error("❌ticket already exists", ex);
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(
                        ex.getMessage(),
                        HttpStatus.CONFLICT.value(),
                        getPath(request)
                ));
    }

    @ExceptionHandler(TicketDeletionException.class)
    public ResponseEntity<ApiResponse<Void>> handleTicketDeletion(TicketDeletionException ex, WebRequest request) {
        log.error("❌ticket deletion", ex);
        return ResponseEntity
                .status(HttpStatus.NOT_ACCEPTABLE)
                .body(ApiResponse.error(
                        ex.getMessage(),
                        HttpStatus.NOT_ACCEPTABLE.value(),
                        getPath(request)
                ));
    }

    @ExceptionHandler(TicketCreationException.class)
    public ResponseEntity<ApiResponse<Void>> handleTicketCreation(TicketCreationException ex, WebRequest request) {
        log.error("❌ticket creation", ex);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(
                        ex.getMessage(),
                        HttpStatus.BAD_REQUEST.value(),
                        getPath(request)
                ));
    }

    @ExceptionHandler(TicketUpdateException.class)
    public ResponseEntity<ApiResponse<Void>> handleTicketUpdate(TicketUpdateException ex, WebRequest request) {
        log.error("❌ticket update", ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(
                        ex.getMessage(),
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        getPath(request)
                ));
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleCustomerNotFound(CustomerNotFoundException ex, WebRequest request) {
        log.error("Customer not found", ex);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(
                        ex.getMessage(),
                        HttpStatus.NOT_FOUND.value(),
                        getPath(request)
                ));
    }

    @ExceptionHandler(CustomerRequestNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleCustomerRequestNotFound(CustomerRequestNotFoundException ex, WebRequest request) {
        log.error("Customer request not found", ex);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(
                        ex.getMessage(),
                        HttpStatus.NOT_FOUND.value(),
                        getPath(request)
                ));
    }

    @ExceptionHandler(AttachmentNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleAttachmentNotFound(AttachmentNotFoundException ex, WebRequest request) {
        log.error("Attachment not found", ex);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(
                        ex.getMessage(),
                        HttpStatus.NOT_FOUND.value(),
                        getPath(request)

                ));
    }

    @ExceptionHandler(SupportAgentNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleSupportAgentNotFound(SupportAgentNotFoundException ex, WebRequest request) {
        log.error("Support agent not found", ex);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(
                        ex.getMessage(),
                        HttpStatus.NOT_FOUND.value(),
                        getPath(request)
                ));
    }

    // ==================== HELPER METHODS ====================

    private String getPath(WebRequest request) {
        return request.getDescription(false).replace("uri=", "");
    }
}