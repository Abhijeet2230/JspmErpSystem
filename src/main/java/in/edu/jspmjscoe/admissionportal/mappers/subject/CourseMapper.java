package in.edu.jspmjscoe.admissionportal.mappers.subject;

import in.edu.jspmjscoe.admissionportal.dtos.subject.CourseDTO;
import in.edu.jspmjscoe.admissionportal.model.student.Student;
import in.edu.jspmjscoe.admissionportal.model.subject.Course;
import in.edu.jspmjscoe.admissionportal.model.subject.Department;
import in.edu.jspmjscoe.admissionportal.model.subject.Subject;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    @Mapping(source = "department.departmentId", target = "departmentId")
    @Mapping(source = "students", target = "studentIds")
    @Mapping(source = "subjects", target = "subjectIds")
    CourseDTO toDto(Course course);

    @InheritInverseConfiguration
    @Mapping(target = "department", expression = "java(mapDepartment(dto.getDepartmentId()))")
    @Mapping(target = "students", expression = "java(mapStudentsFromIds(dto.getStudentIds()))")
    @Mapping(target = "subjects", expression = "java(mapSubjectsFromIds(dto.getSubjectIds()))")
    Course toEntity(CourseDTO dto);

    // ===== Helper methods =====
    default Department mapDepartment(Long departmentId) {
        if (departmentId == null) return null;
        Department dept = new Department();
        dept.setDepartmentId(departmentId);
        return dept;
    }

    // ---------- Students ----------
    default List<Long> mapStudentsToIds(List<Student> students) {
        if (students == null) return null;
        return students.stream()
                .map(Student::getStudentId)
                .collect(Collectors.toList());
    }

    default List<Student> mapStudentsFromIds(List<Long> studentIds) {
        if (studentIds == null) return null;
        return studentIds.stream()
                .map(id -> {
                    Student s = new Student();
                    s.setStudentId(id);
                    return s;
                })
                .collect(Collectors.toList());
    }

    // ---------- Subjects ----------
    default List<Long> mapSubjectsToIds(List<Subject> subjects) {
        if (subjects == null) return null;
        return subjects.stream()
                .map(Subject::getSubjectId)
                .collect(Collectors.toList());
    }

    default List<Subject> mapSubjectsFromIds(List<Long> subjectIds) {
        if (subjectIds == null) return null;
        return subjectIds.stream()
                .map(id -> {
                    Subject subject = new Subject();
                    subject.setSubjectId(id);
                    return subject;
                })
                .collect(Collectors.toList());
    }
}
