package in.edu.jspmjscoe.admissionportal.dtos.assessment;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamUpdateRequestDTO {
    private Long examId;       // Existing exam record ID
    private Double marksObtained; // New marks to update
}
