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
    private Long studentId;
    private String candidateName;
    private Integer rollNo;
    private List<UnitMarksDTO> units; // list of 5 units
}