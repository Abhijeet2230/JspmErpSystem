package in.edu.jspmjscoe.admissionportal.exception;

public class UnitAssessmentNotFoundException extends RuntimeException {

    public UnitAssessmentNotFoundException(Long unitId) {
        super("Unit assessment not found with id: " + unitId);
    }

    public UnitAssessmentNotFoundException(String message) {
        super(message);
    }
}
