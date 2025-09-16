package in.edu.jspmjscoe.admissionportal.mappers.assessment;

import in.edu.jspmjscoe.admissionportal.dtos.assessment.AttendanceDTO;
import in.edu.jspmjscoe.admissionportal.model.assessment.Attendance;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AttendanceMapper {

    @Mapping(source = "student.studentId", target = "studentId")
    @Mapping(source = "subject.subjectId", target = "subjectId")
    AttendanceDTO toDTO(Attendance entity);

    @InheritInverseConfiguration
    @Mapping(target = "student", ignore = true)
    @Mapping(target = "subject", ignore = true)
    Attendance toEntity(AttendanceDTO dto);
}
