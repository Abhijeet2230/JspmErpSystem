package in.edu.jspmjscoe.admissionportal.dtos.assessment;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentUnitAssessmentDTO {

    private Long id;

    private Long studentId;   // only ID, not full Student entity
    private Long subjectId;   // only ID, not full Subject entity

    private Integer unitNumber;
    private Double quizMarks;
    private Double activityMarks;
}
