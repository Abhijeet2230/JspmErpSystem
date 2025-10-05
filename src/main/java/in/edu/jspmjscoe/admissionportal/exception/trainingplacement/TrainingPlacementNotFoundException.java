package in.edu.jspmjscoe.admissionportal.exception.trainingplacement;

public class TrainingPlacementNotFoundException extends RuntimeException {
    public TrainingPlacementNotFoundException(Long studentId, String testName) {
        super("Training placement record not found for studentId=" + studentId + ", test=" + testName);
    }
}

