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

    private Long studentAcademicYearId;           // replaces studentId
    private String candidateName;                 // from StudentAcademicYear.student
    private Integer rollNo;                       // from StudentAcademicYear
    private String division;                      // from StudentAcademicYear
    private List<SubjectCCERecordResponseDTO> subjects; // CCE data for subjects in this semester
}
