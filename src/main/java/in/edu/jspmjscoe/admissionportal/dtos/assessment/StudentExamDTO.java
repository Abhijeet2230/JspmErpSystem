package in.edu.jspmjscoe.admissionportal.dtos.assessment;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentExamDTO {

    private Long studentExamId;           // maps to entity studentExamId
    private Long studentAcademicYearId;   // replaces studentId
    private Long subjectId;               // only ID, not full Subject entity
    private String examType;              // "MID_TERM" or "END_TERM"
    private Double marksObtained;
}
