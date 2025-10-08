package in.edu.jspmjscoe.admissionportal.dtos.assessment;

import lombok.*;

/**
 * DTO for representing a student's overall attendance in a single subject (not monthly, total overall).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminStudentOverallSubjectAttendanceDTO {

    private Long studentId;
    private String studentName;
    private String rollNo;
    private String division;
    private String subjectName;   // e.g. "Engineering Mathematics-I"
    private Double percentage;    // Overall attendance percentage (0.0–100.0)
}
