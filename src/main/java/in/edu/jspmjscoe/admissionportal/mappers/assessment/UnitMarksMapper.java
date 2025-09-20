package in.edu.jspmjscoe.admissionportal.mappers.assessment;

import in.edu.jspmjscoe.admissionportal.dtos.assessment.UnitMarksDTO;
import in.edu.jspmjscoe.admissionportal.model.assessment.StudentUnitAssessment;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UnitMarksMapper {

    @Mapping(source = "unitNumber", target = "unitNumber")
    @Mapping(source = "quizMarks", target = "quizMarks")
    @Mapping(source = "activityMarks", target = "activityMarks")
    UnitMarksDTO toDto(StudentUnitAssessment entity);

    @InheritInverseConfiguration
    StudentUnitAssessment toEntity(UnitMarksDTO dto);
}
