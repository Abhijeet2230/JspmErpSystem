package in.edu.jspmjscoe.admissionportal.mappers.assessment;

import in.edu.jspmjscoe.admissionportal.dtos.assessment.StudentUnitAssessmentDTO;
import in.edu.jspmjscoe.admissionportal.model.assessment.StudentUnitAssessment;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface StudentUnitAssessmentMapper {

    @Mapping(source = "studentUnitAssessmentId", target = "studentUnitAssessmentId")
    @Mapping(source = "studentAcademicYear.rollNo", target = "rollNo")
    @Mapping(source = "studentAcademicYear.student.candidateName", target = "candidateName")
    @Mapping(source = "subject.subjectId", target = "subjectId")
    StudentUnitAssessmentDTO toDTO(StudentUnitAssessment entity);

    @Mapping(source = "studentUnitAssessmentId", target = "studentUnitAssessmentId")
    @Mapping(target = "studentAcademicYear", ignore = true) // pass via service
    @Mapping(target = "subject", ignore = true)            // pass via service
    StudentUnitAssessment toEntity(StudentUnitAssessmentDTO dto);
}
