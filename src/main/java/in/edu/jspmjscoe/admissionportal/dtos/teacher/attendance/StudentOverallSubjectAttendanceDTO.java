package in.edu.jspmjscoe.admissionportal.dtos.teacher.attendance;

import lombok.*;

/**
 * DTO representing a student's overall attendance per subject.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentOverallSubjectAttendanceDTO {

    private Long subjectId;
    private String subjectName;
    private Double percentage;  // Overall attendance percentage for that subject (0.0–100.0)
}
