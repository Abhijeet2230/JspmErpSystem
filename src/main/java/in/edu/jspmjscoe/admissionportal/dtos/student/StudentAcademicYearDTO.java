package in.edu.jspmjscoe.admissionportal.dtos.student;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentAcademicYearDTO {

    private Long studentAcademicYearId;
    private Long studentId;         // Reference to Student

    private Integer yearOfStudy;    // 1 = FE, 2 = SE, etc.
    private Integer semester;       // 1..8
    private String batchYear;      // Admission batch or academic year
    private String division;        // A, B, C, etc.
    private Integer rollNo;         // Roll number in division

    private LocalDate semesterStartDate;
    private LocalDate semesterEndDate;

    private Boolean isActive;       // Current semester or not
}
