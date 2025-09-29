package in.edu.jspmjscoe.admissionportal.dtos.assessment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentWithUnitsDTO {

    private Long studentAcademicYearId;  // replaces studentId
    private String candidateName;        // optional: from StudentAcademicYear.student
    private Integer rollNo;              // optional: from StudentAcademicYear
    private List<UnitMarksDTO> units;    // list of 5 units
}
