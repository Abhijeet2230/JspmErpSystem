package in.edu.jspmjscoe.admissionportal.services.excel;

import org.springframework.web.multipart.MultipartFile;

public interface TeacherExcelImportService {
    /**
     * Import teachers from uploaded Excel file.
     * @param file uploaded .xlsx file
     * @param headerRowNumber 1-based header row (e.g. 2 if header is on second row)
     * @return number of teachers imported
     */
    int importTeachers(MultipartFile file, int headerRowNumber);
}
