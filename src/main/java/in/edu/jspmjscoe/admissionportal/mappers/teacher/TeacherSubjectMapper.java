package in.edu.jspmjscoe.admissionportal.mappers.teacher;

import in.edu.jspmjscoe.admissionportal.dtos.teacher.TeacherSubjectDto;
import in.edu.jspmjscoe.admissionportal.model.subject.Subject;
import in.edu.jspmjscoe.admissionportal.model.teacher.Teacher;
import in.edu.jspmjscoe.admissionportal.model.teacher.TeacherSubject;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TeacherSubjectMapper {

    // ===== Entity -> DTO =====
    @Mapping(source = "teacher.teacherId", target = "teacherId")
    @Mapping(source = "subject.subjectId", target = "subjectId")
    TeacherSubjectDto toDto(TeacherSubject teacherSubject);

    // ===== DTO -> Entity =====
    @InheritInverseConfiguration
    @Mapping(target = "teacher", expression = "java(mapTeacher(dto.getTeacherId()))")
    @Mapping(target = "subject", expression = "java(mapSubject(dto.getSubjectId()))")
    TeacherSubject toEntity(TeacherSubjectDto dto);

    // ---------- Helper methods ----------
    default Teacher mapTeacher(Long teacherId) {
        if (teacherId == null) return null;
        Teacher t = new Teacher();
        t.setTeacherId(teacherId);
        return t;
    }

    default Subject mapSubject(Long subjectId) {
        if (subjectId == null) return null;
        Subject s = new Subject();
        s.setSubjectId(subjectId);
        return s;
    }
}
