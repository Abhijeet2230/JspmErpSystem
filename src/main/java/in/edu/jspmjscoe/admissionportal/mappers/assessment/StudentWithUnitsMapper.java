package in.edu.jspmjscoe.admissionportal.mappers.assessment;

import in.edu.jspmjscoe.admissionportal.dtos.assessment.StudentWithUnitsDTO;
import in.edu.jspmjscoe.admissionportal.model.student.Student;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {UnitMarksMapper.class})
public interface StudentWithUnitsMapper {

    // Entity -> DTO (units will be injected separately in service)
    @Mapping(source = "studentId", target = "studentId")
    @Mapping(source = "candidateName", target = "candidateName")
    @Mapping(source = "rollNo", target = "rollNo")
    StudentWithUnitsDTO toDto(Student student);
}
