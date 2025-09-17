package in.edu.jspmjscoe.admissionportal.dtos.assessment.studentside;

import in.edu.jspmjscoe.admissionportal.dtos.assessment.UnitMarksDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubjectCCERecordResponseDTO {
    private String subjectName;
    private List<UnitMarksDTO> units;
    private ExamMarksResponseDTO midTerm;
    private ExamMarksResponseDTO endTerm;
    private Double attendance;
}
