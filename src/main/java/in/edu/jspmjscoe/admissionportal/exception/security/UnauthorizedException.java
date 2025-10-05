package in.edu.jspmjscoe.admissionportal.exception.security;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}