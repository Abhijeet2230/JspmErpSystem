package in.edu.jspmjscoe.admissionportal.exception.cce;

public class ExamNotFoundException extends RuntimeException {

    public ExamNotFoundException(Long examId) {
        super("Exam not found with id: " + examId);
    }

    public ExamNotFoundException(String message) {
        super(message);
    }
}
