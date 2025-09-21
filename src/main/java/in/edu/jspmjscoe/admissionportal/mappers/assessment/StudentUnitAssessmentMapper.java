package in.edu.jspmjscoe.admissionportal.mappers.assessment;

import in.edu.jspmjscoe.admissionportal.dtos.assessment.StudentUnitAssessmentDTO;
import in.edu.jspmjscoe.admissionportal.model.assessment.StudentUnitAssessment;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface StudentUnitAssessmentMapper {

    // ---------- Entity -> DTO ----------
    @Mapping(source = "student.studentId", target = "studentId")
    @Mapping(source = "student.rollNo", target = "rollNo")
    @Mapping(source = "student.candidateName", target = "candidateName")
    @Mapping(source = "subject.subjectId", target = "subjectId")
    StudentUnitAssessmentDTO toDto(StudentUnitAssessment entity);

    // ---------- DTO -> Entity ----------
    @Mapping(source = "studentId", target = "student.studentId")
    @Mapping(source = "subjectId", target = "subject.subjectId")
    StudentUnitAssessment toEntity(StudentUnitAssessmentDTO dto);

    // ---------- Update from DTO (null-safe) ----------
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(StudentUnitAssessmentDTO dto, @MappingTarget StudentUnitAssessment entity);
}
