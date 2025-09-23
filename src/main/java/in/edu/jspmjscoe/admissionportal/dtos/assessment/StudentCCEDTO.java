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

    private Long studentAcademicYearId;  // replaces studentId
    private String candidateName;        // optional: from StudentAcademicYear.student
    private Integer rollNo;              // optional: from StudentAcademicYear
    private String division;             // optional: from StudentAcademicYear
}
