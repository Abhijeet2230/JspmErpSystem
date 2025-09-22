package in.edu.jspmjscoe.admissionportal.mappers.subject;

import in.edu.jspmjscoe.admissionportal.dtos.subject.SubjectDTO;
import in.edu.jspmjscoe.admissionportal.model.subject.Course;
import in.edu.jspmjscoe.admissionportal.model.subject.ElectiveGroup;
import in.edu.jspmjscoe.admissionportal.model.subject.Subject;
import in.edu.jspmjscoe.admissionportal.model.teacher.TeacherSubject;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface SubjectMapper {

    // Entity → DTO
    @Mapping(source = "course.courseId", target = "courseId")
    @Mapping(source = "electiveGroup.groupId", target = "electiveGroupId")
    @Mapping(source = "teacherSubjects", target = "teacherSubjectIds")
    @Mapping(source = "subjectGroup", target = "subjectGroup")
    @Mapping(source = "hasCCE", target = "hasCCE") // 👈 add this
    SubjectDTO toDto(Subject subject);

    // DTO → Entity
    @InheritInverseConfiguration
    @Mapping(target = "course", expression = "java(mapCourse(dto.getCourseId()))")
    @Mapping(target = "electiveGroup", expression = "java(mapElectiveGroup(dto.getElectiveGroupId()))")
    @Mapping(target = "teacherSubjects", expression = "java(mapTeacherSubjectsFromIds(dto.getTeacherSubjectIds()))")
    @Mapping(source = "subjectGroup", target = "subjectGroup")
    @Mapping(source = "hasCCE", target = "hasCCE") // 👈 add this
    Subject toEntity(SubjectDTO dto);

    // ---------- Helper methods ----------
    default Course mapCourse(Long courseId) {
        if (courseId == null) return null;
        Course c = new Course();
        c.setCourseId(courseId);
        return c;
    }

    default ElectiveGroup mapElectiveGroup(Long groupId) {
        if (groupId == null) return null;
        ElectiveGroup eg = new ElectiveGroup();
        eg.setGroupId(groupId);
        return eg;
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
