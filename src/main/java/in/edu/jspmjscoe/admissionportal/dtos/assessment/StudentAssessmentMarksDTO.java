package in.edu.jspmjscoe.admissionportal.dtos.assessment;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentAssessmentMarksDTO {

    private Long studentAssessmentId;

    private Long studentId;

    private Long subjectAssessmentId;

    private BigDecimal marksObtained;

    private String status; // Submitted, Absent, Late

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
