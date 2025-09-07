package in.edu.jspmjscoe.admissionportal.helper;


import in.edu.jspmjscoe.admissionportal.dtos.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ExcelHelper {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("M/d/yyyy");

    public static List<StudentDTO> excelToStudentDTOs(InputStream is) {
        try (Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(4);
            List<StudentDTO> students = new ArrayList<>();

            int firstDataRow = 3; // skip headers
            for (int i = firstDataRow; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                StudentDTO studentDTO = new StudentDTO();

                // ------------------- Basic Info -------------------
                studentDTO.setApplicationId(getCellString(row.getCell(1)));
                studentDTO.setCandidateName(getCellString(row.getCell(2)));
                studentDTO.setMobileNo(getCellString(row.getCell(19)));
                studentDTO.setEmail(getCellString(row.getCell(20)));
                studentDTO.setGender(getCellString(row.getCell(5)));
                studentDTO.setDob(getCellString(row.getCell(6)));
                studentDTO.setReligion(getCellString(row.getCell(7)));
                studentDTO.setRegion(getCellString(row.getCell(8)));
                studentDTO.setMotherTongue(getCellString(row.getCell(9)));
                studentDTO.setAnnualFamilyIncome(getCellString(row.getCell(10)));
                studentDTO.setCandidatureType(getCellString(row.getCell(22)));
                studentDTO.setHomeUniversity(getCellString(row.getCell(23)));
                studentDTO.setCategory(getCellString(row.getCell(24)));
                studentDTO.setPhType(getCellString(row.getCell(25)));
                studentDTO.setDefenceType(getCellString(row.getCell(26)));
                studentDTO.setLinguisticMinority(getCellString(row.getCell(27)));
                studentDTO.setReligiousMinority(getCellString(row.getCell(28)));

                // ------------------- Parent Info -------------------
                ParentDTO parentDTO = new ParentDTO();
                parentDTO.setFatherName(getCellString(row.getCell(3)));
                parentDTO.setMotherName(getCellString(row.getCell(4)));
                parentDTO.setFatherMobileNo(getCellString(row.getCell(21)));
                parentDTO.setMotherMobileNo(getCellString(row.getCell(21)));
                studentDTO.setParent(parentDTO);

                // ------------------- Address Info -------------------
                AddressDTO addressDTO = new AddressDTO();
                addressDTO.setAddressLine1(getCellString(row.getCell(11)));
                addressDTO.setAddressLine2(getCellString(row.getCell(12)));
                addressDTO.setAddressLine3(getCellString(row.getCell(13)));
                addressDTO.setState(getCellString(row.getCell(14)));
                addressDTO.setDistrict(getCellString(row.getCell(15)));
                addressDTO.setTaluka(getCellString(row.getCell(16)));
                addressDTO.setVillage(getCellString(row.getCell(17)));
                addressDTO.setPincode(getCellString(row.getCell(18)));
                studentDTO.setAddress(addressDTO);

                // ------------------- SSC Info -------------------
                SSCDTO sscDTO = new SSCDTO();
                sscDTO.setBoard(getCellString(row.getCell(29)));
                sscDTO.setPassingYear(getCellString(row.getCell(30)));
                sscDTO.setSeatNo(normaliseSeatNo(row.getCell(31)));
                sscDTO.setMathPercentage(getCellDouble(row.getCell(32)));
                sscDTO.setTotalPercentage(getCellDouble(row.getCell(33)));
                studentDTO.setSsc(sscDTO);

                // ------------------- HSC Info -------------------
                HSCDTO hscDTO = new HSCDTO();
                hscDTO.setBoard(getCellString(row.getCell(35)));
                hscDTO.setPassingYear(getCellInteger(row.getCell(36)));
                hscDTO.setSeatNo(getCellString(row.getCell(37)));
                hscDTO.setPhysicsPercentage(getCellDouble(row.getCell(38)));
                hscDTO.setChemistryPercentage(getCellDouble(row.getCell(39)));
                hscDTO.setMathPercentage(getCellDouble(row.getCell(40)));
                hscDTO.setAdditionalSubjectName(getCellString(row.getCell(41)));
                hscDTO.setAdditionalSubjectPercentage(getCellDouble(row.getCell(42)));
                hscDTO.setEnglishPercentage(getCellDouble(row.getCell(43)));
                hscDTO.setTotalPercentage(getCellDouble(row.getCell(44)));
                hscDTO.setEligibilityPercentage(getCellDouble(row.getCell(45)));
                studentDTO.setHsc(hscDTO);

                // ------------------- CET Info -------------------
                CETDTO cetDTO = new CETDTO();
                cetDTO.setRollNo(getCellString(row.getCell(46)));
                cetDTO.setPercentile(getCellDouble(row.getCell(47)));
                studentDTO.setCet(cetDTO);

                // ------------------- JEE Info -------------------
                String jeeAppNo = getCellString(row.getCell(48));
                Double jeePercentile = getCellDouble(row.getCell(49));
                if (jeeAppNo != null && !jeeAppNo.isBlank()) {
                    JEEDTO jeeDTO = new JEEDTO();
                    jeeDTO.setApplicationNo(jeeAppNo);
                    jeeDTO.setPercentile(jeePercentile);
                    studentDTO.setJee(jeeDTO);
                } else {
                    studentDTO.setJee(null); // no JEE data
                }

                // ------------------- Admission Info -------------------
                Integer meritNo = getCellInteger(row.getCell(50));
                String instituteCode = getCellString(row.getCell(52));
                String courseName = getCellString(row.getCell(54));

                if (meritNo != null || instituteCode != null || courseName != null) {

                    AdmissionDTO admissionDTO = new AdmissionDTO();
                    admissionDTO.setMeritNo(meritNo);
                    admissionDTO.setMeritMarks(getCellDouble(row.getCell(51)));
                    admissionDTO.setInstituteCode(instituteCode);
                    admissionDTO.setInstituteName(getCellString(row.getCell(53)));
                    admissionDTO.setCourseName(courseName);
                    admissionDTO.setChoiceCode(getCellString(row.getCell(55)));
                    admissionDTO.setSeatType(getCellString(row.getCell(56)));
                    admissionDTO.setAdmissionDate(parseDate(row.getCell(57)));
                    admissionDTO.setReportedDate(parseDate(row.getCell(58)));

                    List<AdmissionDTO> admissions = new ArrayList<>();
                    admissions.add(admissionDTO);
                    studentDTO.setAdmissions(admissions);
                    for (StudentDTO dto : students) {
                        System.out.println(dto);
                    }
                } else {
                    studentDTO.setAdmissions(new ArrayList<>()); // empty list if no admission data
                }

                students.add(studentDTO);
            }

            return students;

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Excel file: " + e.getMessage(), e);
        }
    }

    // ------------------- Helper Methods -------------------

    private static String getCellString(Cell cell) {
        if (cell == null) return null;
        String value = cell.toString().trim();
        return value.isEmpty() ? null : value;
    }

    private static Integer getCellInteger(Cell cell) {
        if (cell == null) return null;
        if (cell.getCellType() == CellType.NUMERIC) return (int) cell.getNumericCellValue();
        try {
            return Integer.parseInt(cell.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static Double getCellDouble(Cell cell) {
        if (cell == null) return null;
        if (cell.getCellType() == CellType.NUMERIC) return cell.getNumericCellValue();
        try {
            return Double.parseDouble(cell.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static LocalDate parseDate(Cell cell) {
        if (cell == null) return null;

        try {
            if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
                return cell.getLocalDateTimeCellValue().toLocalDate();
            }
            String text = cell.toString().trim();
            if (text.isEmpty()) return null;
            return LocalDate.parse(text, DATE_FORMATTER);
        } catch (Exception e) {
            System.out.println("Could not parse date for cell: " + cell.toString());
            return null;
        }
    }


    private static String normaliseSeatNo(Cell cell) {
        if (cell == null) return null;
        // this removes scientific notation and trailing .0
        if (cell.getCellType() == CellType.NUMERIC) {
            BigDecimal bd = new BigDecimal(cell.getNumericCellValue());
            return bd.stripTrailingZeros().toPlainString();
        }
        String value = cell.toString().trim();
        return value.isEmpty() ? null : value;
    }

}
