package in.edu.jspmjscoe.admissionportal.mappers.assessment;

import in.edu.jspmjscoe.admissionportal.dtos.assessment.StudentWithUnitsDTO;
import in.edu.jspmjscoe.admissionportal.model.student.StudentAcademicYear;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {UnitMarksMapper.class})
public interface StudentWithUnitsMapper {

    // Entity -> DTO (units will be injected separately in service)
    @Mapping(source = "studentAcademicYearId", target = "studentAcademicYearId")
    @Mapping(source = "student.candidateName", target = "candidateName")
    @Mapping(source = "rollNo", target = "rollNo")
    StudentWithUnitsDTO toDto(StudentAcademicYear studentAcademicYear);
}
