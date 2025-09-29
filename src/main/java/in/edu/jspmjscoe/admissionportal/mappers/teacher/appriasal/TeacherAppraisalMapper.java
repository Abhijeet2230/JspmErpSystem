package in.edu.jspmjscoe.admissionportal.mappers.teacher.appriasal;

import in.edu.jspmjscoe.admissionportal.dtos.teacher.appriasal.TeacherAppraisalDTO;
import in.edu.jspmjscoe.admissionportal.model.teacher.Teacher;
import in.edu.jspmjscoe.admissionportal.model.teacher.appriasal.TeacherAppraisal;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TeacherAppraisalMapper {

    // DTO → Entity using teacherId
    @Mapping(target = "teacher", source = "teacherId", qualifiedByName = "teacherFromId")
    TeacherAppraisal toEntity(TeacherAppraisalDTO dto);

    // Entity → DTO
    @Mapping(target = "teacherId", source = "teacher.teacherId")
    @Mapping(
            target = "teacherName",
            expression = "java(appraisal.getTeacher() != null ? appraisal.getTeacher().getFirstName() + \" \" + appraisal.getTeacher().getLastName() : null)"
    )
    @Mapping(
            target = "departmentName",
            expression = "java(appraisal.getTeacher() != null && appraisal.getTeacher().getDepartment() != null ? appraisal.getTeacher().getDepartment().getName() : null)"
    )
    TeacherAppraisalDTO toDTO(TeacherAppraisal appraisal);

    // Helper method to create Teacher object from teacherId
    @Named("teacherFromId")
    default Teacher teacherFromId(Long teacherId) {
        if (teacherId == null) return null;
        Teacher teacher = new Teacher();
        teacher.setTeacherId(teacherId);
        return teacher;
    }
}
