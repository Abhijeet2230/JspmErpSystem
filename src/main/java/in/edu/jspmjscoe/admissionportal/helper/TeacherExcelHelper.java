package in.edu.jspmjscoe.admissionportal.helper;

import in.edu.jspmjscoe.admissionportal.dtos.teacher.TeacherDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.util.*;

public class TeacherExcelHelper {

    // Row number where headers exist (3rd row in Excel = index 2)
    private static final int HEADER_ROW_INDEX = 2;

    public static List<TeacherDTO> excelToTeacherDTOs(InputStream is) {
        try {
            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheetAt(0);

            Iterator<Row> rows = sheet.iterator();
            List<TeacherDTO> teachers = new ArrayList<>();

            Map<String, Integer> headerMap = new HashMap<>();
            int currentRowIndex = 0;

            // Iterate until we reach header row
            while (rows.hasNext()) {
                Row row = rows.next();

                if (currentRowIndex == HEADER_ROW_INDEX) {
                    for (Cell cell : row) {
                        String normalizedHeader = normalizeHeader(cell.getStringCellValue());
                        headerMap.put(normalizedHeader, cell.getColumnIndex());
                        System.out.println("Header found: '" + normalizedHeader + "'");
                    }
                } else if (currentRowIndex > HEADER_ROW_INDEX) {
                    TeacherDTO teacher = TeacherDTO.builder()
                            .firstName(getCellValue(row, headerMap, "First Name"))
                            .middleName(getCellValue(row, headerMap, "Middle Name"))
                            .lastName(getCellValue(row, headerMap, "Last Name (Surname)"))
                            .prefix(getCellValue(row, headerMap, "Prefix"))
                            .gender(getCellValue(row, headerMap, "Gender"))
                            .dateOfBirth(getCellValue(row, headerMap, "Date of Birth"))
                            .phone(getCellValue(row, headerMap, "Mobile No."))
                            .personalEmail(getCellValue(row, headerMap, "Email Address"))
                            .officialEmail(getCellValue(row, headerMap, "Official Email ID (JSPM)"))
                            .departmentName(getCellValue(row, headerMap, "Department"))
                            .designation(getCellValue(row, headerMap, "Present Designation"))
                            .aadhaarNumber(getCellValue(row, headerMap, "AADHAAR"))
                            .bcudId(getCellValue(row, headerMap, "BCUD ID"))
                            .vidwaanId(getCellValue(row, headerMap, "VIDWAAN ID"))
                            .orchidId(getCellValue(row, headerMap, "Orchid ID"))
                            .googleScholarId(getCellValue(row, headerMap, "Google Scholar"))
                            .highestDegree(getCellValue(row, headerMap, "Highest Degree"))
                            .specialization(getCellValue(row, headerMap, "Area of Specialization"))
                            .degreeUniversity(getCellValue(row, headerMap, "Mention University where Highest Degree Awarded"))
                            .build();

                    // Skip empty rows (if firstName and lastName are null/blank)
                    if (teacher.getFirstName() != null && !teacher.getFirstName().isBlank()) {
                        teachers.add(teacher);
                    }
                }

                currentRowIndex++;
            }

            workbook.close();
            return teachers;

        } catch (Exception e) {
            throw new RuntimeException("Fail to parse Excel file: " + e.getMessage(), e);
        }
    }

    // ----------------- Helper methods -----------------

    private static String normalizeHeader(String header) {
        if (header == null) return "";
        return header.replaceAll("\\r|\\n", " ") // remove newlines
                .replaceAll("\\s+", " ")   // multiple spaces -> single
                .trim();
    }

    private static String getCellValue(Row row, Map<String, Integer> headerMap, String candidate) {
        if (candidate == null || candidate.isBlank()) return null;

        String normalizedCandidate = normalizeHeader(candidate).toLowerCase();
        Integer idx = headerMap.entrySet().stream()
                .filter(e -> normalizeHeader(e.getKey()).toLowerCase().equals(normalizedCandidate))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);

        if (idx == null) return null;

        Cell cell = row.getCell(idx);
        if (cell == null) return null;

        DataFormatter formatter = new DataFormatter();
        return formatter.formatCellValue(cell).trim();
    }
}
