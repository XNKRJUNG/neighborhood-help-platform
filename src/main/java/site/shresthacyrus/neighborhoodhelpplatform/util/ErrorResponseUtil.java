package site.shresthacyrus.neighborhoodhelpplatform.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import site.shresthacyrus.neighborhoodhelpplatform.exception.ApiError;

import java.time.Instant;

public class ErrorResponseUtil {

    public static ResponseEntity<ApiError> buildResponse(HttpStatus status, String message, String path) {
        ApiError error = new ApiError(
                message,
                path,
                status.value(),
                Instant.now()
        );
        return ResponseEntity.status(status).body(error);
    }
}