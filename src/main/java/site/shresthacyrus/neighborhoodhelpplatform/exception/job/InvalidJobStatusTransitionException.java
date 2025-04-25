package site.shresthacyrus.neighborhoodhelpplatform.exception.job;

public class InvalidJobStatusTransitionException extends RuntimeException {
    public InvalidJobStatusTransitionException(String message) {
        super(message);
    }
}