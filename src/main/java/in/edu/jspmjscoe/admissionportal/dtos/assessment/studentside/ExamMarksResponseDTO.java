package in.edu.jspmjscoe.admissionportal.dtos.assessment.studentside;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// For response only
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamMarksResponseDTO {
    private String examType;
    private Double marksObtained;
}
