package in.edu.jspmjscoe.admissionportal.mappers.teacher;

import in.edu.jspmjscoe.admissionportal.dtos.teacher.LeaveDTO;
import in.edu.jspmjscoe.admissionportal.model.teacher.Leave;
import in.edu.jspmjscoe.admissionportal.model.teacher.Teacher;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface LeaveMapper {

    // DTO → Entity using teacherId
    @Mapping(target = "teacher", source = "teacherId", qualifiedByName = "teacherFromId")
    @Mapping(target = "status", ignore = true) // ignore status, set manually in service
    Leave toEntity(LeaveDTO dto);

    // DTO → Entity when Teacher object already available
    @Mapping(target = "status", ignore = true) // ignore status
    Leave toEntity(LeaveDTO dto, Teacher teacher);

    // Entity → DTO
    @Mapping(target = "teacherId", source = "teacher.teacherId")
    @Mapping(
            target = "teacherName",
            expression = "java(leave.getTeacher() != null ? leave.getTeacher().getFirstName() + \" \" + leave.getTeacher().getLastName() : null)"
    )
    @Mapping(
            target = "departmentName",
            expression = "java(leave.getTeacher() != null && leave.getTeacher().getDepartment() != null ? leave.getTeacher().getDepartment().getName() : null)"
    )
    LeaveDTO toDTO(Leave leave);

    // Helper method to create Teacher object from teacherId
    @Named("teacherFromId")
    default Teacher teacherFromId(Long teacherId) {
        if (teacherId == null) return null;
        Teacher teacher = new Teacher();
        teacher.setTeacherId(teacherId);
        return teacher;
    }
}
