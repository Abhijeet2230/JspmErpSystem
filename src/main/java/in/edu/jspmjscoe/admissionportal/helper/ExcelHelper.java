package in.edu.jspmjscoe.admissionportal.helper;

import in.edu.jspmjscoe.admissionportal.dtos.excel.ExcelStudentDTO;
import in.edu.jspmjscoe.admissionportal.dtos.student.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ExcelHelper {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("M/d/yyyy");

    public static List<StudentDTO> excelToStudentDTOs(InputStream is) {
        try (Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(1); // first sheet; adjust if needed

            // ---- Build header map from row 3 (index 2) ----
            Row headerRow = sheet.getRow(2); // 0-based index: third row
            Map<String,Integer> headerMap = new HashMap<>();
            for (Cell cell : headerRow) {
                String header = cell.toString().trim();
                if (!header.isEmpty()) {
                    headerMap.put(header, cell.getColumnIndex());
                }
            }

            List<StudentDTO> students = new ArrayList<>();

            // ---- Data starts from row 4 (index 3) ----
            for (int i = 3; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                StudentDTO studentDTO = new StudentDTO();

                // Basic Info
                studentDTO.setApplicationId(getCellString(row, headerMap, "Application ID"));
                studentDTO.setCandidateName(getCellString(row, headerMap, "Candidate Name"));
                studentDTO.setMobileNo(getCellString(row, headerMap, "Mobile No"));
                studentDTO.setEmail(getCellString(row, headerMap, "E-Mail ID"));
                studentDTO.setGender(getCellString(row, headerMap, "Gender"));
                studentDTO.setDob(getDobFormatted(row, headerMap, "DOB"));
                studentDTO.setReligion(getCellString(row, headerMap, "Religion"));
                studentDTO.setRegion(getCellString(row, headerMap, "Region"));
                studentDTO.setMotherTongue(getCellString(row, headerMap, "Mother Tongue"));
                studentDTO.setAnnualFamilyIncome(getCellString(row, headerMap, "Annual Family Income"));
                studentDTO.setCandidatureType(getCellString(row, headerMap, "Candidature Type"));
                studentDTO.setHomeUniversity(getCellString(row, headerMap, "Home University"));
                studentDTO.setCategory(getCellString(row, headerMap, "Category"));
                studentDTO.setPhType(getCellString(row, headerMap, "PH Type"));
                studentDTO.setDefenceType(getCellString(row, headerMap, "Defence Type"));
                studentDTO.setLinguisticMinority(getCellString(row, headerMap, "Linguistic Minority"));
                studentDTO.setReligiousMinority(getCellString(row, headerMap, "Religious Minority"));

                // Parent Info
                ParentDTO parentDTO = new ParentDTO();
                parentDTO.setFatherName(getCellString(row, headerMap, "Father Name"));
                parentDTO.setMotherName(getCellString(row, headerMap, "Mother Name"));
                parentDTO.setFatherMobileNo(getCellString(row, headerMap, "Phone No"));
                parentDTO.setMotherMobileNo(getCellString(row, headerMap, "Phone No"));
                studentDTO.setParent(parentDTO);

                // Address Info
                AddressDTO addressDTO = new AddressDTO();
                addressDTO.setAddressLine1(getCellString(row, headerMap, "Address Line 1"));
                addressDTO.setAddressLine2(getCellString(row, headerMap, "Address Line 2"));
                addressDTO.setAddressLine3(getCellString(row, headerMap, "Address Line 3"));
                addressDTO.setState(getCellString(row, headerMap, "State"));
                addressDTO.setDistrict(getCellString(row, headerMap, "District"));
                addressDTO.setTaluka(getCellString(row, headerMap, "Taluka"));
                addressDTO.setVillage(getCellString(row, headerMap, "Village"));
                addressDTO.setPincode(getCellString(row, headerMap, "Pincode"));
                studentDTO.setAddress(addressDTO);

                // SSC Info
                SSCDTO sscDTO = new SSCDTO();
                sscDTO.setBoard(getCellString(row, headerMap, "SSC Board"));
                sscDTO.setPassingYear(getCellString(row, headerMap, "SSC Passing Year"));
                sscDTO.setSeatNo(normaliseSeatNo(row, headerMap, "SSC Seat No"));
                sscDTO.setMathPercentage(getCellDouble(row, headerMap, "SSC Math Percentage"));
                sscDTO.setTotalPercentage(getCellDouble(row, headerMap, "SSC Total Percentage"));
                studentDTO.setSsc(sscDTO);

                // HSC Info
                HSCDTO hscDTO = new HSCDTO();
                hscDTO.setBoard(getCellString(row, headerMap, "HSC Board"));
                hscDTO.setPassingYear(getCellInteger(row, headerMap, "HSC Passing Year"));
                hscDTO.setSeatNo(getCellString(row, headerMap, "HSC Seat No"));
                hscDTO.setPhysicsPercentage(getCellDouble(row, headerMap, "HSC Physics Percentage"));
                hscDTO.setChemistryPercentage(getCellDouble(row, headerMap, "HSC Chemistry Percentage"));
                hscDTO.setMathPercentage(getCellDouble(row, headerMap, "HSC Math Percentage"));
                hscDTO.setAdditionalSubjectName(getCellString(row, headerMap, "HSC Additional Subject for Eligiblity"));
                hscDTO.setAdditionalSubjectPercentage(getCellDouble(row, headerMap, "HSC Subject Percentage"));
                hscDTO.setEnglishPercentage(getCellDouble(row, headerMap, "HSC English Percentage"));
                hscDTO.setTotalPercentage(getCellDouble(row, headerMap, "HSC Total Percentage"));
                hscDTO.setEligibilityPercentage(getCellDouble(row, headerMap, "Eligibility Percentage"));
                studentDTO.setHsc(hscDTO);

                // Build entrance exams list
                List<EntranceExamDTO> exams = new ArrayList<>();

                // CET info
                String cetRollNo = getCellString(row, headerMap, "CET Roll No");
                Double cetPercentile = getCellDouble(row, headerMap, "CET Percentile");
                if (cetRollNo != null && !cetRollNo.isBlank()) {
                    EntranceExamDTO cetExam = new EntranceExamDTO();
                    cetExam.setExamType("CET");
                    cetExam.setExamNo(cetRollNo);
                    cetExam.setPercentile(cetPercentile);
                    // studentId will be set later by service/controller
                    exams.add(cetExam);
                }

                // JEE info
                String jeeAppNo = getCellString(row, headerMap, "JEE Application No");
                Double jeePercentile = getCellDouble(row, headerMap, "JEE Percentile");
                if (jeeAppNo != null && !jeeAppNo.isBlank()) {
                    EntranceExamDTO jeeExam = new EntranceExamDTO();
                    jeeExam.setExamType("JEE");
                    jeeExam.setExamNo(jeeAppNo);
                    jeeExam.setPercentile(jeePercentile);
                    exams.add(jeeExam);
                }

                studentDTO.setEntranceExams(exams);

                // Admission Info
                Integer meritNo = getCellInteger(row, headerMap, "Merit No");
                if (meritNo != null) {
                    AdmissionDTO admissionDTO = new AdmissionDTO();
                    admissionDTO.setMeritNo(meritNo);
                    admissionDTO.setMeritMarks(getCellDouble(row, headerMap, "Merit Marks"));
                    admissionDTO.setInstituteCode(getCellString(row, headerMap, "Institute Code"));
                    admissionDTO.setInstituteName(getCellString(row, headerMap, "Institute Name"));
                    admissionDTO.setCourseName(getCellString(row, headerMap, "Course Name"));
                    admissionDTO.setChoiceCode(getCellString(row, headerMap, "Choice Code"));
                    admissionDTO.setSeatType(getCellString(row, headerMap, "Seat Type"));
                    admissionDTO.setAdmissionDate(parseDate(row, headerMap, "Admission Date"));
                    admissionDTO.setReportedDate(parseDate(row, headerMap, "Reported Date"));

                    studentDTO.setAdmissions(Collections.singletonList(admissionDTO));
                } else {
                    studentDTO.setAdmissions(new ArrayList<>());
                }

                students.add(studentDTO);
            }

            return students;

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Excel file: " + e.getMessage(), e);
        }
    }

    public static List<ExcelStudentDTO> excelToBasicStudentDTOs(InputStream is) {
        try (Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0); // First sheet

            int headerRowIndex = 3;
            Row headerRow = sheet.getRow(headerRowIndex);
            if (headerRow == null) throw new RuntimeException("Header row not found at row ");

            // ---- Build header map, normalize headers ----
            Map<String, Integer> headerMap = new HashMap<>();
            for (Cell cell : headerRow) {
                String header = normalizeCell(cell.toString());
                if (!header.isEmpty()) {
                    headerMap.put(header, cell.getColumnIndex());
                }
            }
            System.out.println("Header Map: " + headerMap);

            List<ExcelStudentDTO> students = new ArrayList<>();

            for (int i = headerRowIndex + 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                ExcelStudentDTO dto = new ExcelStudentDTO();

                // ----- Candidate Name -----
                String candidateName = normalizeCell(getCellString(row, headerMap, "Candidate Name"));

                // ----- Roll No -----
                String rollNoStr = normalizeCell(getCellString(row, headerMap, "Roll No"));
                Integer rollNo = null;
                if (rollNoStr != null && !rollNoStr.isEmpty()) {
                    try {
                        rollNo = (int) Double.parseDouble(rollNoStr); // handles numeric-like strings
                    } catch (NumberFormatException e) {
                        System.out.println("Failed to parse Roll No at row " + (i + 1) + ": " + rollNoStr);
                    }
                }

                // Skip row only if both are really missing
                if ((candidateName == null || candidateName.isEmpty()) && rollNo == null) {
                    System.out.println("Skipping row " + (i + 1) +
                            ": RollNo='" + rollNoStr + "', CandidateName='" + candidateName + "'");
                    continue;
                }

                dto.setCandidateName(candidateName);
                dto.setRollNo(rollNo);

                // ----- DOB (default if missing) -----
                String dob = normalizeCell(getCellString(row, headerMap, "DOB"));
                dto.setDob(dob != null && !dob.isEmpty() ? dob : "01/08/2003");

                // ----- Course Name -----
                dto.setCourseName(normalizeCell(getCellString(row, headerMap, "Course Name")));

                // ----- Division -----
                dto.setDivision(normalizeCell(getCellString(row, headerMap, "DIV")));

                // Debug: print row values
                System.out.println("Row " + (i + 1) + " => RollNo=" + rollNo + ", Name='" + candidateName +
                        "', Course='" + dto.getCourseName() + "', DIV='" + dto.getDivision() + "'");

                students.add(dto);
            }

            return students;

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Excel file: " + e.getMessage(), e);
        }
    }

    // ----- Helper method to normalize cell strings -----
    private static String normalizeCell(String s) {
        if (s == null) return null;
        return s.replace("\u00A0", " ")   // non-breaking space
                .replace("\u200B", "")    // zero-width space
                .trim();
    }

    // ---------------- Helper methods for name-based access ----------------

    private static Cell getCell(Row row, Map<String,Integer> headerMap, String colName) {
        Integer idx = headerMap.get(colName);
        if (idx == null) return null;
        return row.getCell(idx);
    }

    private static String getCellString(Row row, Map<String,Integer> headerMap, String colName) {
        Cell cell = getCell(row, headerMap, colName);
        if (cell == null) return null;

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    // Return as Excel's original text
                    DataFormatter formatter = new DataFormatter();
                    return formatter.formatCellValue(cell).trim();
                } else {
                    // For numeric cells like 20041021 (no scientific notation)
                    BigDecimal bd = new BigDecimal(cell.getNumericCellValue());
                    return bd.stripTrailingZeros().toPlainString();
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                DataFormatter formatter = new DataFormatter();
                return formatter.formatCellValue(cell).trim();
            case BLANK:
            case _NONE:
            case ERROR:
            default:
                return null;
        }
    }

    private static String getDobFormatted(Row row, Map<String, Integer> headerMap, String colName) {
        Cell cell = getCell(row, headerMap, colName);
        if (cell == null) return null;

        if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            LocalDate date = cell.getLocalDateTimeCellValue().toLocalDate();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return date.format(formatter);
        } else {
            // If someone enters it as text in Excel
            String text = cell.toString().trim();
            if (text.isEmpty()) return null;

            try {
                // Try parsing 2/1/05 as date
                DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("M/d/yy");
                LocalDate date = LocalDate.parse(text, inputFormatter);
                DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                return date.format(outputFormatter);
            } catch (Exception e) {
                return text; // fallback: keep original text
            }
        }
    }




    private static Integer getCellInteger(Row row, Map<String,Integer> headerMap, String colName) {
        Cell cell = getCell(row, headerMap, colName);
        if (cell == null) return null;
        if (cell.getCellType() == CellType.NUMERIC) return (int) cell.getNumericCellValue();
        try {
            return Integer.parseInt(cell.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static Double getCellDouble(Row row, Map<String,Integer> headerMap, String colName) {
        Cell cell = getCell(row, headerMap, colName);
        if (cell == null) return null;
        if (cell.getCellType() == CellType.NUMERIC) return cell.getNumericCellValue();
        try {
            return Double.parseDouble(cell.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static LocalDate parseDate(Row row, Map<String,Integer> headerMap, String colName) {
        Cell cell = getCell(row, headerMap, colName);
        if (cell == null) return null;
        try {
            if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
                return cell.getLocalDateTimeCellValue().toLocalDate();
            }
            String text = cell.toString().trim();
            if (text.isEmpty()) return null;
            return LocalDate.parse(text, DATE_FORMATTER);
        } catch (Exception e) {
            return null;
        }
    }

    private static String normaliseSeatNo(Row row, Map<String,Integer> headerMap, String colName) {
        Cell cell = getCell(row, headerMap, colName);
        if (cell == null) return null;
        if (cell.getCellType() == CellType.NUMERIC) {
            BigDecimal bd = new BigDecimal(cell.getNumericCellValue());
            return bd.stripTrailingZeros().toPlainString();
        }
        String value = cell.toString().trim();
        return value.isEmpty() ? null : value;
    }

}
