package in.edu.jspmjscoe.admissionportal.dtos.assessment;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubjectAssessmentDTO {

    private Long subjectAssessmentId;

    private Long subjectId;

    private Long assessmentTypeId;

    private Integer unitNo;   // optional

    private String title;

    private Integer maxMarks;

    private BigDecimal weightage;  // percentage for final grade

    private LocalDate assessmentDate;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
