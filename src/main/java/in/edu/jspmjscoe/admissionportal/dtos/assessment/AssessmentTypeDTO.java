package in.edu.jspmjscoe.admissionportal.dtos.assessment;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssessmentTypeDTO {
    private Long assessmentTypeId;
    private String name;
    private String category;
    private String description;

    // Instead of full SubjectAssessment objects, just their IDs
    private List<Long> subjectAssessmentIds;
}
