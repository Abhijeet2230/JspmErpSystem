package in.edu.jspmjscoe.admissionportal.exception.minio;

public class AchievementFileAccessException extends RuntimeException {

    public AchievementFileAccessException() {
        super();
    }

    public AchievementFileAccessException(String message) {
        super(message);
    }

    public AchievementFileAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
