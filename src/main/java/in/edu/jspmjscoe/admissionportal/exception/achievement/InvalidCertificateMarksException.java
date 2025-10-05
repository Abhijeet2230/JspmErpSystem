package in.edu.jspmjscoe.admissionportal.exception.achievement;

public class InvalidCertificateMarksException extends RuntimeException {
    public InvalidCertificateMarksException(Long certificateId, Double marks) {
        super("Invalid marks: " + marks + " for certificateId=" + certificateId 
              + ". Allowed range is 0–10.");
    }
}
