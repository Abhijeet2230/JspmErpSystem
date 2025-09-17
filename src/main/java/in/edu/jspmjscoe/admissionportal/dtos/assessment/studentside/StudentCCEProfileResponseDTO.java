package in.edu.jspmjscoe.admissionportal.dtos.assessment.studentside;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentCCEProfileResponseDTO {
    private Long studentId;
    private String candidateName;
    private Integer rollNo;
    private String division;
    private List<SubjectCCERecordResponseDTO> subjects;
}
