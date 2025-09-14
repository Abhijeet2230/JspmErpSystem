package in.edu.jspmjscoe.admissionportal.mappers.teacher;

import in.edu.jspmjscoe.admissionportal.dtos.teacher.TeacherDTO;
import in.edu.jspmjscoe.admissionportal.model.subject.Department;
import in.edu.jspmjscoe.admissionportal.model.teacher.Teacher;
import in.edu.jspmjscoe.admissionportal.model.teacher.TeacherSubject;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface TeacherMapper {

    // ===== Entity -> DTO =====
    @Mapping(source = "department.departmentId", target = "departmentId")
    @Mapping(source = "teacherSubjects", target = "teacherSubjectIds")
    TeacherDTO toDto(Teacher teacher);

    // ===== DTO -> Entity =====
    @InheritInverseConfiguration
    @Mapping(target = "department", expression = "java(mapDepartment(dto.getDepartmentId()))")
    @Mapping(target = "teacherSubjects", expression = "java(mapTeacherSubjectsFromIds(dto.getTeacherSubjectIds()))")
    Teacher toEntity(TeacherDTO dto);

    // ---------- Helper methods ----------
    default Department mapDepartment(Long departmentId) {
        if (departmentId == null) return null;
        Department d = new Department();
        d.setDepartmentId(departmentId);
        return d;
    }

    default List<Long> mapTeacherSubjectsToIds(List<TeacherSubject> teacherSubjects) {
        if (teacherSubjects == null) return null;
        return teacherSubjects.stream()
                .map(TeacherSubject::getTeacherSubjectId)
                .collect(Collectors.toList());
    }

    default List<TeacherSubject> mapTeacherSubjectsFromIds(List<Long> teacherSubjectIds) {
        if (teacherSubjectIds == null) return null;
        return teacherSubjectIds.stream()
                .map(id -> {
                    TeacherSubject ts = new TeacherSubject();
                    ts.setTeacherSubjectId(id);
                    return ts;
                })
                .collect(Collectors.toList());
    }
}
