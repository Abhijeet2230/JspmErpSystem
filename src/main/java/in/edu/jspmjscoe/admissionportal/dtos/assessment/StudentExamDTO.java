package in.edu.jspmjscoe.admissionportal.dtos.assessment;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentExamDTO {

    private Long id;

    private Long studentId;   // only ID, not full Student entity
    private Long subjectId;   // only ID, not full Subject entity

    private String examType;  // "MID_TERM" or "END_TERM"

    private Double marksObtained;
}
