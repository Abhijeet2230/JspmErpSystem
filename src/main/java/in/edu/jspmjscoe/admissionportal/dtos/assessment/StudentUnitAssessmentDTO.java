package in.edu.jspmjscoe.admissionportal.dtos.assessment;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentUnitAssessmentDTO {

    private Long studentUnitAssessmentId;
    private Long subjectId;   // only ID, not full Subject entity
    private Integer rollNo;
    private String candidateName;
    private Integer unitNumber;
    private Double quizMarks;
    private Double activityMarks;
}
