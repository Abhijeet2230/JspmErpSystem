package in.edu.jspmjscoe.admissionportal.exception.excel;

public class ExcelImportException extends RuntimeException {
    public ExcelImportException(String message) {
        super(message);
    }

    public ExcelImportException(String message, Throwable cause) {
        super(message, cause);
    }
}
