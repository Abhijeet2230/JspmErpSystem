package in.edu.jspmjscoe.admissionportal.dtos.assessment;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentExamDTO {

    private Long id;

    private Long studentId;
    private Long subjectId;

    private String examType;  // "MID_TERM" or "END_TERM"

    private Double marksObtained;
}
