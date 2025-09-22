package in.edu.jspmjscoe.admissionportal.mappers.assessment;

import in.edu.jspmjscoe.admissionportal.dtos.assessment.AttendanceDTO;
import in.edu.jspmjscoe.admissionportal.model.assessment.Attendance;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AttendanceMapper {

    // ---------- Entity -> DTO ----------
    @Mapping(source = "student.studentId", target = "studentId")
    @Mapping(source = "subject.subjectId", target = "subjectId")
    AttendanceDTO toDto(Attendance entity);

    // ---------- DTO -> Entity ----------
    @Mapping(source = "studentId", target = "student.studentId")
    @Mapping(source = "subjectId", target = "subject.subjectId")
    Attendance toEntity(AttendanceDTO dto);

    // ---------- Update from DTO (null-safe) ----------
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(AttendanceDTO dto, @MappingTarget Attendance entity);
}