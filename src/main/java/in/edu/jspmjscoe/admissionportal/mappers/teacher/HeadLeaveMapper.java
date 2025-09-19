package in.edu.jspmjscoe.admissionportal.mappers.teacher;

import in.edu.jspmjscoe.admissionportal.dtos.teacher.HeadLeaveDTO;
import in.edu.jspmjscoe.admissionportal.model.teacher.HeadLeave;
import in.edu.jspmjscoe.admissionportal.model.teacher.Teacher;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface HeadLeaveMapper {

    // DTO → Entity using teacherId
    @Mapping(target = "teacher", source = "teacherId", qualifiedByName = "teacherFromId")
    @Mapping(target = "status", ignore = true) // set manually in service
    HeadLeave toEntity(HeadLeaveDTO dto);

    // DTO → Entity when Teacher object already available
    @Mapping(target = "status", ignore = true)
    HeadLeave toEntity(HeadLeaveDTO dto, Teacher teacher);

    // Entity → DTO
    @Mapping(target = "teacherId", source = "teacher.teacherId")
    @Mapping(
            target = "teacherName",
            expression = "java(headLeave.getTeacher() != null ? headLeave.getTeacher().getFirstName() + \" \" + headLeave.getTeacher().getLastName() : null)"
    )
    @Mapping(
            target = "departmentName",
            expression = "java(headLeave.getTeacher() != null && headLeave.getTeacher().getDepartment() != null ? headLeave.getTeacher().getDepartment().getName() : null)"
    )
    HeadLeaveDTO toDTO(HeadLeave headLeave);

    // Helper to build Teacher from teacherId
    @Named("teacherFromId")
    default Teacher teacherFromId(Long teacherId) {
        if (teacherId == null) return null;
        Teacher teacher = new Teacher();
        teacher.setTeacherId(teacherId);
        return teacher;
    }
}
