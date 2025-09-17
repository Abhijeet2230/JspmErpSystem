package in.edu.jspmjscoe.admissionportal.dtos.assessment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentCCEDTO {
    private Long studentId;
    private String candidateName;
    private Integer rollNo;
    private String division;
}
