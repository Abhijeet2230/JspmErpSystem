package in.edu.jspmjscoe.admissionportal.services.excel;

import org.springframework.web.multipart.MultipartFile;

public interface ExcelImportService {

    /**
     * Import students from Excel file and save to database.
     *
     * @param file Excel file uploaded by user
     * @return number of students successfully imported
     */
    int importStudents(MultipartFile file);

    int importStudentsBasic(MultipartFile file, int headerRowNumber);
}
