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
                        headerMap.put(cell.getStringCellValue().trim(), cell.getColumnIndex());
                    }
                } else if (currentRowIndex > HEADER_ROW_INDEX) {
                    TeacherDTO teacher = TeacherDTO.builder()
                            .firstName(getCellValue(row, headerMap.get("First Name")))
                            .middleName(getCellValue(row, headerMap.get("Middle Name")))
                            .lastName(getCellValue(row, headerMap.get("Last Name (Surname)")))
                            .prefix(getCellValue(row, headerMap.get("Prefix")))
                            .gender(getCellValue(row, headerMap.get("Gender")))
                            .dateOfBirth(getCellValue(row, headerMap.get("Date of Birth")))
                            .phone(getCellValue(row, headerMap.get("Mobile No.")))
                            .personalEmail(getCellValue(row, headerMap.get("Email Address")))
                            .officialEmail(getCellValue(row, headerMap.get("Official Email ID (JSPM)\ne.g. maheshshinde@jspmjscoe.edu.in")))
                            .designation(getCellValue(row, headerMap.get("Present Designation")))
                            .aadhaarNumber(getCellValue(row, headerMap.get("AADHAAR")))
                            .bcudId(getCellValue(row, headerMap.get("BCUD ID \ne.g.52201698652")))
                            .vidwaanId(getCellValue(row, headerMap.get("VIDWAAN ID")))
                            .orchidId(getCellValue(row, headerMap.get("Orchid ID")))
                            .googleScholarId(getCellValue(row, headerMap.get("Google Scholar")))
                            .highestDegree(getCellValue(row, headerMap.get("Highest Degree")))
                            .specialization(getCellValue(row, headerMap.get("Area of Specialization")))
                            .degreeUniversity(getCellValue(row, headerMap.get("Mention University where Highest Degree Awarded")))
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

    private static String getCellValue(Row row, Integer index) {
        if (index == null) return null;
        Cell cell = row.getCell(index);
        if (cell == null) return null;

        DataFormatter formatter = new DataFormatter();
        return formatter.formatCellValue(cell).trim();
    }
}
