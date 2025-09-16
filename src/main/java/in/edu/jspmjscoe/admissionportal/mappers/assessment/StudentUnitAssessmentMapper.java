package in.edu.jspmjscoe.admissionportal.mappers.assessment;

import in.edu.jspmjscoe.admissionportal.dtos.assessment.StudentUnitAssessmentDTO;
import in.edu.jspmjscoe.admissionportal.model.assessment.StudentUnitAssessment;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface StudentUnitAssessmentMapper {

    @Mapping(source = "student.studentId", target = "studentId")
    @Mapping(source = "subject.subjectId", target = "subjectId")
    StudentUnitAssessmentDTO toDTO(StudentUnitAssessment entity);

    @InheritInverseConfiguration
    @Mapping(target = "student", ignore = true)
    @Mapping(target = "subject", ignore = true)
    StudentUnitAssessment toEntity(StudentUnitAssessmentDTO dto);
}
