package in.edu.jspmjscoe.admissionportal.dtos.assessment;

import lombok.Data;

import java.util.List;

@Data
public class SubjectCCERecordDTO {
    private String subjectName;
    private List<UnitMarksDTO> units;
    private ExamUpdateRequestDTO midTerm;
    private ExamUpdateRequestDTO endTerm;
}