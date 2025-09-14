package in.edu.jspmjscoe.admissionportal.mappers.subject;

import in.edu.jspmjscoe.admissionportal.dtos.subject.DepartmentDTO;
import in.edu.jspmjscoe.admissionportal.model.subject.Course;
import in.edu.jspmjscoe.admissionportal.model.subject.Department;
import in.edu.jspmjscoe.admissionportal.model.teacher.Teacher;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {

    // ===== Entity -> DTO =====
    @Mapping(source = "courses", target = "courseIds")
    @Mapping(source = "teachers", target = "teacherIds")
    DepartmentDTO toDto(Department department);

    // ===== DTO -> Entity =====
    @InheritInverseConfiguration
    @Mapping(target = "courses", expression = "java(mapCoursesFromIds(dto.getCourseIds()))")
    @Mapping(target = "teachers", expression = "java(mapTeachersFromIds(dto.getTeacherIds()))")
    Department toEntity(DepartmentDTO dto);

    // ---------- Helper methods ----------
    default List<Long> mapCoursesToIds(List<Course> courses) {
        if (courses == null) return null;
        return courses.stream()
                .map(Course::getCourseId)
                .collect(Collectors.toList());
    }

    default List<Course> mapCoursesFromIds(List<Long> courseIds) {
        if (courseIds == null) return null;
        return courseIds.stream()
                .map(id -> {
                    Course c = new Course();
                    c.setCourseId(id);
                    return c;
                })
                .collect(Collectors.toList());
    }

    default List<Long> mapTeachersToIds(List<Teacher> teachers) {
        if (teachers == null) return null;
        return teachers.stream()
                .map(Teacher::getTeacherId)
                .collect(Collectors.toList());
    }

    default List<Teacher> mapTeachersFromIds(List<Long> teacherIds) {
        if (teacherIds == null) return null;
        return teacherIds.stream()
                .map(id -> {
                    Teacher t = new Teacher();
                    t.setTeacherId(id);
                    return t;
                })
                .collect(Collectors.toList());
    }
}
