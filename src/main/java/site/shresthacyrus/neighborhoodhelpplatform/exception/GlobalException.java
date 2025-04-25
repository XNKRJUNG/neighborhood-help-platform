package site.shresthacyrus.neighborhoodhelpplatform.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import site.shresthacyrus.neighborhoodhelpplatform.exception.bid.BidAlreadyAcceptedException;
import site.shresthacyrus.neighborhoodhelpplatform.exception.bid.BidNotFoundException;
import site.shresthacyrus.neighborhoodhelpplatform.exception.job.InvalidJobStatusTransitionException;
import site.shresthacyrus.neighborhoodhelpplatform.exception.job.InvalidPriceRangeException;
import site.shresthacyrus.neighborhoodhelpplatform.exception.skill.SkillNotFoundException;
import site.shresthacyrus.neighborhoodhelpplatform.exception.user.DuplicateUserException;
import site.shresthacyrus.neighborhoodhelpplatform.exception.skill.DuplicateSkillException;

import java.util.stream.Collectors;

import static site.shresthacyrus.neighborhoodhelpplatform.util.ErrorResponseUtil.buildResponse;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity<ApiError> handleDuplicateUserException(DuplicateUserException e, HttpServletRequest request) {
        return buildResponse(HttpStatus.CONFLICT, e.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(DuplicateSkillException.class)
    public ResponseEntity<ApiError> handleDuplicateSkillException(DuplicateSkillException e, HttpServletRequest request) {
        return buildResponse(HttpStatus.CONFLICT, e.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> handleBadCredentials(BadCredentialsException e, HttpServletRequest request) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "Invalid username or password.", request.getRequestURI());
    }

    @ExceptionHandler(SkillNotFoundException.class)
    public ResponseEntity<ApiError> handleSkillNotFound(SkillNotFoundException e, HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, e.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(InvalidPriceRangeException.class)
    public ResponseEntity<ApiError> handleInvalidPriceRange(InvalidPriceRangeException e, HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, e.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(BidNotFoundException.class)
    public ResponseEntity<ApiError> handleBidNotFound(BidNotFoundException e, HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, e.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(BidAlreadyAcceptedException.class)
    public ResponseEntity<ApiError> handleBidAlreadyAccepted(BidAlreadyAcceptedException e, HttpServletRequest request) {
        return buildResponse(HttpStatus.CONFLICT, e.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(InvalidJobStatusTransitionException.class)
    public ResponseEntity<ApiError> handleInvalidJobTransition(InvalidJobStatusTransitionException e, HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, e.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDenied(AccessDeniedException e, HttpServletRequest request) {
        return buildResponse(HttpStatus.FORBIDDEN, "Access Denied", request.getRequestURI());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException e, HttpServletRequest request) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return buildResponse(HttpStatus.BAD_REQUEST, message, request.getRequestURI());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(Exception e, HttpServletRequest request) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), request.getRequestURI());
    }

}
