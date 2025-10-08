package in.edu.jspmjscoe.admissionportal.dtos.teacher.attendance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for fetching students for attendance
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentAttendanceDTO {

    private Long studentId;           // Student primary key
    private String candidateName;     // Full name or candidate_name field
    private String courseName;        // Course name (resolved from Student → Course)
    private String departmentName;    // Department name (resolved from Course → Department)
    private Integer semester;         // Semester from StudentAcademicYear
    private String division;          // Division from StudentAcademicYear
    private Integer rollNo;           // Roll number in division
}
