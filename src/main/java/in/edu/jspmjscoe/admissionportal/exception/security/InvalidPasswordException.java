package in.edu.jspmjscoe.admissionportal.exception.security;

public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException(String message) {
        super(message);
    }
}